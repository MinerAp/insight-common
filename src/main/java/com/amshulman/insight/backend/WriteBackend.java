package com.amshulman.insight.backend;

import java.util.UUID;

import com.amshulman.insight.row.RowEntry;

public interface WriteBackend extends AutoCloseable {

    /**
     * Add a player with the specified name and UUID to the database. This should be called before writing any actions for this player.
     *
     * @param playerName
     *            The player's name
     * @param uuid
     *            The player's UUID
     */
    public void registerPlayer(String playerName, UUID uuid);

    /**
     * Add a world with the specified name to the database. This should be called before writing any actions for this world.
     *
     * @param worldName
     *            The name of the world
     */
    public void registerWorld(String worldName);

    /**
     * Schedule this data to be written to the backing store at a future time.
     *
     * @param data
     *            The information to log
     */
    public void submit(RowEntry data);

    /**
     * Suggest that the database flush its row cache to ensure capacity for multiple rows. This is only a hint and may be ignored.
     */
    public void suggestFlush();

    @Override
    public void close();
}
