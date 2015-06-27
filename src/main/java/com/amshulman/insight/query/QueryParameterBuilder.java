package com.amshulman.insight.query;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.types.InsightLocation;
import com.amshulman.insight.types.InsightMaterial;
import com.google.common.base.CharMatcher;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class QueryParameterBuilder {

    final Set<String> worlds = new HashSet<String>(5);
    final Set<String> actors = new HashSet<String>(10);
    final Set<InsightAction> actions = new HashSet<InsightAction>(10);
    final Set<String> actees = new HashSet<String>(10);
    final Set<InsightMaterial> materials = new HashSet<InsightMaterial>(10);

    boolean reverseOrder = false;
    boolean invertActors = false;
    boolean invertActions = false;
    boolean invertActees = false;
    boolean invertMaterials = false;
    boolean locationSet = false;

    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;
    int minZ = 0;
    int maxZ = 0;
    int radius = 0;
    InsightLocation point = null;

    Date after;
    Date before;

    boolean built = false;

    public QueryParameters build() {
        assert (!built);

        built = true;
        return new QueryParameters(worlds, actors, actions, actees, materials, reverseOrder, invertActors, invertActions, invertActees, invertMaterials, locationSet, minX, maxX, minY, maxY, minZ, maxZ, radius, point, after, before);
    }

    public QueryParameterBuilder reverseOrder(boolean reversed) {
        assert (!built);

        reverseOrder = reversed;
        return this;
    }

    public QueryParameterBuilder addWorld(String world) {
        assert (!built);

        if (!CharMatcher.JAVA_LETTER_OR_DIGIT.or(CharMatcher.anyOf("_-")).matchesAllOf(world)) {
            throw new IllegalArgumentException(world + " contains unacceptable special characters");
        }

        if (point != null && !point.getWorld().equals(world)) {
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

    public QueryParameterBuilder setArea(InsightLocation loc, int radius) {
        assert (!built);
        assert (!locationSet);

        if (loc != null) {
            worlds.add(loc.getWorld());
        }

        if (worlds.size() > 1) {
            throw new IllegalStateException("You may not query a specific area in multiple worlds");
        }

        this.radius = radius;
        point = loc;

        if (loc != null) {
            minX = loc.getX() - radius;
            maxX = loc.getX() + radius;
            minY = loc.getY() - radius;
            maxY = loc.getY() + radius;
            minZ = loc.getZ() - radius;
            maxZ = loc.getZ() + radius;
        }

        locationSet = true;
        return this;
    }

    public QueryParameterBuilder setArea(String world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ) {
        assert (!built);
        assert (!locationSet);
        assert (worlds.isEmpty());

        assert (maxX >= minX);
        assert (maxY >= minY);
        assert (maxZ >= minZ);

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
