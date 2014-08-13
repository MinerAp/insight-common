package com.amshulman.insight.action.impl;

import javax.annotation.Nonnull;

import com.amshulman.insight.action.EntityAction;

public class EntityActionImpl extends AbstractAction implements EntityAction {

    public EntityActionImpl(@Nonnull String name, @Nonnull String friendlyDescription, RollbackAction rollbackAction) {
        super(name, friendlyDescription, rollbackAction);
    }
}
