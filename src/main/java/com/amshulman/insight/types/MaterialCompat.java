package com.amshulman.insight.types;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.javatuples.Pair;

public final class MaterialCompat {

    public static InsightMaterial getInsightMaterial(@Nonnull BlockState b) {
        return getInsightMaterial(b.getType(), b.getRawData());
    }

    public static InsightMaterial getInsightMaterial(@Nonnull Material mat, @Nonnegative short subtype) {
        return new InsightMaterial("minecraft", mat.name(), subtype);
    }

    public static InsightMaterial getWildcardMaterial(String materialName) {
        Material mat = Material.getMaterial(materialName);
        if (mat == null) {
            return null;
        }

        return new InsightMaterial("minecraft", mat.name(), InsightMaterial.UNSPECIFIED_SUBTYPE);
    }

    public static Pair<Material, Short> getBukkitMaterial(String materialName, short subType) {
        return new Pair<Material, Short>(Material.getMaterial(materialName), subType);
    }

    public static String getFriendlyName(InsightMaterial mat) {
        return mat.getName().toLowerCase().replace('_', ' ');
    }
}
