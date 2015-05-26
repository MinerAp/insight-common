package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.EntityAction;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public class EntityActionImpl extends EntityAction {

    String name;
    String friendlyDescription;
    EntityRollbackAction rollbackAction;

    public static final EntityRollbackAction NOTHING = null;
}
