package com.amshulman.insight.serialization;

import lombok.Value;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.amshulman.insight.playerheads.PlayerHeadsBridge;

@Value
public class SkullMeta implements MetadataEntry {

    private static final long serialVersionUID = -3025864226932077542L;

    private transient static final boolean playerHeads;

    BlockFace rotation;
    SkullType type;
    String owner;
    String displayName;

    static {
        playerHeads = playerHeadsLoaded();
    }

    public SkullMeta(Skull skull) {
        rotation = skull.getRotation();
        type = skull.getSkullType();

        if (skull.hasOwner()) {
            owner = skull.getOwner();
            if (playerHeads) {
                displayName = PlayerHeadsBridge.getDisplayName(owner);
            } else {
                displayName = null;
            }
        } else {
            owner = null;
            displayName = null;
        }
    }

    public SkullMeta(Skull skull, ItemMeta itemMeta) {
        rotation = skull.getRotation();

        if (itemMeta instanceof org.bukkit.inventory.meta.SkullMeta) {
            org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) itemMeta;
            if (meta.hasOwner()) {
                type = SkullType.PLAYER;
                owner = meta.getOwner();
                displayName = meta.hasDisplayName() ? meta.getDisplayName() : null;
                return;
            }
        }

        type = skull.getSkullType();
        owner = skull.getOwner();
        displayName = null;
    }

    public ItemStack getItemStack() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) type.ordinal());
        if (SkullType.PLAYER.equals(type)) {
            org.bukkit.inventory.meta.SkullMeta skullMeta = (org.bukkit.inventory.meta.SkullMeta) skull.getItemMeta();
            skullMeta.setOwner(owner);
            if (displayName != null) {
                skullMeta.setDisplayName(displayName);
            }
            skull.setItemMeta(skullMeta);
        }

        return skull;
    }

    private static boolean playerHeadsLoaded() {
        try {
            Class.forName(PlayerHeadsBridge.class.getName());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
