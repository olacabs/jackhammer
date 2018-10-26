package com.olacabs.jackhammer.validations;

import com.google.common.base.Preconditions;
import com.olacabs.jackhammer.common.CustomErrorCodes;
import com.olacabs.jackhammer.common.ExceptionMessages;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.exceptions.ValidationFailedException;
import com.olacabs.jackhammer.models.Repo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RepoValidator extends AbstractValidator<Repo> {

    @Override
    public void dataValidations(Repo repo)  throws ValidationFailedException {
        try {
            Preconditions.checkNotNull(repo.getName());
        } catch (NullPointerException npe){
            throw new ValidationFailedException(ExceptionMessages.INVALID_DATA, npe, CustomErrorCodes.INVALID_DATA);
        }

    }
    @Override
    public void uniquenessValidations(Repo repo) throws ValidationFailedException {
        Repo repoPresent = null;
        try {
            repoPresent =  (Repo) dataServiceBuilderFactory.getService(Handler.REPO_SERVICE).fetchRecordByname(repo);
            if(repoPresent!=null)
                throw new ValidationFailedException(ExceptionMessages.REPO_ALREADY_EXISTS,null,CustomErrorCodes.REPO_ALREADY_EXISTS);
        } catch (AbstractException e){
            log.error("Handler not found while validating repo");
        }
        if(repoPresent!=null) throw new ValidationFailedException(ExceptionMessages.REPO_ALREADY_EXISTS,null,CustomErrorCodes.REPO_ALREADY_EXISTS);

    }
}
