package com.olacabs.jackhammer.handler;

import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.Tool;
import com.olacabs.jackhammer.models.User;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

@Slf4j
public class ToolsHandler extends AbstractHandler<Tool> {


    @Override
    public Response getAllRecords(Tool tool) throws HandlerNotFoundException {
        try {
            User user = currentUser(tool.getUserToken());
            tool.setUser(user);
            paginationRecords = dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).getAllRecords(tool);
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildFetchAllRecordsResponse(paginationRecords);
        } catch(AbstractException e){
            log.error("Exception while fetching tools");
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response createRecord(Tool tool) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.TOOL_SERVICE).dataValidations(tool);
            validatorBuilderFactory.getValidator(Handler.TOOL_SERVICE).uniquenessValidations(tool);
            Tool dbTool = (Tool) dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).createRecord(tool);
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while creating tool");
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildErrorResponse(e);
        }
    }
    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        try {
            Tool tool = (Tool) dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).fetchRecordById(id);
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildFetchRecordResponse(tool);
        } catch (AbstractException e){
            log.error("Exception while getting tool");
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response updateRecord(Tool tool) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.TOOL_SERVICE).dataValidations(tool);
            dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).updateRecord(tool);
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while updating tool");
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        try {
            dataServiceBuilderFactory.getService(Handler.TOOL_SERVICE).deleteRecord(id);
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildSuccessResponse();
        } catch (AbstractException e){
            log.error("Exception while deleting tool");
            return responseBuilderFactory.getResponseBuilder(Handler.TOOL_SERVICE).buildErrorResponse(e);
        }
    }
}
