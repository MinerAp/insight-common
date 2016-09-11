package com.amshulman.insight.serialization;

import lombok.Value;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

@Value
public final class SkullMeta implements MetadataEntry {

    private static final long serialVersionUID = -3025864226932077542L;

    BlockFace rotation;
    SkullType type;
    UUID uuid;
    String name;

    public SkullMeta(Skull skull) {
        rotation = skull.getRotation();
        type = skull.getSkullType();

        if (skull.hasOwner()) {
            uuid = skull.getOwningPlayer().getUniqueId();
            name = skull.getOwningPlayer().getName();
        } else {
            uuid = null;
            name = null;
        }
    }

    public ItemStack getItemStack() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) type.ordinal());
        if (SkullType.PLAYER.equals(type)) {
            org.bukkit.inventory.meta.SkullMeta skullMeta = (org.bukkit.inventory.meta.SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(name);
            skull.setItemMeta(skullMeta);
        }

        return skull;
    }
}
