package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.DataServiceNotFoundException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.ScanType;
import com.olacabs.jackhammer.models.Tool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ToolValidator extends AbstractValidator<Tool> {

    @Override
    public void dataValidations(Tool tool) throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(tool.getName());
            Preconditions.checkNotNull(tool.getScanTypeId());
            Preconditions.checkNotNull(tool.getUploadedInputStream());
            ScanType scanType = (ScanType) dataServiceBuilderFactory.getService(Handler.SCAN_TYPE_SERVICE).fetchRecordById(tool.getScanTypeId());
            if (scanType.getIsStatic()) {
                Preconditions.checkNotNull(tool.getLanguageId());
            }
        } catch (NullPointerException npe) {
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        } catch (DataServiceNotFoundException dne) {
            log.error("Handler not found while validating tool", dne);
        }
    }

    @Override
    public void uniquenessValidations(Tool tool) throws ValidationFailedException {
        Tool dbTool = null;
        try {
            dbTool = (Tool) dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).fetchRecordByname(tool);
        } catch (DataServiceNotFoundException dne) {
            log.error("Handler not found while validating tool", dne);
        }
        if (dbTool != null)
            throw new ValidationFailedException(ExceptionMessages.TOOL_ALREADY_EXISTS, null, CustomErrorCodes.TOOL_ALREADY_EXISTS);
    }
}
