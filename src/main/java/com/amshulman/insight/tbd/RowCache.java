package com.amshulman.insight.tbd;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import com.amshulman.insight.row.RowEntry;

/**
 * Batches rows to allow more efficient bulk database writes. The methods in this class are not thread-safe unless explicitly marked.
 */
@EqualsAndHashCode(of = { "cacheId" })
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class RowCache implements Iterable<RowEntry> {

    static Set<RowCache> dirtyCaches = Collections.newSetFromMap(new ConcurrentHashMap<RowCache, Boolean>());
    @NonFinal static int cacheCounter = 0;

    @Getter int cacheId;
    int size;
    RowEntry[] cache;

    @NonFinal int counter = 0;

    public RowCache(@Nonnegative int size) {
        this.size = size;
        cache = new RowEntry[size];

        this.cacheId = cacheCounter;
        ++cacheCounter;

        dirtyCaches.add(this);
    }

    /**
     * Adds a row to this cache
     *
     * @param e
     *            The row to add
     */
    public void add(@Nonnull RowEntry e) {
        cache[counter] = e;
        ++counter;
    }

    /**
     * Does this cache have any dirty data?
     *
     * @return true if this cache has any dirty data
     */
    public boolean isDirty() {
        return counter != 0;
    }

    /**
     * Gets the number of rows in this cache
     *
     * @return The number of rows in this cache.
     */
    public int getSize() {
        return counter;
    }

    /**
     * Checks if this cache can hold additional rows.
     *
     * @return true if this cache is full
     */
    public boolean isFull() {
        return counter == size;
    }

    /**
     * Get all caches which still have dirty data. This method is thread-safe.
     *
     * @return A snapshot of which caches were dirty at the time this method was called.
     */
    public static Set<RowCache> getDirtyCaches() {
        return new HashSet<RowCache>(dirtyCaches);
    }

    /**
     * Mark this cache as clean.
     */
    public void markClean() {
        dirtyCaches.remove(this);
    }

    @Override
    public Iterator<RowEntry> iterator() {
        return new Iterator<RowEntry>() {

            private int index = -1;

            @Override
            public boolean hasNext() {
                return index + 1 < counter;
            }

            @Override
            public RowEntry next() throws ArrayIndexOutOfBoundsException {
                return cache[++index];
            }

            @Override
            public void remove() throws ArrayIndexOutOfBoundsException {
                cache[index] = null;
            }
        };
    }
}
