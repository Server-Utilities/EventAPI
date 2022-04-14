package tv.quaint.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.List;
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
            return split[0].substring(split[0].lastIndexOf(".") + 1);
        } else {
            return split[1].substring(split[1].lastIndexOf(".") + 1);
        }
    }

    public static boolean isSameAnyAmount(ItemStack toCheck, ItemStack original) {
        if (toCheck == null) return false;
        if (original == null) return false;

        ItemStack temp = toCheck;
        temp.setCount(original.getCount());

        return temp.equals(original);
    }

    public static HashMap<Integer, ItemStack> getInventory(PlayerEntity player) {
        HashMap<Integer, ItemStack> inventory = new HashMap<>();

        for (int i = 0; i < 27; i ++) {
            inventory.put(i, player.getInventory().getStack(i));
        }

        return inventory;
    }

    public static HashMap<Integer, ItemStack> getSimilarInInventory(PlayerEntity player, ItemStack stack) {
        HashMap<Integer, ItemStack> similar = new HashMap<>();
        HashMap<Integer, ItemStack> inventory = getInventory(player);

        for (int key : inventory.keySet()) {
            ItemStack s = inventory.get(key);

            if (isSameAnyAmount(s, stack)) {
                similar.put(key, s);
            }
        }

        return similar;
    }

    public static boolean hasEnoughToRemoveOf(PlayerEntity player, ItemStack stack, int amount) {
        HashMap<Integer, ItemStack> similar = getSimilarInInventory(player, stack);

        int has = 0;
        for (int key : similar.keySet()) {
            ItemStack s = similar.get(key);

            has += s.getCount();
        }

        return has >= amount;
    }
    
    public static boolean removeAmountOf(PlayerEntity player, ItemStack stack, int amount) {
        if (! hasEnoughToRemoveOf(player, stack, amount)) return false;

        HashMap<Integer, ItemStack> similar = getSimilarInInventory(player, stack);
        
        for (int key : similar.keySet()) {
            ItemStack s = similar.get(key);

            if (s.getCount() > amount) {
                while (amount > 0) {
                    amount--;
                    removeOneOf(s, key, player);
                }
            } else {
                while (s.getCount() > 0 && ! getType(s).equals("air")) {
                    amount--;
                    removeOneOf(s, key, player);
                }
            }
        }

        return true;
    }
    
    public static void removeOneOf(ItemStack stack, int slot, PlayerEntity player) {
        if (getType(stack).equals("air")) return;
        if (stack.getCount() <= 1) player.getInventory().setStack(slot, newItem(Items.AIR, 1));

        stack.setCount(stack.getCount() - 1);
    }
}
