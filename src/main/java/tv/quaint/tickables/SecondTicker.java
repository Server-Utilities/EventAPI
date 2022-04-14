package tv.quaint.tickables;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import tv.quaint.events.ItemEvents;
import tv.quaint.utils.MainUtils;

public class SecondTicker {
    public int countdown;
    public int reset;

    public SecondTicker(int reset) {
        this.countdown = 0;
        this.reset = reset;
    }

    public void tick() {
        if (countdown <= 0) {
            countdown = reset;

            done();
        }

        countdown --;
    }

    public void done() {
        for (ServerPlayerEntity player : MainUtils.getOnlinePlayers()) {
            ItemStack main = player.getMainHandStack();
            ItemStack off = player.getOffHandStack();

            ItemEvents.PLAYER_HOLD_ITEM_FOR_ONE_SECOND_EVENT.invoker().onHoldForOneSecond(player, main, off);
        }
    }
}
