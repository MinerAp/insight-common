package com.amshulman.insight.serialization;

import lombok.Value;

import org.bukkit.Material;

@Value
public class Block implements MetadataEntry {

    private static final long serialVersionUID = -6457551706614140054L;

    Material mat;
    byte damage;
}
