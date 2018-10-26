package com.olacabs.jackhammer.response.builder;


import javax.ws.rs.core.*;

import com.olacabs.jackhammer.common.*;
import com.olacabs.jackhammer.exceptions.OperationFailedException;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.models.*;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class AbstractResponseBuilder<T extends AbstractModel> {

    public abstract Response buildFetchRecordResponse(T model) throws OperationFailedException;

    public Response buildFetchAllRecordsResponse(PagedResponse pagedResponse) {
        return Response.status(CustomErrorCodes.HTTP_RESPONSE_SUCCESS.getValue()).entity(pagedResponse).build();
    }

    public Response buildSuccessResponse() {
        SuccessResponseModel successResponseModel = new SuccessResponseModel();
        successResponseModel.setSuccessCode(HttpResponseCodes.HTTP_RESPONSE_SUCCESS);
        successResponseModel.setMessage(HttpResponseMessages.HTTP_RESPONSE_SUCCESS);
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS).entity(successResponseModel).build();
    }

    public Response buildErrorResponse(AbstractException e) {
        log.error(e.getMessage(), e);
        ErrorResponseModel errorResponseModel = new ErrorResponseModel();
        errorResponseModel.setErrorCode(e.getCode());
        errorResponseModel.setMessage(e.getMessage());
        return Response.status(CustomErrorCodes.FORBIDDEN.getValue()).entity(errorResponseModel).build();
    }


    public Response loginResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setMessage(HttpResponseMessages.HTTP_RESPONSE_SUCCESS);
        userResponse.setSuccessCode(HttpResponseCodes.HTTP_RESPONSE_SUCCESS);
        userResponse.setUsername(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setAllowedExecutiveDashboard(user.getAllowedExecutiveDashboard());
        userResponse.setAllowedCorporateDashboard(user.getAllowedCorporateDashboard());
        userResponse.setAllowedTeamDashboard(user.getAllowedTeamDashboard());
        userResponse.setAllowedPersonalDashboard(user.getAllowedPersonalDashboard());
        userResponse.setOwnerTypes(user.getOwnerTypes());
//        NewCookie accessTokenCookie = new NewCookie(new Cookie(HttpKeys.ACCESS_TOKEN,user.getUserToken(),
//                "/","localhost"),"/",NewCookie.DEFAULT_MAX_AGE, DateTime.now().plusHours(24).toDate(),false,false);

        log.debug("built the registration success response..{}", userResponse);
        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS)
                .header(HttpKeys.ACCESS_TOKEN, user.getUserToken())
                .header("Access-Control-Expose-Headers", HttpKeys.ACCESS_TOKEN)
                .entity(userResponse)
                .build();
    }

    public Response logoutResponse() {
        NewCookie accessTokenCookie = new NewCookie(new Cookie(HttpKeys.ACCESS_TOKEN, null,
                "/", "localhost"),
                "/", 0,
                new Date(System.currentTimeMillis()), false,
                false);

        return Response.status(HttpResponseCodes.HTTP_RESPONSE_SUCCESS)
                .cookie(accessTokenCookie)
                .build();
    }

    public Response exportFindingsToCSV(PagedResponse pagedResponse) {
        List<T> findings = pagedResponse.getItems();
        String fileExtension = Constants.CSV_FORMAT;
        try {
            File decryptionFile = File.createTempFile(Constants.DOWNLOAD_TEMP_FILE_PREFIX, fileExtension);
            CSVWriter csvWriter = getCSVWriter(decryptionFile);
            csvWriter.writeNext(fetchHeaders());
            for (T eachFinding : findings) {
                Finding finding = (Finding) eachFinding;
                String[] findingDetails = fetchFindingRecord(finding);
                csvWriter.writeNext(findingDetails);
            }
            csvWriter.close();
            Scan scan = (Scan) pagedResponse.getItem();
            StringBuilder fileName = new StringBuilder();
            fileName.append(Constants.DOWNLOAD_TEMP_FILE_PREFIX);
            fileName.append(scan.getName());
            fileName.append(Constants.CSV_FORMAT);
            return Response.ok(new FileInputStream(decryptionFile.getAbsolutePath()), MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=" + "\"" + fileName.toString() + "\"")
                    .build();
        } catch (IOException io) {
            log.error("IOException while exporting finding values {} {}", io);
            log.error(io.getMessage(), io);
            ErrorResponseModel errorResponseModel = new ErrorResponseModel();
            errorResponseModel.setErrorCode(CustomErrorCodes.FAILED);
            errorResponseModel.setMessage(io.getMessage());
            return Response.status(CustomErrorCodes.FORBIDDEN.getValue()).entity(errorResponseModel).build();
        }
    }

    private CSVWriter getCSVWriter(File file) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(file), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        return csvWriter;
    }

    private String[] fetchFindingRecord(Finding finding) {
        List<String> findingDetails = new ArrayList();
        findingDetails.add(finding.getName());
        findingDetails.add(finding.getSeverity());
        findingDetails.add(finding.getDescription());
        findingDetails.add(finding.getToolName());
        findingDetails.add(finding.getFileName());
        findingDetails.add(finding.getLineNumber());
        findingDetails.add(finding.getCode());
        findingDetails.add(finding.getStatus());
        findingDetails.add(finding.getIsFalsePositive().toString());
        findingDetails.add(finding.getExternalLink());
        findingDetails.add(finding.getSolution());
        findingDetails.add(finding.getCvssScore());
        findingDetails.add(finding.getLocation());
        findingDetails.add(finding.getUserInput());
        findingDetails.add(finding.getPort());
        findingDetails.add(finding.getProtocol());
        findingDetails.add(finding.getState());
        findingDetails.add(finding.getProduct());
        findingDetails.add(finding.getScripts());
        findingDetails.add(finding.getVersion());
        findingDetails.add(finding.getHost());
        findingDetails.add(finding.getRequest());
        findingDetails.add(finding.getResponse());
        findingDetails.add(finding.getNotExploitable().toString());
        findingDetails.add(finding.getAdvisory());
        findingDetails.add(finding.getCveCode());
        findingDetails.add(finding.getCweCode());
        return findingDetails.toArray(new String[0]);
    }

    private String[] fetchHeaders() {
        String[] headers = {"Bug Type", "Severity", "Description", "Tool Name", "File Name", "Line Number", "Code", "Status", "Is False Positive",
                "External Link", "Solution", "Cvss Score", "Location", "User Input", "Port", "Protocol", "State", "Product", "Scripts", "Version",
                "Host", "Request", "Response", "Not Exploitable", "Advisory", "Cve Code", "CWE Code"};
        return headers;
    }
}
