package com.amshulman.insight.query;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;

import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.types.InsightMaterial;
import com.google.common.base.CharMatcher;

public class QueryParameterBuilder {

    private final Set<String> worlds = new HashSet<String>(5);
    private final Set<String> actors = new HashSet<String>(10);
    private final Set<InsightAction> actions = new HashSet<InsightAction>(10);
    private final Set<String> actees = new HashSet<String>(10);
    private final Set<InsightMaterial> materials = new HashSet<InsightMaterial>(10);

    private boolean reverseOrder = false;
    private boolean invertActors = false;
    private boolean invertActions = false;
    private boolean invertActees = false;
    private boolean invertMaterials = false;
    private boolean locationSet = false;

    private int minX = 0;
    private int maxX = 0;
    private int minY = 0;
    private int maxY = 0;
    private int minZ = 0;
    private int maxZ = 0;
    private int radius = 0;
    private Location point = null;

    private Date after;
    private Date before;

    private boolean built = false;

    public QueryParameters build() {
        assert (!built);

        built = true;
        return new QueryParameters(worlds, actors, actions, actees, materials, reverseOrder, invertActors, invertActions, invertActees, invertMaterials, locationSet, minX, maxX, minY, maxY, minZ, maxZ, radius, point, after, before);
    }

    public QueryParameterBuilder reverseOrder() {
        assert (!built);

        reverseOrder = true;
        return this;
    }

    public QueryParameterBuilder addWorld(String world) {
        assert (!built);

        if (!CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf("_-")).matchesAllOf(world)) {
            throw new IllegalArgumentException(world + " contains unacceptable special characters");
        }

        if (point != null && !point.getWorld().getName().equals(world)) {
            throw new IllegalStateException("You may not specify both the world and location in another world");
        }

        worlds.add(world);
        return this;
    }

    public QueryParameterBuilder invertActors() {
        assert (!built);

        invertActors = true;
        return this;
    }

    public QueryParameterBuilder addActor(String entity) {
        assert (!built);

        actors.add(entity);
        return this;
    }

    public QueryParameterBuilder invertActions() {
        assert (!built);

        invertActions = true;
        return this;
    }

    public QueryParameterBuilder addAction(InsightAction action) {
        assert (!built);

        actions.add(action);
        return this;
    }

    public QueryParameterBuilder invertActees() {
        assert (!built);

        invertActees = true;
        return this;
    }

    public QueryParameterBuilder addActee(String entity) {
        assert (!built);

        actees.add(entity);
        return this;
    }

    public QueryParameterBuilder invertMaterials() {
        assert (!built);

        invertMaterials = true;
        return this;
    }

    public QueryParameterBuilder addMaterial(InsightMaterial m) {
        assert (!built);

        materials.add(m);
        return this;
    }

    public QueryParameterBuilder setArea(Location loc, int radius) {
        assert (!built);
        assert (!locationSet);

        if (worlds.size() > 1 || (worlds.size() == 1 && !loc.getWorld().getName().equals(worlds.iterator().next()))) {
            throw new IllegalStateException("You may not specify both the world and location in another world");
        } else if (loc != null) {
            worlds.add(loc.getWorld().getName());
        }

        this.radius = radius;
        point = loc;

        if (loc != null) {
            minX = loc.getBlockX() - radius;
            maxX = loc.getBlockX() + radius;
            minY = loc.getBlockY() - radius;
            maxY = loc.getBlockY() + radius;
            minZ = loc.getBlockZ() - radius;
            maxZ = loc.getBlockZ() + radius;
        }

        locationSet = true;
        return this;
    }

    public QueryParameterBuilder setArea(String world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        assert (!built);
        assert (!locationSet);
        assert (worlds.isEmpty());

        assert (maxX > minX);
        assert (maxY > minY);
        assert (maxZ > minZ);

        radius = QueryParameters.WORLDEDIT;
        point = null;

        addWorld(world);
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;

        locationSet = true;
        return this;
    }

    public QueryParameterBuilder setAfter(Date date) {
        assert (!built);

        if (date != null && before != null) {
            assert (date.before(before));
        }

        after = date;
        return this;
    }

    public QueryParameterBuilder setBefore(Date date) {
        assert (!built);

        if (date != null && after != null) {
            assert (date.after(after));
        }

        before = date;
        return this;
    }
}
