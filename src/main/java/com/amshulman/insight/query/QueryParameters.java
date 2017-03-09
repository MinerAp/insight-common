package com.amshulman.insight.query;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.Value;

import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.types.InsightLocation;
import com.amshulman.insight.types.InsightMaterial;

@Value
public final class QueryParameters {

    public static final int WORLDEDIT = -1;

    Set<String> worlds;
    Set<String> actors;
    Set<InsightAction> actions;
    Set<String> actees;
    Set<InsightMaterial> materials;

    boolean orderReversed;
    boolean invertActors;
    boolean invertActions;
    boolean invertActees;
    boolean invertMaterials;
    boolean locationSet;

    int minX;
    int maxX;
    int minY;
    int maxY;
    int minZ;
    int maxZ;
    int radius;
    InsightLocation point;

    LocalDateTime after;
    LocalDateTime before;
}
