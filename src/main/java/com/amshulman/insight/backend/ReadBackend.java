package com.amshulman.insight.backend;

import java.util.Set;

import com.amshulman.insight.query.QueryParameterBuilder;
import com.amshulman.insight.query.QueryParameters;
import com.amshulman.insight.results.InsightResultSet;

public interface ReadBackend extends AutoCloseable {

    public abstract InsightResultSet submit(QueryParameters params);

    public Set<String> getWorlds();

    public QueryParameterBuilder newQueryBuilder();

    public abstract void close();
}
