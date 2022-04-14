package tv.quaint.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class ItemEvents {
    public interface PlayerConsumeItem {
        void onConsume(ServerPlayerEntity self, ItemStack stack);
    }

    public static Event<ItemEvents.PlayerConsumeItem> PLAYER_CONSUME_ITEM_EVENT = EventFactory.createArrayBacked(ItemEvents.PlayerConsumeItem.class, (callbacks) -> (self, by) -> {
        for (ItemEvents.PlayerConsumeItem callback : callbacks) {
            callback.onConsume(self, by);
        }
    });

    public interface PlayerHoldItemForOneSecond {
        void onHoldForOneSecond(ServerPlayerEntity self, ItemStack mainHand, ItemStack offHand);
    }

    public static Event<ItemEvents.PlayerHoldItemForOneSecond> PLAYER_HOLD_ITEM_FOR_ONE_SECOND_EVENT = EventFactory.createArrayBacked(ItemEvents.PlayerHoldItemForOneSecond.class, (callbacks) -> (self, mainHand, offHand) -> {
        for (ItemEvents.PlayerHoldItemForOneSecond callback : callbacks) {
            callback.onHoldForOneSecond(self, mainHand, offHand);
        }
    });
}
