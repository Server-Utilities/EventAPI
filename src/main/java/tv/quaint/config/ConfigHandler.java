package tv.quaint.config;

import de.leonhard.storage.Config;
import de.leonhard.storage.sections.FlatFileSection;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import tv.quaint.EventAPI;
import tv.quaint.items.ConfiguredItem;
import tv.quaint.objects.events.ConfigurableEvent;
import tv.quaint.objects.events.EventHandler;
import tv.quaint.objects.events.EventType;
import tv.quaint.objects.events.rewards.ConfigurableReward;
import tv.quaint.objects.events.rewards.RewardType;
import tv.quaint.utils.MainUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {
    public Config config;
    public Config storage;
    public String cstring = "config.yml";
    public String sstring = "storage.yml";
    public File cfile = new File(EventAPI.getDataFolder(), cstring);
    public File sfile = new File(EventAPI.getDataFolder(), sstring);

    public ConfigHandler() {
        reloadConfig();
        reloadStorage();

        parseConfig();
    }

    public Config loadConfig() {
        return MainUtils.loadConfigFromSelf(cfile, cstring);
    }

    public Config loadStorage() {
        return MainUtils.loadConfigNoDefault(sfile);
    }

    public void reloadConfig() {
        config = loadConfig();
    }

    public void reloadStorage() {
        storage = loadStorage();
    }

    public void parseConfig() {
        EventHandler.flushEvents();

        for (String key : config.singleLayerKeySet("events")) {
            FlatFileSection section = config.getSection("events." + key);

            EventType type = section.getEnum("type", EventType.class);
            String value = section.getString("value");
            boolean cancelReal = section.getBoolean("cancel-real");

            ConfigurableEvent event = new ConfigurableEvent(key, type, value, cancelReal);

            List<ConfigurableReward> toAdd = new ArrayList<>();

            for (String k : section.singleLayerKeySet("rewards")) {
                FlatFileSection s = config.getSection("events." + key + ".rewards." + k);

                RewardType t = s.getEnum("type", RewardType.class);
                String v = s.getString("value");

                ConfigurableReward reward = new ConfigurableReward(k, t, v);

                EventAPI.LOGGER.info("Loaded ConfiguredReward: < identifier: '" + k + "', type: '" + t.name() + "', value: '" + v + "' >");

                toAdd.add(reward);
            }

            event = event.addRewards(toAdd.toArray(new ConfigurableReward[0]));

            EventHandler.registerConfiguredEvent(event);
        }
    }

    public int getFromStorage(String identifier, PlayerEntity player) {
        reloadStorage();
        return storage.getInt(identifier + "." + player.getUuidAsString());
    }

    public void setInStorage(String identifier, PlayerEntity player, int amount) {
        storage.set(identifier + "." + player.getUuidAsString(), amount);
    }

    public void addToStorage(String identifier, PlayerEntity player, int amount) {
        setInStorage(identifier, player, getFromStorage(identifier, player) + amount);
    }

    public void removeFromStorage(String identifier, PlayerEntity player, int amount) {
        setInStorage(identifier, player, getFromStorage(identifier, player) - amount);
    }

    public void addToStorage(String identifier, PlayerEntity player) {
        addToStorage(identifier, player, 1);
    }

    public void removeFromStorage(String identifier, PlayerEntity player) {
        removeFromStorage(identifier, player, 1);
    }

    public List<ConfiguredItem> getConfiguredItems() {
        reloadConfig();

        List<ConfiguredItem> items = new ArrayList<>();

        FlatFileSection section = config.getSection("items");

        for (String key : section.singleLayerKeySet()) {
            items.add(new ConfiguredItem(key, section.getString(key)));
        }

        return items;
    }
}
