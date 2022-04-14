package tv.quaint.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

public class SelfEvents {
    public interface StorageSetEvent {
        void onSet(String identifier, PlayerEntity player, int previous, int now);
    }

    public static Event<SelfEvents.StorageSetEvent> STORAGE_SET_EVENT = EventFactory.createArrayBacked(SelfEvents.StorageSetEvent.class, (callbacks) -> (identifier, player, previous, now) -> {
        for (SelfEvents.StorageSetEvent callback : callbacks) {
            callback.onSet(identifier, player, previous, now);
        }
    });

    public interface SecondTickEvent {
        void onTick();
    }

    public static Event<SelfEvents.SecondTickEvent> SECOND_TICK_EVENT = EventFactory.createArrayBacked(SelfEvents.SecondTickEvent.class, (callbacks) -> () -> {
        for (SelfEvents.SecondTickEvent callback : callbacks) {
            callback.onTick();
        }
    });
}
