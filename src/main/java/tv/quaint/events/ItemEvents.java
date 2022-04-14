package tv.quaint.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ItemEvents {
    public interface PlayerConsumeItem {
        void onConsume(ServerPlayerEntity self, ItemStack stack, CallbackInfo ci);
    }

    public static Event<ItemEvents.PlayerConsumeItem> PLAYER_CONSUME_ITEM_EVENT = EventFactory.createArrayBacked(ItemEvents.PlayerConsumeItem.class, (callbacks) -> (self, by, ci) -> {
        for (ItemEvents.PlayerConsumeItem callback : callbacks) {
            callback.onConsume(self, by, ci);
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

    public interface InteractWithBlockEvent {
        void onHoldForOneSecond(ServerPlayerEntity self, ItemStack stack, Block block);
    }

    public static Event<ItemEvents.InteractWithBlockEvent> PLAYER_INTERACT_WITH_BLOCK_EVENT = EventFactory.createArrayBacked(ItemEvents.InteractWithBlockEvent.class, (callbacks) -> (self, stack, block) -> {
        for (ItemEvents.InteractWithBlockEvent callback : callbacks) {
            callback.onHoldForOneSecond(self, stack, block);
        }
    });
}
