package tv.quaint.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Predicate;

public class ItemUtils {
    public static ItemStack newItem(Item material) {
        return new ItemStack(material);
    }

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

        ItemStack temp = toCheck.copy();
        temp.setCount(original.getCount());

        return temp.equals(original);
    }

    public static boolean isSame(ItemStack toCheck, ItemStack original) {
        if (toCheck == null) return false;
        if (original == null) return false;

        return toCheck.equals(original);
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
        ItemStack inv = player.getMainHandStack();
        if (! isSameAnyAmount(inv, stack)) return false;

        return inv.getCount() >= amount;
    }

    public static boolean removeAmountOf(PlayerEntity player, ItemStack stack, int amount) {
        if (amount - stack.getCount() != 0) return false;
        if (stack != player.getMainHandStack()) return false;

        removeItemInHand(player);
        return true;
    }

    public static void removeItemInHand(PlayerEntity player) {
//        player.getInventory().setStack(player.getInventory().selectedSlot, newItem(Items.AIR));
        player.getInventory().removeStack(player.getInventory().selectedSlot);
    }

    public static boolean isShopItem(ItemStack stack) {
        return hasStringedTagValue(stack, "shopItem", "yes");
    }
}
