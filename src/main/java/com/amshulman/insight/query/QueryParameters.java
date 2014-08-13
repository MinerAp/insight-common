package com.amshulman.insight.query;

import java.util.Date;
import java.util.Set;

import lombok.Value;

import org.bukkit.Location;

import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.types.InsightMaterial;

@Value
public class QueryParameters {

    public static final int WORLDEDIT = -1;

    Set<String> worlds;
    Set<String> actors;
    Set<InsightAction> actions;
    Set<String> actees;
    Set<InsightMaterial> materials;

    boolean reverseOrder;
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
    Location point;

    Date after;
    Date before;
}
