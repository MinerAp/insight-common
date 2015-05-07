package com.amshulman.insight.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.amshulman.insight.action.BlockAction;
import com.amshulman.insight.action.BlockAction.BlockRollbackAction;
import com.amshulman.insight.action.EntityAction;
import com.amshulman.insight.action.EntityAction.EntityRollbackAction;
import com.amshulman.insight.action.InsightAction;
import com.amshulman.insight.action.ItemAction;
import com.amshulman.insight.action.ItemAction.ItemRollbackAction;
import com.amshulman.insight.action.impl.BlockActionImpl;
import com.amshulman.insight.action.impl.EntityActionImpl;
import com.amshulman.insight.action.impl.ItemActionImpl;
import com.amshulman.insight.results.InsightRecord;
import com.google.common.base.Predicate;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class EventCompat {

    private static Multimap<String, InsightAction> actionAliases = HashMultimap.create();

    /* Block Actions */
    public static final BlockAction BLOCK_PLACE = createBlockAction("BLOCK_PLACE", "placed", BlockActionImpl.REMOVE);
    public static final BlockAction BUCKET_PLACE = createBlockAction("BUCKET_PLACE", "placed", BlockActionImpl.REMOVE);
    public static final BlockAction ENDERMAN_PLACE = createBlockAction("ENDERMAN_PLACE", "placed", BlockActionImpl.REMOVE);

    public static final BlockAction BLOCK_BREAK = createBlockAction("BLOCK_BREAK", "broke", BlockActionImpl.PLACE);
    public static final BlockAction BUCKET_REMOVE = createBlockAction("BUCKET_REMOVE", "removed", BlockActionImpl.PLACE);
    public static final BlockAction BLOCK_BURN = createBlockAction("BLOCK_BURN", "burned", BlockActionImpl.PLACE);
    public static final BlockAction BLOCK_EXPLODE = createBlockAction("BLOCK_EXPLODE", "blew up", BlockActionImpl.PLACE);
    public static final BlockAction ENDERMAN_REMOVE = createBlockAction("ENDERMAN_REMOVE", "removed", BlockActionImpl.PLACE);

    public static final BlockAction BLOCK_MELT = createBlockAction("BLOCK_MELT", "melted", BlockActionImpl.PLACE);
    public static final BlockAction BLOCK_FORM = createBlockAction("BLOCK_FORM", "formed", BlockActionImpl.REMOVE);
    public static final BlockAction BLOCK_GROW = createBlockAction("BLOCK_GROW", "grew", BlockActionImpl.REMOVE);
    public static final BlockAction BLOCK_DIE = createBlockAction("BLOCK_DIE", "killed", BlockActionImpl.PLACE);
    public static final BlockAction BLOCK_DROP = createBlockAction("BLOCK_DROP", "dropped", null); // TODO
    public static final BlockAction SHEEP_EAT = createBlockAction("SHEEP_EAT", "ate", BlockActionImpl.PLACE);
    public static final BlockAction SOIL_TILL = createBlockAction("SOIL_TILL", "tilled", BlockActionImpl.PLACE);
    public static final BlockAction SOIL_TRAMPLE = createBlockAction("SOIL_TRAMPLE", "trampled", BlockActionImpl.PLACE);
    public static final BlockAction SOIL_REVERT = createBlockAction("SOIL_REVERT", "deteriorated", BlockActionImpl.PLACE);

    public static final BlockAction BLOCK_IGNITE = createBlockAction("BLOCK_IGNITE", "created", BlockActionImpl.REMOVE);
    public static final BlockAction FIRE_SPREAD = createBlockAction("FIRE_SPREAD", "spread", BlockActionImpl.REMOVE);

    public static final BlockAction BLOCK_FLOW = createBlockAction("BLOCK_FLOW", "flowed into", BlockActionImpl.PLACE); // Opposite of normal because water "removes" other blocks
    public static final BlockAction BLOCK_TELEPORT = createBlockAction("BLOCK_TELEPORT", "teleported", null); // TODO

    public static final BlockAction SIGN_CHANGE = createBlockAction("SIGN_CHANGE", "wrote", BlockActionImpl.NOTHING);

    /* Entity Actions */
    public static final EntityAction ENTITY_DEATH = createEntityAction("ENTITY_DEATH", "died", null); // TODO
    public static final EntityAction ENTITY_KILL = createEntityAction("ENTITY_KILL", "killed", null); // TODO

    public static final EntityAction HANGING_PLACE = createEntityAction("HANGING_PLACE", "placed", null); // TODO
    public static final EntityAction HANGING_BREAK = createEntityAction("HANGING_BREAK", "broke", null); // TODO

    public static final EntityAction EXP_GAIN = createEntityAction("EXP_GAIN", "picked up", EntityActionImpl.NOTHING);
    public static final EntityAction SHEEP_DYE = createEntityAction("SHEEP_DYE", "dyed", EntityActionImpl.NOTHING);
    public static final EntityAction SHEEP_SHEAR = createEntityAction("SHEEP_SHEAR", "sheared", EntityActionImpl.NOTHING);

    public static final EntityAction VEHICLE_ENTER = createEntityAction("VEHICLE_ENTER", "entered", EntityActionImpl.NOTHING);
    public static final EntityAction VEHICLE_EXIT = createEntityAction("VEHICLE_EXIT", "exited", EntityActionImpl.NOTHING);

    /* Item Actions */
    public static final ItemAction ITEM_INSERT = createItemAction("ITEM_INSERT", "inserted", ItemActionImpl.WITHDRAW);
    public static final ItemAction CRAFTING_INSERT = createItemAction("CRAFT_INSERT", "inserted", ItemActionImpl.NOTHING);
    public static final ItemAction ENDERCHEST_INSERT = createItemAction("EC_INSERT", "inserted", ItemActionImpl.NOTHING);

    public static final ItemAction ITEM_REMOVE = createItemAction("ITEM_REMOVE", "removed", ItemActionImpl.INSERT);
    public static final ItemAction CRAFTING_REMOVE = createItemAction("CRAFT_REMOVE", "removed", ItemActionImpl.NOTHING);
    public static final ItemAction ENDERCHEST_REMOVE = createItemAction("EC_REMOVE", "removed", ItemActionImpl.NOTHING);

    public static final ItemAction ITEM_DROP = createItemAction("ITEM_DROP", "dropped", ItemActionImpl.NOTHING);
    public static final ItemAction ITEM_PICKUP = createItemAction("ITEM_PICKUP", "picked up", ItemActionImpl.NOTHING);
    public static final ItemAction ITEM_BURN = createItemAction("ITEM_BURN", "burned", ItemActionImpl.NOTHING);

    public static final ItemAction ITEM_ROTATE = createItemAction("ITEM_ROTATE", "rotated", ItemActionImpl.NOTHING);

    /* Intransitive Actions */
    //

    static {
        add("PLACE", BLOCK_PLACE, BUCKET_PLACE, ENDERMAN_PLACE);
        add("BREAK", BLOCK_BREAK, BUCKET_REMOVE, BLOCK_BURN, BLOCK_EXPLODE, ENDERMAN_REMOVE);
        add("CHANGE", BLOCK_MELT, BLOCK_FORM, BLOCK_GROW, BLOCK_DIE, BLOCK_DROP, SHEEP_EAT);
        add("SPREAD", FIRE_SPREAD, BLOCK_IGNITE);

        add("INSERT", ITEM_INSERT);
        add("REMOVE", ITEM_REMOVE);
        add("DROP", ITEM_DROP);
        add("PICKUP", ITEM_PICKUP);
    }

    public static Collection<InsightAction> getQueryActions(String actionName) {
        return Collections.unmodifiableCollection(actionAliases.get(actionName.toUpperCase()));
    }

    public static InsightAction getActionByName(final String actionName) {
        Collection<InsightAction> actions = actionAliases.get(actionName.toUpperCase());
        if (actions.isEmpty()) {
            return new UnknownAction();
        }

        return Iterables.find(actions, new Predicate<InsightAction>() {

            @Override
            public boolean apply(InsightAction action) {
                return action.getName().equalsIgnoreCase(actionName);
            }
        }, null);
    }

    private static BlockAction createBlockAction(String name, String friendlyDescription, BlockRollbackAction rollbackAction) {
        BlockActionImpl action = new BlockActionImpl(name, friendlyDescription, rollbackAction);
        add(action);
        return action;
    }

    private static EntityAction createEntityAction(String name, String friendlyDescription, EntityRollbackAction rollbackAction) {
        EntityActionImpl action = new EntityActionImpl(name, friendlyDescription, rollbackAction);
        add(action);
        return action;
    }

    private static ItemAction createItemAction(String name, String friendlyDescription, ItemRollbackAction rollbackAction) {
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

        @Override
        public RollbackAction<InsightAction> getRollbackAction() {
            return new RollbackAction<InsightAction>() {

                @Override
                public boolean rollback(InsightRecord<InsightAction> rowEntry, boolean force) {
                    return false;
                }
            };
        }
    }
}
