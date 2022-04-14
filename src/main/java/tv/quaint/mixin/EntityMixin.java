package tv.quaint.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tv.quaint.events.EntityEvents;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "onKilledOther", at = @At("HEAD"))
    private void onKilled(ServerWorld world, LivingEntity other, CallbackInfo ci) {
        if (other instanceof PlayerEntity player) {
            EntityEvents.PLAYER_DEATH_BY_ENTITY_EVENT.invoker().onDeath(player, other.getPrimeAdversary());
        }
    }
}
