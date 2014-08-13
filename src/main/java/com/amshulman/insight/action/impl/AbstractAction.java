package com.amshulman.insight.action.impl;

import com.amshulman.insight.action.InsightAction;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
@EqualsAndHashCode(of = { "name" })
public abstract class AbstractAction implements InsightAction {

    String name;
    String friendlyDescription;
    RollbackAction rollbackAction;

    public static abstract class RollbackAction implements Runnable {
        public static final RollbackAction NOTHING = null;
        public static final RollbackAction BLOCK_PLACE = null;
        public static final RollbackAction BLOCK_REMOVE = null;
        public static final RollbackAction CONTAINER_INSERT = null;
        public static final RollbackAction CONTAINER_WITHDRAW = null;

        public abstract void run();
    }
}
