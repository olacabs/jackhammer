package com.olacabs.jackhammer.utilities;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.exceptions.WritingFileException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public class FileOperations {

    @Inject
    JackhammerConfiguration jackhammerConfiguration;


    public void copyUploadFile(InputStream uploadedInputStream, long findingId, String fileName) throws WritingFileException {
        String uploadDir = jackhammerConfiguration.getFileConfiguration().getTargetDirectory();
        String uploadedFileLocation = uploadDir + "/" + findingId;
        File destinationDir = new File(uploadDir);
        if (!destinationDir.exists()) destinationDir.mkdir();
        try {
            copyFileToDestinationDir(uploadedInputStream,fileName, uploadedFileLocation);
        } catch (WritingFileException e) {
            throw new WritingFileException(ExceptionMessages.WRITE_FILE_EXCEPTION, e, CustomErrorCodes.WRITE_FILE_EXCEPTION);
        }
    }

    public void copyToolManifestFile(InputStream uploadedInputStream, long toolId, String fileName) throws WritingFileException {
        String toolDir = jackhammerConfiguration.getFileConfiguration().getToolsDir();
        String uploadedFileLocation = toolDir + "/" + toolId;
        File destinationDir = new File(toolDir);
        if (!destinationDir.exists()) destinationDir.mkdir();
        try {
            copyFileToDestinationDir(uploadedInputStream,fileName, uploadedFileLocation);
        } catch (WritingFileException e) {
            throw new WritingFileException(ExceptionMessages.WRITE_FILE_EXCEPTION, e, CustomErrorCodes.WRITE_FILE_EXCEPTION);
        }
    }

    public void copyFileToDestinationDir(InputStream uploadedInputStream,String fileName, String uploadedFileLocation) throws WritingFileException {
        try {
            File uploadFile = new File(uploadedFileLocation);
            if (!uploadFile.exists()) uploadFile.mkdir(); //make specific dir
            Path outputPath = FileSystems.getDefault().getPath(uploadedFileLocation, fileName);
            Files.copy(uploadedInputStream, outputPath);
        } catch (IOException e) {
            throw new WritingFileException(ExceptionMessages.WRITE_FILE_EXCEPTION, e, CustomErrorCodes.WRITE_FILE_EXCEPTION);
        }
    }
}
