package com.amshulman.insight.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import com.amshulman.insight.action.BlockAction;
import com.amshulman.insight.action.EntityAction;
import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.results.InsightRecord;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class EventRegistry {

    static Multimap<String, InsightAction> actionsByName = HashMultimap.create();

    /**
     * @param action
     *            the action to register
     * @throws NullPointerException
     *             if <code>action</code> is <code>null</code>
     */
    public static void addAction(InsightAction action) {
        checkIsValid(action);
        if (actionsByName.containsKey(action.getName().toUpperCase())) {
            throw new IllegalArgumentException("An action with name " + action.getName().toUpperCase() + " has already been registered.");
        }

        actionsByName.put(action.getName().toUpperCase(), action);
    }

    /**
     * @param actionName
     *            the name of the action to look up
     * @return the action, or an <code>UnknownAction</code>
     */
    public static InsightAction getActionByName(String actionName) {
        Collection<InsightAction> actions = actionsByName.get(actionName.toUpperCase());
        if (actions.size() == 0) {
            return new UnknownAction();
        } else if (actions.size() == 1) {
            return actions.iterator().next();
        }

        return Iterables.find(actions, new Predicate<InsightAction>() {

            @Override
            public boolean apply(InsightAction action) {
                return action.getName().equalsIgnoreCase(actionName);
            }
        }, null);
    }

    /**
     * @param alias
     *            the alias to add the actions to
     * @param actions
     *            actions to add
     * @throws NullPointerException
     *             if <code>alias</code> is <code>null</code> or any of the <code>actions</code> are <code>null</code>
     * @throws IllegalArgumentException
     *             if any <code>actions</code> are not a {@link BlockAction}, {@link EntityAction}, or {@link ItemAction}
     */
    public static void addActionsToAlias(String alias, InsightAction... actions) {
        for (InsightAction action : actions) {
            checkIsValid(action);
        }

        actionsByName.putAll(alias.toUpperCase(), Arrays.asList(actions));
    }

    /**
     * @param alias
     * @return
     */
    public static Collection<InsightAction> getActionsByAlias(String alias) {
        return Collections.unmodifiableCollection(actionsByName.get(alias.toUpperCase()));
    }

    private static void checkIsValid(InsightAction action) {
        if (action == null) {
            throw new NullPointerException("action may not be null");
        }

        if (!(action instanceof BlockAction) &&
                !(action instanceof EntityAction) &&
                !(action instanceof ItemAction)) {
            throw new IllegalArgumentException("action is not one of BlockAction, EntityAction, or ItemAction");
        }
    }

    private static class UnknownAction extends InsightAction {
        @Override
        public String getName() {
            return "UNKNOWN_ACTION";
        }

        @Override
        public String getFriendlyDescription() {
            return "did something to";
        }

        @Override
        public RollbackAction<InsightAction> getRollbackAction() {
            return new RollbackAction<InsightAction>() {

                @Override
                public RollbackActionStatus rollback(InsightRecord<InsightAction> rowEntry, boolean force) {
                    return RollbackActionStatus.NO_ACTION_AVAILABLE;
                }
            };
        }
    }
}
