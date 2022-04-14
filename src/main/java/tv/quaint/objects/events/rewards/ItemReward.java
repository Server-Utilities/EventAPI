package tv.quaint.objects.events.rewards;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import tv.quaint.objects.lists.SingleSet;
import tv.quaint.utils.ItemUtils;
import tv.quaint.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ItemReward {
    public Item material;
    public int amount;
    public String name;
    public List<String> lore;
    public TreeMap<String, String> tags;

    public ItemReward() {
        this.material = null;
        this.amount = 1;
        this.name = "";
        this.lore = new ArrayList<>();
        this.tags = new TreeMap<>();
    }

    public ItemReward(Item material, int amount, String name, List<String> lore, TreeMap<String, String> tags) {
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lore = lore;
        this.tags = tags;
    }

    public ItemReward setMaterial(Item material) {
        this.material = material;

        return this;
    }

    public ItemReward setAmount(int amount) {
        this.amount = amount;

        return this;
    }

    public ItemReward setName(String name) {
        this.name = name;

        return this;
    }

    public ItemReward setLore(List<String> lore) {
        this.lore = lore;

        return this;
    }

    public ItemReward addLore(String string) {
        this.lore.add(string);

        return this;
    }

    public ItemReward setTags(TreeMap<String, String> tags) {
        this.tags = tags;

        return this;
    }

    public ItemReward addTag(SingleSet<String, String> set) {
        tags.put(set.key, set.value);

        return this;
    }

    public ItemReward removeTag(String key) {
        tags.remove(key);

        return this;
    }

    public ItemStack asStack() {
        ItemStack stack = ItemUtils.newItem(this.material, this.amount);
        ItemUtils.setName(stack, this.name);
        ItemUtils.setLore(stack, this.lore);
        ItemUtils.putStringedTags(stack, tags);

        return stack;
    }
}
