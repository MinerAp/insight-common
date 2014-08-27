package com.amshulman.insight.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.amshulman.insight.action.BlockAction;
import com.amshulman.insight.action.EntityAction;
import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.action.impl.AbstractAction.RollbackAction;
import com.amshulman.insight.action.impl.BlockActionImpl;
import com.amshulman.insight.action.impl.EntityActionImpl;
import com.amshulman.insight.action.impl.ItemActionImpl;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class EventCompat {

    private static Multimap<String, InsightAction> actionAliases = HashMultimap.create();

    /* Block Actions */
    public static final BlockAction BLOCK_PLACE = createBlockAction("BLOCK_PLACE", "placed", RollbackAction.BLOCK_REMOVE);
    public static final BlockAction BUCKET_PLACE = createBlockAction("BUCKET_PLACE", "placed", RollbackAction.BLOCK_REMOVE);

    public static final BlockAction BLOCK_BREAK = createBlockAction("BLOCK_BREAK", "broke", RollbackAction.BLOCK_PLACE);
    public static final BlockAction BUCKET_REMOVE = createBlockAction("BUCKET_REMOVE", "removed", RollbackAction.BLOCK_PLACE);
    public static final BlockAction BLOCK_BURN = createBlockAction("BLOCK_BURN", "burned", RollbackAction.BLOCK_PLACE);
    public static final BlockAction BLOCK_EXPLODE = createBlockAction("BLOCK_EXPLODE", "blew up", RollbackAction.BLOCK_PLACE);

    public static final BlockAction BLOCK_MELT = createBlockAction("BLOCK_MELT", "melted", RollbackAction.BLOCK_PLACE);
    public static final BlockAction BLOCK_FORM = createBlockAction("BLOCK_FORM", "formed", RollbackAction.BLOCK_REMOVE);
    public static final BlockAction BLOCK_GROW = createBlockAction("BLOCK_GROW", "grew", null);
    public static final BlockAction BLOCK_DIE = createBlockAction("BLOCK_DIE", "killed", RollbackAction.BLOCK_PLACE);
    public static final BlockAction BLOCK_DROP = createBlockAction("BLOCK_DROP", "dropped", null);
    public static final BlockAction ENTITY_EAT = createBlockAction("ENTITY_EAT", "ate", RollbackAction.BLOCK_PLACE);

    public static final BlockAction BLOCK_IGNITE = createBlockAction("BLOCK_IGNITE", "created", null);
    public static final BlockAction FIRE_SPREAD = createBlockAction("FIRE_SPREAD", "spread", null);

    public static final BlockAction BLOCK_FLOW = createBlockAction("BLOCK_FLOW", "flowed into", null);
    public static final BlockAction BLOCK_TELEPORT = createBlockAction("BLOCK_TELEPORT", "teleported", null);

    /* Entity Actions */
    public static final EntityAction ENTITY_DEATH = createEntityAction("ENTITY_DEATH", "died", null);
    public static final EntityAction ENTITY_KILL = createEntityAction("ENTITY_KILL", "killed", null);

    public static final EntityAction HANGING_PLACE = createEntityAction("HANGING_PLACE", "placed", null);
    public static final EntityAction HANGING_BREAK = createEntityAction("HANGING_BREAK", "broke", null);

    public static final EntityAction EXP_GAIN = createEntityAction("EXP_GAIN", "picked up", RollbackAction.NOTHING);

    public static final EntityAction VEHICLE_ENTER = createEntityAction("VEHICLE_ENTER", "entered", RollbackAction.NOTHING);
    public static final EntityAction VEHICLE_EXIT = createEntityAction("VEHICLE_EXIT", "exited", RollbackAction.NOTHING);

    /* Item Actions */
    public static final ItemAction ITEM_INSERT = createItemAction("ITEM_INSERT", "inserted", RollbackAction.CONTAINER_WITHDRAW);
    public static final ItemAction CRAFTING_INSERT = createItemAction("CRAFT_INSERT", "inserted", RollbackAction.NOTHING);
    public static final ItemAction ENDERCHEST_INSERT = createItemAction("EC_INSERT", "inserted", RollbackAction.CONTAINER_WITHDRAW);

    public static final ItemAction ITEM_REMOVE = createItemAction("ITEM_REMOVE", "removed", RollbackAction.CONTAINER_INSERT);
    public static final ItemAction CRAFTING_REMOVE = createItemAction("CRAFT_REMOVE", "removed", RollbackAction.NOTHING);
    public static final ItemAction ENDERCHEST_REMOVE = createItemAction("EC_REMOVE", "removed", RollbackAction.CONTAINER_INSERT);

    public static final ItemAction ITEM_DROP = createItemAction("ITEM_DROP", "dropped", RollbackAction.NOTHING);
    public static final ItemAction ITEM_PICKUP = createItemAction("ITEM_PICKUP", "picked up", RollbackAction.NOTHING);
    public static final ItemAction ITEM_BURN = createItemAction("ITEM_BURN", "burned", RollbackAction.NOTHING);

    public static final ItemAction ITEM_ROTATE = createItemAction("ITEM_ROTATE", "rotated", RollbackAction.NOTHING);

    /* Intransitive Actions */
    //

    static {
        add("PLACE", BLOCK_PLACE, BUCKET_PLACE, HANGING_PLACE);
        add("BREAK", BLOCK_BREAK, BUCKET_REMOVE, BLOCK_BURN, BLOCK_EXPLODE, HANGING_BREAK);
        add("CHANGE", BLOCK_MELT, BLOCK_FORM, BLOCK_GROW, BLOCK_DIE, BLOCK_DROP, ENTITY_EAT);
        add("SPREAD", FIRE_SPREAD, BLOCK_IGNITE);

        add("INSERT", ITEM_INSERT);
        add("REMOVE", ITEM_REMOVE);
        add("DROP", ITEM_DROP);
        add("PICKUP", ITEM_PICKUP);
    }

    public static Collection<InsightAction> getQueryActions(String actionName) {
        Collection<InsightAction> actions = actionAliases.get(actionName.toUpperCase());
        if (actions == null) {
            return null;
        }

        return Collections.unmodifiableCollection(actions);
    }

    public static InsightAction getActionByName(final String actionName) {
        Collection<InsightAction> actions = actionAliases.get(actionName.toUpperCase());
        if (actions == null) {
            return new UnknownAction();
        }

        return Iterables.find(actions, new Predicate<InsightAction>() {

            @Override
            public boolean apply(InsightAction action) {
                return action.getName().equalsIgnoreCase(actionName);
            }
        }, null);
    }

    private static BlockAction createBlockAction(String name, String friendlyDescription, RollbackAction rollbackAction) {
        BlockActionImpl action = new BlockActionImpl(name, friendlyDescription, rollbackAction);
        add(action);
        return action;
    }

    private static EntityAction createEntityAction(String name, String friendlyDescription, RollbackAction rollbackAction) {
        EntityActionImpl action = new EntityActionImpl(name, friendlyDescription, rollbackAction);
        add(action);
        return action;
    }

    private static ItemAction createItemAction(String name, String friendlyDescription, RollbackAction rollbackAction) {
        ItemActionImpl action = new ItemActionImpl(name, friendlyDescription, rollbackAction);
        add(action);
        return action;
    }

    private static void add(String alias, InsightAction... actions) {
        actionAliases.putAll(alias, Arrays.asList(actions));
    }

    private static void add(InsightAction action) {
        add(action.getName(), action);
    }

    private static class UnknownAction implements InsightAction {

        @Override
        public String getName() {
            return "UNKNOWN_ACTION";
        }

        @Override
        public String getFriendlyDescription() {
            return "did something to";
        }
    }
}
