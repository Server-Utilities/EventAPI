package tv.quaint.objects.events;

import net.minecraft.entity.player.PlayerEntity;
import tv.quaint.objects.events.conditions.ConfigurableCondition;
import tv.quaint.objects.events.rewards.ConfigurableReward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigurableEvent {
    public String identifier;
    public EventType type;
    public String value;
    public boolean cancelReal;
    public List<ConfigurableCondition> conditions;
    public List<ConfigurableReward> rewards;

    public ConfigurableEvent(String identifier, EventType type, String value, boolean cancelReal) {
        this.identifier = identifier;
        this.type = type;
        this.cancelReal = cancelReal;
        this.conditions = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.value = value;
    }

    public ConfigurableEvent addConditions(ConfigurableCondition... conditions) {
        this.conditions.addAll(Arrays.asList(conditions));

        return this;
    }

    public ConfigurableEvent addRewards(ConfigurableReward... rewards) {
        this.rewards.addAll(Arrays.asList(rewards));

        return this;
    }

    public void rewardPlayer(PlayerEntity player) {
        for (ConfigurableReward reward : rewards) {
            try {
                reward.reward(player);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
