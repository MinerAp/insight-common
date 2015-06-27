package com.amshulman.insight.backend;

import java.util.Set;

import com.amshulman.insight.query.QueryParameterBuilder;
import com.amshulman.insight.query.QueryParameters;
import com.amshulman.insight.results.InsightResultSet;

public interface ReadBackend extends AutoCloseable {

    /**
     * Query the backend for results matching the specified QueryParameters.
     *
     * @param params
     *            The result of {@link QueryParameterBuilder#build()}
     * @return Any results matching the query parameters
     */
    public InsightResultSet submit(QueryParameters params);

    /**
     * Get a list of worlds which have data stored in the backend
     *
     * @return A list of worlds
     */
    public Set<String> getWorlds();

    /**
     * Get a {@link QueryParameterBuilder} for use with {@link #submit(QueryParameters)}
     *
     * @return A new {@link QueryParameterBuilder}.
     */
    public QueryParameterBuilder newQueryBuilder();

    @Override
    public void close();
}
