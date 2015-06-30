package com.amshulman.insight.action.impl;

import java.util.Iterator;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.results.InsightRecord;
import com.amshulman.insight.serialization.ItemMetadata;
import com.amshulman.insight.types.InsightLocation;
import com.amshulman.insight.types.MaterialCompat;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public final class ItemActionImpl extends ItemAction {

    String name;
    String friendlyDescription;
    ItemRollbackAction rollbackAction;

    private static final Block getBlock(InsightLocation loc) {
        return Bukkit.getWorld(loc.getWorld()).getBlockAt(loc.getX(), loc.getY(), loc.getZ());
    }

    private static final ItemStack getItem(InsightRecord<ItemAction> record) {
        Material mat = MaterialCompat.getBukkitMaterial(record.getMaterial().getName(), record.getMaterial().getSubtype()).getValue0();
        ItemMetadata metadata = (ItemMetadata) record.getMetadata();
        if (metadata == null) {
            return new ItemStack(mat);
        }

        ItemStack item = new ItemStack(mat, metadata.getQuantity(), metadata.getDamage());
        item.setItemMeta(metadata.getMeta());
        return item;
    }

    /**
     * Insert the item described in the action
     */
    public static final ItemRollbackAction INSERT = new ItemRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<ItemAction> record, boolean force) {
            BlockState state = getBlock(record.getLocation()).getState();
            if (!(state instanceof Chest)) {
                return RollbackActionStatus.SKIPPED;
            }

            boolean success = ((Chest) state).getInventory().addItem(getItem(record)).isEmpty();
            return success ? RollbackActionStatus.OK : RollbackActionStatus.FAILED;
        }
    };

    /**
     * Withdraw the item described in the action
     */
    public static final ItemRollbackAction WITHDRAW = new ItemRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<ItemAction> record, boolean force) {
            BlockState state = getBlock(record.getLocation()).getState();
            if (!(state instanceof Chest)) {
                return RollbackActionStatus.SKIPPED;
            }

            ItemStack toRemove = getItem(record);
            int amount = toRemove.getAmount();
            for (Iterator<ItemStack> iter = ((Chest) state).getInventory().iterator(); iter.hasNext();) {
                ItemStack item = iter.next();
                if (toRemove.isSimilar(item)) {
                    if (item.getAmount() > amount) {
                        item.setAmount(item.getAmount() - amount);
                        return RollbackActionStatus.OK;
                    } else {
                        amount -= item.getAmount();
                        iter.remove();
                        if (amount == 0) {
                            return RollbackActionStatus.OK;
                        }
                    }
                }
            }

            return RollbackActionStatus.FAILED;
        }
    };

    /**
     * Do nothing
     */
    public static final ItemRollbackAction NOTHING = new ItemRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<ItemAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };
}
