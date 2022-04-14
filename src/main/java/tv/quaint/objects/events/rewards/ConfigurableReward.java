package tv.quaint.objects.events.rewards;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ConfigurableReward {
    public String identifier;
    public RewardType type;
    public String value;

    public ConfigurableReward(String identifier, RewardType type, String value) {
        this.identifier = identifier;
        this.type = type;
        this.value = value;
    }

    public void reward(PlayerEntity player) {
        RewardHandler.handleReward(this, player);
    }
}
