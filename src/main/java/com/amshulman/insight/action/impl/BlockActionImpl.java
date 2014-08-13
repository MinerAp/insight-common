package com.amshulman.insight.action.impl;

import javax.annotation.Nonnull;

import com.amshulman.insight.action.BlockAction;

public class BlockActionImpl extends AbstractAction implements BlockAction {

    public BlockActionImpl(@Nonnull String name, @Nonnull String friendlyDescription, RollbackAction rollbackAction) {
        super(name, friendlyDescription, rollbackAction);
    }
}
