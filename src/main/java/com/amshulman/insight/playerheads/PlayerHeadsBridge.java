package com.amshulman.insight.playerheads;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.ChatColor;
import org.shininet.bukkit.playerheads.CustomSkullType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerHeadsBridge {

    public static String getDisplayName(String owner) {
        CustomSkullType skullType = CustomSkullType.get(owner);
        if (skullType == null) {
            return null;
        }

        return ChatColor.RESET + skullType.getDisplayName();
    }
}
