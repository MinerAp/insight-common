package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.amshulman.insight.action.EntityAction;
import com.amshulman.insight.results.InsightRecord;

@Value
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "name" }, callSuper = false)
public class EntityActionImpl extends EntityAction {

    String name;
    String friendlyDescription;
    EntityRollbackAction rollbackAction;

    public static final EntityRollbackAction NOTHING = new EntityRollbackAction() {

        @Override
        public RollbackActionStatus rollback(InsightRecord<EntityAction> record, boolean force) {
            return RollbackActionStatus.SKIPPED;
        }
    };
}
