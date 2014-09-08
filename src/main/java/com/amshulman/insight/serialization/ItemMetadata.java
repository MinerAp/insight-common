package com.amshulman.insight.serialization;

import lombok.Value;

import org.bukkit.inventory.meta.ItemMeta;

@Value
public class ItemMetadata implements StorageMetadata {

    private static final long serialVersionUID = -94062966217112384L;

    ItemMeta meta;
    int quantity;
    short damage;
}
