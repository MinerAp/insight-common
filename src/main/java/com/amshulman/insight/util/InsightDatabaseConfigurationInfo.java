package com.amshulman.insight.util;

import java.util.logging.Logger;

import com.amshulman.insight.backend.BackendType;

public interface InsightDatabaseConfigurationInfo {

    public String getDatabaseAddress();

    public int getDatabasePort();

    public String getDatabaseName();

    public String getDatabaseUsername();

    public String getDatabasePassword();

    public BackendType getDatabaseType();

    public Logger getLogger();
}
