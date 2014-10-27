package com.amshulman.insight.action.impl;

import javax.annotation.Nonnull;

import com.amshulman.insight.action.BlockAction;

public class BlockActionImpl extends AbstractAction implements BlockAction {

    public BlockActionImpl(@Nonnull String name, @Nonnull String friendlyDescription, BlockRollbackAction rollbackAction) {
        super(name, friendlyDescription, rollbackAction);
    }

    public static final BlockRollbackAction PLACE = null;
    public static final BlockRollbackAction REMOVE = null;
    public static final BlockRollbackAction NOTHING = null;
}
