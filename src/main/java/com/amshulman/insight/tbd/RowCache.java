package com.amshulman.insight.tbd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import com.amshulman.insight.row.RowEntry;
import com.google.common.collect.Iterators;

@EqualsAndHashCode(of = { "cacheId" })
public final class RowCache implements Iterable<RowEntry> {

    private static Set<RowCache> dirtyCaches = Collections.newSetFromMap(new ConcurrentHashMap<RowCache, Boolean>());
    private static int cacheCounter = 0;

    @Getter private final int cacheId;
    private final int size;
    private final RowEntry[] cache;

    private int counter = 0;

    public RowCache(@Nonnegative int size) {
        this.size = size;
        cache = new RowEntry[size];

        this.cacheId = cacheCounter;
        ++cacheCounter;

        dirtyCaches.add(this);
    }

    public void add(@Nonnull RowEntry e) {
        cache[counter] = e;
        ++counter;
    }

    public boolean isDirty() {
        return counter != 0;
    }

    public int getSize() {
        return counter;
    }

    public boolean isFull() {
        return counter == size;
    }

    public static Set<RowCache> getDirtyCaches() {
        return new HashSet<RowCache>(dirtyCaches);
    }

    public void markClean() {
        dirtyCaches.remove(this);
    }

    @Override
    public Iterator<RowEntry> iterator() {
        return Iterators.forArray(cache);
    }
}
