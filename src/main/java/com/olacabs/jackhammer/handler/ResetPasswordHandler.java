package com.olacabs.jackhammer.handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.HandlerNotFoundException;
import com.olacabs.jackhammer.models.JwtToken;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.utilities.EmailOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.ws.rs.core.Response;

@Slf4j
public class ResetPasswordHandler extends AbstractHandler<User> {

    @Inject
    EmailOperations emailOperations;

    @Inject
    @Named(Constants.BCRYPT_PASSWORD_ENCODER)
    PasswordEncoder passwordEncoder;

    @Override
    public Response getAllRecords(User model) throws HandlerNotFoundException {
        return null;
    }

    @Override
    public Response createRecord(User user) throws HandlerNotFoundException {
        try {
            validatorBuilderFactory.getValidator(Handler.RESET_PASSWORD_SERVICE).dataValidations(user);
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
            JwtToken jwtToken = new JwtToken();
            jwtToken.setUserId(dbUser.getId());
            JwtToken insertedJwt = (JwtToken) dataServiceBuilderFactory.getService(Handler.JWT_SERVICE).createRecord(jwtToken);
            dbUser.setUserToken(insertedJwt.getUserToken());
            emailOperations.sendPasswordInstructions(dbUser);
            return responseBuilderFactory.getResponseBuilder(Handler.RESET_PASSWORD_SERVICE).buildSuccessResponse();
        } catch (AbstractException e) {
            log.error("Exception while validating email exist",e);
            return responseBuilderFactory.getResponseBuilder(Handler.RESET_PASSWORD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response getRecord(long id) throws HandlerNotFoundException {
        return null;
    }

    @Override
    public Response updateRecord(User user) throws HandlerNotFoundException {
        try {
            user.setNewPassword(user.getPassword());
            validatorBuilderFactory.getValidator(Handler.CHANGE_PASSWORD_SERVICE).dataValidations(user);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setNewPassword(passwordEncoder.encode(user.getPassword()));
            User currentUser = currentUser(user.getUserToken());
            User dbUser = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(currentUser);
            user.setId(dbUser.getId());
            dataServiceBuilderFactory.getService(Handler.RESET_PASSWORD_SERVICE).updateRecord(user);
            JwtToken jwtToken = createJwtToken(dbUser);
            dbUser.setUserToken(jwtToken.getUserToken());
            return responseBuilderFactory.getResponseBuilder(Handler.USER_SERVICE).loginResponse(dbUser);
        } catch (AbstractException e) {
            log.error("Exception while updating User");
            return responseBuilderFactory.getResponseBuilder(Handler.CHANGE_PASSWORD_SERVICE).buildErrorResponse(e);
        }
    }

    @Override
    public Response deleteRecord(long id) throws HandlerNotFoundException {
        return null;
    }
}
