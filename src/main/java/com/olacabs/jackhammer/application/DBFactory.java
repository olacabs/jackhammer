package com.olacabs.jackhammer.application;

import org.skife.jdbi.v2.DBI;

import com.olacabs.jackhammer.exceptions.DBConfigIntializationRequired;

public class DBFactory {
    private static DBI dbi;
    protected DBFactory(DBI dbi) {
        DBFactory.dbi = dbi;
    }
    public static DBI getDBI(){
        if(null != dbi){
            return dbi;
        }
        throw new DBConfigIntializationRequired();
    }

}
