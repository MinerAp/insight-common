package com.amshulman.insight.results;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.amshulman.insight.action.BlockAction;
import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.query.QueryParameters;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InsightResultSet implements Iterable<InsightRecord> {

    private InsightRecord previous = null;
    private final List<InsightRecord> records;
    @Getter private final QueryParameters queryParameters;

    protected InsightResultSet(QueryParameters params) {
        records = new ArrayList<InsightRecord>();
        queryParameters = params;
    }

    protected final void add(InsightRecord r) {
        if (previous != null && areSimultaneous(previous, r) && (areDuplicateItemActions(previous, r) || areDuplicateBlockActions(previous, r))) {
            previous = null;
        } else {
            records.add(r);
            previous = r;
        }
    }

    protected final void doneAdding() {
        previous = null;
    }

    public InsightRecord getRecord(int index) {
        return records.get(index);
    }

    public abstract InsightResultSet getResultSubset(int fromIndex, int toIndex);

    protected final List<InsightRecord> getSubList(int fromIndex, int toIndex) {
        return records.subList(fromIndex, toIndex);
    }

    public int getSize() {
        return records.size();
    }

    public Iterator<InsightRecord> iterator() {
        return records.iterator();
    }

    private static boolean areDuplicateItemActions(InsightRecord first, InsightRecord second) {
        if (!(first.getAction() instanceof ItemAction)) {
            return false;
        }

        if (!first.getMaterial().equals(second.getMaterial())) {
            return false;
        }

        if (first.getMetadata() != second.getMetadata() && (first.getMetadata() == null || !first.getMetadata().equals(second.getMetadata()))) {
            return false;
        }

        return areAdjacent(first, second);
    }

    private static boolean areDuplicateBlockActions(InsightRecord first, InsightRecord second) {
        if (!(first.getAction() instanceof BlockAction)) {
            return false;
        }

        boolean placeholder = true;
        if (placeholder) {
            return false;
        }

        return areStacked(first, second);
    }

    private static boolean areSimultaneous(InsightRecord first, InsightRecord second) {
        if (!first.getDatetime().equals(second.getDatetime())) {
            return false;
        }

        if (!first.getActor().equals(second.getActor())) {
            return false;
        }

        if (!first.getAction().equals(second.getAction())) {
            return false;
        }

        return true;
    }

    private static boolean areAdjacent(InsightRecord first, InsightRecord second) {
        if (first.getY() != second.getY()) {
            return false;
        }

        if (first.getX() + 1 == second.getX() && first.getZ() == second.getZ()) {
            return true;
        }

        if (first.getX() - 1 == second.getX() && first.getZ() == second.getZ()) {
            return true;
        }

        if (first.getX() == second.getX() && first.getZ() + 1 == second.getZ()) {
            return true;
        }

        if (first.getX() == second.getX() && first.getZ() - 1 == second.getZ()) {
            return true;
        }

        return false;
    }

    private static boolean areStacked(InsightRecord first, InsightRecord second) {
        if (first.getX() != second.getX() || first.getZ() != second.getZ()) {
            return false;
        }

        if (first.getY() + 1 == second.getY()) {
            return true;
        }

        if (first.getY() - 1 == second.getY()) {
            return true;
        }

        return false;
    }
}
