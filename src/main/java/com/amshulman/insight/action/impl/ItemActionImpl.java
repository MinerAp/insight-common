package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.ItemAction;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public class ItemActionImpl extends ItemAction {

    String name;
    String friendlyDescription;
    ItemRollbackAction rollbackAction;

    public static final ItemRollbackAction INSERT = null;
    public static final ItemRollbackAction WITHDRAW = null;
    public static final ItemRollbackAction NOTHING = null;
}
