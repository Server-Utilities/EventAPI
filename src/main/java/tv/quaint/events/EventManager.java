package tv.quaint.events;

import tv.quaint.EventAPI;
import tv.quaint.objects.events.EventHandler;
import tv.quaint.utils.ItemUtils;

public class EventManager {
    public static void registerEvents() {
        EntityEvents.KILL_ENTITY_EVENT.register(((player, entity) -> {
            String entityName = entity.getType().getUntranslatedName();

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainKillMob(entityName));
        }));
        EntityEvents.PLAYER_DEATH_BY_ENTITY_EVENT.register((player, entity) -> {
            String entityName = entity.getType().getUntranslatedName();

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainDeathByMob(entityName));
        });
        ItemEvents.PLAYER_CONSUME_ITEM_EVENT.register((player, itemStack) -> {
            String itemType = ItemUtils.getType(itemStack);

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainConsumeType(itemType));
        });
        ItemEvents.PLAYER_HOLD_ITEM_FOR_ONE_SECOND_EVENT.register((player, mainHand, offHand) -> {
            String mainType = ItemUtils.getType(mainHand);
            String offType = ItemUtils.getType(offHand);

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainHoldType(mainType, true));
            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainHoldType(offType, false));
        });
    }
}
