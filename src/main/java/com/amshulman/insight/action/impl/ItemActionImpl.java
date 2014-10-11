package com.amshulman.insight.action.impl;

import javax.annotation.Nonnull;

import com.amshulman.insight.action.ItemAction;

public class ItemActionImpl extends AbstractAction implements ItemAction {

    public ItemActionImpl(@Nonnull String name, @Nonnull String friendlyDescription, ItemRollbackAction rollbackAction) {
        super(name, friendlyDescription, rollbackAction);
    }

    public static final ItemRollbackAction INSERT = null;
    public static final ItemRollbackAction WITHDRAW = null;
    public static final ItemRollbackAction NOTHING = null;
}
