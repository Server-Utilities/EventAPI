package tv.quaint.items;

import net.minecraft.item.ItemStack;

public class ConfiguredItem {
    public String identifier;
    public String rawValue;

    public ConfiguredItem(String identifier, String rawValue) {
        this.identifier = identifier;
        this.rawValue = rawValue;
    }
}
