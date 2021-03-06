package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.BlockAction;
import com.amshulman.insight.results.InsightRecord;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public final class BlockActionImpl extends BlockAction {

    String name;
    String friendlyDescription;
    BlockRollbackAction rollbackAction;

    /**
     * Place the block described in the action
     */
    public static final BlockRollbackAction PLACE = new BlockRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<BlockAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };

    /**
     * Remove the block described in the action
     */
    public static final BlockRollbackAction REMOVE = new BlockRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<BlockAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };

    /**
     * Do nothing
     */
    public static final BlockRollbackAction NOTHING = new BlockRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<BlockAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };
}
