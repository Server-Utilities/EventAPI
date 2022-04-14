package tv.quaint.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.TreeMap;

public class ItemUtils {
    public static ItemStack newItem(Item material, int amount) {
        return new ItemStack(material, amount);
    }

    public static ItemStack setName(ItemStack stack, String name) {
        stack.setCustomName(TextUtils.newText(name));
        return stack;
    }

    public static ItemStack putStringedTag(ItemStack stack, String key, String value) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.putString(key, value);
        stack.setNbt(tag);

        return stack;
    }

    public static ItemStack putStringedTags(ItemStack stack, TreeMap<String, String> tags) {
        for (String key : tags.keySet()) {
            ItemUtils.putStringedTag(stack, key, tags.get(key));
        }

        return stack;
    }

    public static boolean hasTag(ItemStack stack, String key) {
        NbtCompound tag = stack.getOrCreateNbt();
        return tag.contains(key);
    }

    public static String getStringedTagValue(ItemStack stack, String key) {
        NbtCompound tag = stack.getOrCreateNbt();
        return tag.getString(key);
    }

    public static boolean hasStringedTagValue(ItemStack stack, String key, String value) {
        return getStringedTagValue(stack, key).equals(value);
    }

    public static String getType(ItemStack stack) {
        String[] split = stack.getTranslationKey().split(":", 2);
        if (split.length < 2) {
            return split[0];
        } else {
            return split[1];
        }
    }
}
