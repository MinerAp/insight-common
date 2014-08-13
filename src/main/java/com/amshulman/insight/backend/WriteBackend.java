package com.amshulman.insight.backend;

import java.util.UUID;

import com.amshulman.insight.row.RowEntry;

public interface WriteBackend extends AutoCloseable {

    public abstract void registerPlayer(String playerName, UUID uuid);

    public abstract void submit(RowEntry data);

    public abstract void suggestFlush();

    public abstract void close();
}
