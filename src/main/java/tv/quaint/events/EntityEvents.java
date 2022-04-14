package tv.quaint.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityEvents {
    public interface PlayerDeathByEntity {
        void onDeath(PlayerEntity self, Entity by);
    }

    public static Event<PlayerDeathByEntity> PLAYER_DEATH_BY_ENTITY_EVENT = EventFactory.createArrayBacked(PlayerDeathByEntity.class, (callbacks) -> (self, by) -> {
        for (PlayerDeathByEntity callback : callbacks) {
            callback.onDeath(self, by);
        }
    });

    public interface KillEntity {
        void onKill(PlayerEntity player, LivingEntity entity);
    }

    public static Event<KillEntity> KILL_ENTITY_EVENT = EventFactory.createArrayBacked(KillEntity.class, (callbacks) -> (killer, entity) -> {
        for (KillEntity callback : callbacks) {
            callback.onKill(killer, entity);
        }
    });
}
