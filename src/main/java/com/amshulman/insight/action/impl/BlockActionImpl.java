package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.BlockAction;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public class BlockActionImpl extends BlockAction {

    String name;
    String friendlyDescription;
    BlockRollbackAction rollbackAction;

    public static final BlockRollbackAction PLACE = null;
    public static final BlockRollbackAction REMOVE = null;
    public static final BlockRollbackAction NOTHING = null;
}
