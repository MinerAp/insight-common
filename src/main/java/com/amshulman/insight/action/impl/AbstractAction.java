package com.amshulman.insight.action.impl;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;

import com.amshulman.insight.action.InsightAction;

@Value
@NonFinal
@EqualsAndHashCode(of = { "name" })
public abstract class AbstractAction implements InsightAction {

    String name;
    String friendlyDescription;
    RollbackAction<?> rollbackAction;
}
