package tv.quaint.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Final
    @Shadow
    private PlayerInventory inventory;

    private PlayerEntity getPlayer() {
        return inventory.player;
    }

    @Inject(method = "onKilledOther", at = @At("HEAD"))
    private void onKilled(ServerWorld world, LivingEntity other, CallbackInfo ci) {
        if (other.getPrimeAdversary() instanceof PlayerEntity player) {
            KillEvents.KILL_ENTITY_EVENT.invoker().onKill(player, other);
        }
    }

//    @Inject(method = "eatFood", at = @At("HEAD"))
//    private void onEat(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
//        ConsumeEvents.PLAYER_CONSUME_ITEM_EVENT.invoker().onConsume(getPlayer(), stack);
//    }
}
