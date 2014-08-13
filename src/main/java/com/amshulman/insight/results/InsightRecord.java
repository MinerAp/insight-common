package com.amshulman.insight.results;

import java.util.Date;

import lombok.Value;

import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.serialization.StorageMetadata;
import com.amshulman.insight.types.InsightMaterial;

@Value
public class InsightRecord {
    private final Date datetime;
    private final String actor;
    private final InsightAction action;
    private final int x;
    private final int y;
    private final int z;
    private final String world;
    private final InsightMaterial material;
    private final String actee;
    private final StorageMetadata metadata;
}
