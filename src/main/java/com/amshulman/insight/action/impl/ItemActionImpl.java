package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.results.InsightRecord;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public final class ItemActionImpl extends ItemAction {

    String name;
    String friendlyDescription;
    ItemRollbackAction rollbackAction;

    /**
     * Insert the item described in the action
     */
    public static final ItemRollbackAction INSERT = new ItemRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<ItemAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };

    /**
     * Withdraw the item described in the action
     */
    public static final ItemRollbackAction WITHDRAW = new ItemRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<ItemAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
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
