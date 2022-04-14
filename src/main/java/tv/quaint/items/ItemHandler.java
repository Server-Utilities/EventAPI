package tv.quaint.items;

import net.minecraft.item.ItemStack;
import tv.quaint.EventAPI;
import tv.quaint.objects.events.rewards.ItemReward;
import tv.quaint.objects.events.rewards.RewardHandler;
import tv.quaint.utils.ItemUtils;

import java.util.List;

public class ItemHandler {
    public static ConfiguredItem getConfiguredItemByIdentifier(String identifier) {
        List<ConfiguredItem> items = EventAPI.CONFIG.getConfiguredItems();

        for (ConfiguredItem item : items) {
            if (item.identifier.equals(identifier)) return item;
        }

        return null;
    }

    public static ItemReward asReward(ConfiguredItem item) {
        return RewardHandler.parseItemValue(item.rawValue);
    }

    public static ItemStack asStack(ItemReward reward) {
        return reward.asStack();
    }

    public static ItemStack asStack(ConfiguredItem item) {
        return asStack(asReward(item));
    }

    public static ItemStack asStack(String identifier) {
        return asStack(getConfiguredItemByIdentifier(identifier));
    }

    public static boolean isSame(ItemStack toCheck, String identifier) {
        if (toCheck == null) return false;

        ItemStack original = asStack(identifier);
        if (original == null) return false;

        return toCheck.equals(original);
    }

    public static boolean isSameAnyAmount(ItemStack toCheck, String identifier) {
        if (toCheck == null) return false;

        ItemStack original = asStack(identifier);
        if (original == null) return false;

        ItemStack temp = toCheck;
        temp.setCount(original.getCount());

        return temp.equals(original);
    }

    public static boolean hasSameStringedTagValue(ItemStack toCheck, String identifier, String key) {
        if (toCheck == null) return false;
        String cValue = ItemUtils.getStringedTagValue(toCheck, key);

        String oValue = getStringedTagValue(identifier, key);
        if (oValue == null) return false;

        return cValue.equals(oValue);
    }

    public static String getStringedTagValue(String identifier, String key) {
        return ItemUtils.getStringedTagValue(asStack(identifier), key);
    }
}
