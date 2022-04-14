package tv.quaint.mixin;

import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tv.quaint.events.ItemEvents;

@Mixin(ConsumeItemCriterion.class)
public class ConsumeItemCriterionMixin {
    @Inject(method = "trigger", at = @At("HEAD"))
    private void onTrigger(ServerPlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ItemEvents.PLAYER_CONSUME_ITEM_EVENT.invoker().onConsume(player, stack);
    }
}
