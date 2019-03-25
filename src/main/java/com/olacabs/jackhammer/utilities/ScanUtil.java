package com.olacabs.jackhammer.utilities;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.security.AES;
import com.olacabs.jackhammer.tool.interfaces.sdk.bridge.SdkCommunicator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import liquibase.util.file.FilenameUtils;

import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.exceptions.GitCloneException;
import com.olacabs.jackhammer.exceptions.TempDirCreationException;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class ScanUtil {

    @Inject
    SdkCommunicator sdkCommunicator;

    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.TOOL_DAO)
    ToolDAO toolDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    @Named(Constants.LANGUAGE_DAO)
    LanguageDAO languageDAO;

    @Inject
    @Named(Constants.GIT_DAO)
    GitDAO gitDAO;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    public void startScan(Scan scan) {
        try {
            log.info("picking scan with id {} {}", scan.getId());
            scan.setStatus(Constants.SCAN_PICKED_STATUS);
            scanDAO.updateScanStatus(scan);
            ScanType scanType = scanTypeDAO.findScanTypeById(scan.getScanTypeId());
            if (scan.getIsTaggedTools() == false) {
                Path tempDirPath = null;
                if (scanType.getIsStatic() || scanType.getIsHardCodeSecret()) {
                    tempDirPath = createTempDirectory();
                    if (!cloneRepo(scan, tempDirPath)) {
                        scan.setStatus(Constants.SCAN_FAILED_STATUS);
                        scan.setStatusReason(Constants.GIT_CLONE_FAILED);
                        scanDAO.updateScanStatusandReason(scan);
                        return;
                    }
                }
                tagPlatform(scan, tempDirPath);
            }
            if (scan.isSupported() && SdkCommunicator.clients != null && SdkCommunicator.clients.size() > 0) {
                Boolean toolsTagged = tagScanTools(scan);
                if (toolsTagged) sdkCommunicator.sendScanRequest(scan);
            } else if (scan.isSupported() == false) {
                String failedMessage = (scanType.getIsStatic() || scanType.getIsHardCodeSecret()) && scan.isAccessible() == false ? Constants.STATIC_SCAN_FAILED_MESSAGE : Constants.TOOLS_NOT_SUPPORTED;
                scan.setStatus(Constants.SCAN_FAILED_STATUS);
                scan.setStatusReason(failedMessage);
                scanDAO.updateScanStatusandReason(scan);
            }
        } catch (Exception e) {
            log.error("Exception while fetching pending scans", e);
        } catch (Throwable e) {
            log.info("Error while sending scans", e);
        }
    }


    public Path createTempDirectory() throws TempDirCreationException {
        Path tempDirPath;
        try {
            tempDirPath = Files.createTempDirectory(Constants.TEMP_DIR_PREFIX);
        } catch (IOException io) {
            throw new TempDirCreationException(ExceptionMessages.TEMP_DIR_CREATION_ERROR, null, CustomErrorCodes.TEMP_DIR_CREATION_ERROR);
        }
        return tempDirPath;
    }

    public Boolean cloneRepo(Scan scan, Path tmpDir) throws GitCloneException {
        StringBuilder command = getGitCloneProcessBuilderWithCredentials(scan);
        String gitCommand = command.toString() + Constants.STRING_SPACER + tmpDir.toAbsolutePath().toString();
        try {
            return runCloneCmd(gitCommand);
        } catch (Exception e) {
            log.error("Error while cloning the repo", e);
            return false;
        } catch (Throwable th) {
            log.error("Error while cloning the repo", th);
            return false;
        }
    }


    public void tagPlatform(Scan scan, Path tmpDir) {
        try {
            scan.setSupported(false);
            List<Long> scanToolIds = Lists.newArrayList();
            ScanType scanType = scanTypeDAO.findScanTypeById(scan.getScanTypeId());
            if (scanType.getIsStatic()) {
                tagStaticPlatform(scan, scanToolIds, tmpDir);
            } else {
                if (scanType.getIsHardCodeSecret()) scan.setCloneRequired(true);
                tagNonStaticPlatform(scan, scanType, scanToolIds);
            }
            if (scanType.getIsHardCodeSecret() || scanType.getIsStatic()) {
                File targetDir = new File(tmpDir.toAbsolutePath().toString());
                if (targetDir.exists()) targetDir.delete();
            }
            if (scan.getPlatforms().size() > 0) {
                scan.setSupported(true);
                scan.setScanPlatforms(String.join(Constants.COMMA, scan.getPlatforms()));
            } else {
                String failedMessage = (scanType.getIsStatic() || scanType.getIsHardCodeSecret()) && scan.isAccessible() == false ? Constants.STATIC_SCAN_FAILED_MESSAGE : Constants.TOOLS_NOT_SUPPORTED;
                scan.setStatus(Constants.SCAN_FAILED_STATUS);
                scan.setStatusReason(failedMessage);
                scanDAO.updateScanStatusandReason(scan);
            }
            scanDAO.updatedScanDetails(scan);
        } catch (Exception e) {
            log.error("Error while doing tagPlatform.....", e);
        } catch (Throwable th) {
            log.error("Error while doing tagPlatform.....", th);
        }
    }

    public Boolean tagScanTools(Scan scan) {
        List<ScanTool> scanTools = scanToolDAO.getQueuedScanTools(scan.getId());
        ScanType scanType = scanTypeDAO.findScanTypeById(scan.getScanTypeId());
        if (scanTools.size() == 0) {
            scanToolDAO.deleteScanTools(scan.getId());
            Path tempDirPath = null;
            if (scanType.getIsStatic() || scanType.getIsHardCodeSecret()) {
                try {
                    tempDirPath = createTempDirectory();
                    if (!cloneRepo(scan, tempDirPath)) {
                        scan.setStatus(Constants.SCAN_FAILED_STATUS);
                        scan.setStatusReason(Constants.GIT_CLONE_FAILED);
                        scanDAO.updateScanStatusandReason(scan);
                        return false;
                    }
                } catch (GitCloneException gce) {
                    log.error("GitCloneException => ", gce);
                } catch (TempDirCreationException tce) {
                    log.error("TempDirCreationException => ", tce);
                }
            }
            tagPlatform(scan, tempDirPath);
        } else {
            for (ScanTool scanTool : scanTools) {
                Tool tool = toolDAO.get(scanTool.getToolId());
                scan.addTool(tool);
            }
        }
        if (scanType.getIsStatic() || scanType.getIsHardCodeSecret()) {
            StringBuilder gitCloneCmd = getGitCloneProcessBuilderWithCredentials(scan);
            scan.setTarget(gitCloneCmd.toString());
        } else {
            scan.setCloneRequired(false);
        }
        if (scanType.getIsMobile()) {
            scan.setIsMobileScan(true);
        } else {
            scan.setIsMobileScan(false);
        }
        return true;
    }

    private Boolean runCloneCmd(final String command) throws IOException, InterruptedException, ExecutionException {
        Boolean status;
        final Duration timeout = Duration.ofMinutes(5);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        final Future<String> handler = executor.submit(new Callable() {
            @Override
            public String call() throws Exception {
                log.info("started cloning the repo.....");
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                return "Success";
            }
        });
        try {
            handler.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            log.info("cloning completed.....");
            status = true;
        } catch (TimeoutException e) {
            handler.cancel(true);
            status = false;
            log.info("TimeoutException while cloning the repo.....");
        }
        executor.shutdownNow();
        return status;
    }

    private void tagStaticPlatform(Scan scan, List<Long> scanToolIds, Path tmpDir) {
        HashMap<String, Language> languagesHash = new HashMap<String, Language>();
        List<Language> languageList = languageDAO.getLanguages();
        for (Language language : languageList) {
            languagesHash.put(language.getFileExtension(), language);
        }
        File modifiedTempDir = new File(tmpDir.toAbsolutePath().toString());
        if (modifiedTempDir != null && modifiedTempDir.list() != null && modifiedTempDir.list().length > 0) {
            List<File> files = (List<File>) FileUtils.listFiles(modifiedTempDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            for (File file : files) {
                if (file.isFile()) {
                    String fileExtension = FilenameUtils.getExtension(file.getAbsolutePath());
                    Language language = languagesHash.get(fileExtension);
                    if (language != null) pickStaticPlatforms(scan, language, scanToolIds);
                }
            }
            scan.setCloneRequired(true);
        } else {
            scan.setAccessible(false);
        }
    }

    private void tagNonStaticPlatform(Scan scan, ScanType scanType, List<Long> scanToolIds) {
        scan.getPlatforms().add(scanType.getName().toLowerCase());
        List<Tool> scanTypeTools = toolDAO.findByScanTypeId(scanType.getId());
        for (Tool eachScanTypeTool : scanTypeTools) {
            if (!scanToolIds.contains(eachScanTypeTool.getId())) {
                scanToolIds.add(eachScanTypeTool.getId());
                scan.addTool(eachScanTypeTool);
                ScanTool scanTool = new ScanTool();
                scanTool.setScanId(scan.getId());
                scanTool.setToolId(eachScanTypeTool.getId());
                scanToolDAO.insert(scanTool);
            }
        }
    }

    private void pickStaticPlatforms(Scan scan, Language language, List<Long> scanToolIds) {
        List<Tool> languageTools = toolDAO.findByLanguageId(language.getId());
        if (languageTools.size() == 0) return;
        scan.getPlatforms().add(language.getName().toLowerCase());
        for (Tool eachLanguageTool : languageTools) {
            if (!scanToolIds.contains(eachLanguageTool.getId())) {
                scanToolIds.add(eachLanguageTool.getId());
                scan.addTool(eachLanguageTool);
                ScanTool scanTool = new ScanTool();
                scanTool.setScanId(scan.getId());
                scanTool.setToolId(eachLanguageTool.getId());
                scanToolDAO.insert(scanTool);
            }
        }
    }

    private StringBuilder getGitCloneProcessBuilderWithCredentials(Scan scan) {
        Git git = gitDAO.get();
        String target = scan.getTarget();
        String internalUrl = jackhammerConfiguration.getGitConfiguration().getInternalUrl();
        String externalUrl = jackhammerConfiguration.getGitConfiguration().getExternalUrl();
        if (!StringUtils.isEmpty(internalUrl)) target = target.replace(externalUrl, internalUrl);
        StringBuilder gitCmd = new StringBuilder();
        try {
            if (git != null) {
                String privateToken = AES.decrypt(git.getApiAccessToken(), jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey());
                StringBuilder targetWithCredentials = new StringBuilder();
                String repoUrlWithoutHttps = target.split(Constants.GIT_HTTPS)[1];
                targetWithCredentials.append(Constants.GIT_HTTPS);
                targetWithCredentials.append(git.getUserName());
                targetWithCredentials.append(Constants.COLON);
                targetWithCredentials.append(privateToken);
                targetWithCredentials.append(Constants.AT_THE_RATE);
                targetWithCredentials.append(repoUrlWithoutHttps);
                target = targetWithCredentials.toString();
            }
            gitCmd.append(Constants.GIT);
            gitCmd.append(Constants.STRING_SPACER);
            gitCmd.append(Constants.CLONE);
            if (scan.getBranch() != null) {
                gitCmd.append(Constants.STRING_SPACER);
                gitCmd.append(Constants.BRANCH_ARG_OPTION);
                gitCmd.append(Constants.STRING_SPACER);
                gitCmd.append(scan.getBranch());
            }
            gitCmd.append(Constants.STRING_SPACER);
            gitCmd.append(target);
        } catch (Exception e) {
            log.error("Error while building clone command", e);
        } catch (Throwable th) {
            log.error("Error while building git clone command", th);
        }
        return gitCmd;
    }
}
