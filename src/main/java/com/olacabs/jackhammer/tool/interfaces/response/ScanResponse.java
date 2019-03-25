package com.olacabs.jackhammer.tool.interfaces.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.*;
import com.olacabs.jackhammer.enums.Severities;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.utilities.EmailOperations;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
public class ScanResponse {


    @Inject
    @Named(Constants.SCAN_DAO)
    ScanDAO scanDAO;

    @Inject
    @Named(Constants.SCAN_TOOL_DAO)
    ScanToolDAO scanToolDAO;

    @Inject
    @Named(Constants.FINDING_DAO)
    FindingDAO findingDAO;

    @Inject
    @Named(Constants.SCAN_TYPE_DAO)
    ScanTypeDAO scanTypeDAO;

    @Inject
    ToolResponse toolResponse;

    @Inject
    JackhammerConfiguration jackhammerConfiguration;

    @Inject
    EmailOperations emailOperations;

    public void saveScanResponse(String scanResponse, long toolInstanceId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode scanNode = mapper.readTree(scanResponse);
            Scan scan = buildScanRecord(scanNode, mapper);
            Boolean fullListSent = updateToolInstanceRunningScanCount(toolInstanceId, mapper, scanNode);
            Scan dbScan = scanDAO.get(scan.getId());
            List<HashMap<String, String>> findingsList = mapper.convertValue(scanNode.get(Constants.FINDINGS), ArrayList.class);
            List<String> currentScanFingerPrints = currentScanFingerprints(dbScan);
            if (findingsList != null) {
                Iterator<HashMap<String, String>> iterator = findingsList.iterator();
                while (iterator.hasNext()) {
                    Map<String, String> findingMap = iterator.next();
                    String fingerprint = findingMap.get(Constants.FINGERPRINT);
                    String title = findingMap.get(Constants.TITLE);
                    if (!currentScanFingerPrints.contains(fingerprint) && !StringUtils.isEmpty(title)) {
                        Finding finding = buildFindingRecord(findingMap, dbScan);
                        updateSeverityCount(scan, findingMap.get(Constants.SEVERITY));
                        findingDAO.insert(finding);
                    }
                }
            }
            String scanTypeTitle = requireSendMail(dbScan, StringUtils.EMPTY);
            if (fullListSent && !StringUtils.equals(scanTypeTitle, StringUtils.EMPTY))
                emailOperations.sendAlertMail(dbScan, scanTypeTitle);
            scanDAO.update(scan);
            scanToolDAO.updateStatusPostScan(toolInstanceId, scan.getStatus(), dbScan.getId());
        } catch (IOException e) {
            log.error("Exception while saving findings....", e);
        } catch (NullPointerException e) {
            log.error("Exception while saving findings....", e);
        } catch (Exception e) {
            log.error("Exception while saving findings...", e);
        }
    }

    private Finding buildFindingRecord(Map<String, String> findingMap, Scan scan) {
        Finding finding = new Finding();
        try {
            finding.setName(findingMap.get(Constants.TITLE));
            finding.setDescription(findingMap.get(Constants.DESCRIPTION));
            finding.setToolName(findingMap.get(Constants.TOOL_NAME));
            finding.setFileName(findingMap.get(Constants.FILE_NAME));
            finding.setLineNumber(findingMap.get(Constants.LINE_NUMBER));
            finding.setCode(findingMap.get(Constants.CODE));
            finding.setSolution(findingMap.get(Constants.SOLUTION));
            finding.setCvssScore(findingMap.get(Constants.CVSS_SCORE));
            finding.setLocation(findingMap.get(Constants.LOCATION));
            finding.setUserInput(findingMap.get(Constants.USER_INPUT));
            finding.setAdvisory(findingMap.get(Constants.ADVISORY));
            finding.setPort(findingMap.get(Constants.PORT));
            finding.setProtocol(findingMap.get(Constants.PROTOCOL));
            finding.setState(findingMap.get(Constants.STATE));
            finding.setProduct(findingMap.get(Constants.PRODUCT));
            finding.setScripts(findingMap.get(Constants.SCRIPTS));
            finding.setVersion(findingMap.get(Constants.VERSION));
            finding.setHost(findingMap.get(Constants.HOST));
            finding.setRequest(findingMap.get(Constants.REQUEST));
            finding.setResponse(findingMap.get(Constants.RESPONSE));
            finding.setSeverity(findingMap.get(Constants.SEVERITY));
            finding.setFingerprint(findingMap.get(Constants.FINGERPRINT));
            finding.setCveCode(findingMap.get(Constants.CVE_CODE));
            finding.setCweCode(findingMap.get(Constants.CWE_CODE));
            finding.setScanId(scan.getId());
            finding.setGroupId(scan.getGroupId());
            finding.setRepoId(scan.getRepoId());
            finding.setUserId(scan.getUserId());
            finding.setScanTypeId(scan.getScanTypeId());
            finding.setOwnerTypeId(scan.getOwnerTypeId());
            if (findingMap.get(Constants.EXTERNAL_LINK) != null)
                finding.setExternalLink(findingMap.get(Constants.EXTERNAL_LINK).toString());
        } catch (Exception e) {
            log.error("Exception while building scan response", e);
        }

        return finding;
    }

    private Scan buildScanRecord(JsonNode scanNode, ObjectMapper mapper) {
        Scan scan = new Scan();
        long startTime = mapper.convertValue(scanNode.get(Constants.START_TIME), long.class);
        long endTime = mapper.convertValue(scanNode.get(Constants.END_TIME), long.class);
        Calendar calendar = Calendar.getInstance();
        java.util.Date currentTime = calendar.getTime();
        java.sql.Date currentDate = new java.sql.Date(currentTime.getTime());
        String status = mapper.convertValue(scanNode.get(Constants.STATUS), String.class);
        String finalScanStatus = getScanStatus(scan, status);
        scan.setId(mapper.convertValue(scanNode.get(Constants.SCAN_ID), long.class));
        scan.setRepoId(mapper.convertValue(scanNode.get(Constants.REPO_ID), long.class));
        scan.setStatus(finalScanStatus);
        scan.setStatusReason(mapper.convertValue(scanNode.get(Constants.FAILED_REASONS), String.class));
        scan.setStartTime(new Timestamp(startTime));
        scan.setEndTime(new Timestamp(endTime));
        scan.setCriticalCount(0);
        scan.setHighCount(0);
        scan.setMediumCount(0);
        scan.setLowCount(0);
        scan.setInfoCount(0);
        scan.setLastRunDate(currentDate);
        return scan;
    }

    private void updateSeverityCount(Scan scan, String severity) {
        switch (Severities.valueOf(severity.toUpperCase())) {
            case CRITICAL:
                scan.setCriticalCount(scan.getCriticalCount() + 1);
                break;
            case HIGH:
                scan.setHighCount(scan.getHighCount() + 1);
                break;
            case MEDIUM:
                scan.setMediumCount(scan.getMediumCount() + 1);
                break;
            case LOW:
                scan.setLowCount(scan.getLowCount() + 1);
                break;
            case INFO:
                scan.setInfoCount(scan.getInfoCount() + 1);
                break;
            default:
                log.info("Invalid severity {} {}", severity);
        }
    }

    private String getScanStatus(Scan scan, String currentToolStatus) {
        String currentScanStatus = currentToolStatus;
        List<ScanTool> queuedScanTools = scanToolDAO.getQueuedScanTools(scan.getId());
        if (queuedScanTools.size() > 0) {
            currentScanStatus = Constants.SCAN_QUEUED_STATUS;
            return currentScanStatus;
        }
        List<ScanTool> getFailedScanTools = scanToolDAO.getFailedScanTools(scan.getId());
        if (getFailedScanTools.size() > 0) {
            currentScanStatus = Constants.SCAN_QUEUED_STATUS;
            return currentScanStatus;
        }
        return currentScanStatus;
    }

    private List<String> currentScanFingerprints(Scan scan) {
        List<String> fingerprints = new ArrayList();
        List<Finding> findings = findingDAO.findByScanId(scan.getId());
        for (Finding finding : findings) {
            fingerprints.add(finding.getFingerprint());
        }
        return fingerprints;
    }

    private Boolean updateToolInstanceRunningScanCount(long toolInstanceId, ObjectMapper mapper, JsonNode scanNode) {
        Boolean fullListSent = mapper.convertValue(scanNode.get(Constants.SENT_FULL_LIST), Boolean.class);
        if (fullListSent != null && fullListSent) {
            toolResponse.decreaseRunningScans(toolInstanceId);
        }
        return fullListSent != null && fullListSent;
    }

    private String requireSendMail(Scan scan, String scanTitle) {
        ScanType scanType = scanTypeDAO.findScanTypeById(scan.getScanTypeId());
        Boolean wpScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getWpScanAlerts();
        Boolean mobileScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getMobileScanAlerts();
        Boolean networkScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getNetworkScanAlerts();
        Boolean webScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getWebScanAlerts();
        Boolean hardcodeSecretScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getHardcodeSecretScanAlerts();
        Boolean staticCodeScanAlerts = jackhammerConfiguration.getScanMangerConfiguration().getStaticCodeScanAlerts();
        if (scanType.getIsStatic() && staticCodeScanAlerts) {
            return scanType.getName();
        }

        if (scanType.getIsHardCodeSecret() && hardcodeSecretScanAlerts) {
            return scanType.getName();
        }

        if (scanType.getIsWeb() && webScanAlerts) {
            return scanType.getName();
        }

        if (scanType.getIsNetwork() && networkScanAlerts) {
            return scanType.getName();
        }

        if (scanType.getIsMobile() && mobileScanAlerts) {
            return scanType.getName();
        }

        if (scanType.getIsWordpress() && wpScanAlerts) {
            return scanType.getName();
        }
        return scanTitle;
    }
}
