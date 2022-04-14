package tv.quaint.objects.events.rewards;

import com.google.re2j.Matcher;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tv.quaint.EventAPI;
import tv.quaint.items.AmountJustification;
import tv.quaint.items.ConfiguredItem;
import tv.quaint.items.ItemHandler;
import tv.quaint.objects.lists.SingleSet;
import tv.quaint.utils.ItemUtils;
import tv.quaint.utils.MainUtils;

import java.util.List;

public class RewardHandler {
    public static void handleReward(ConfigurableReward reward, PlayerEntity player) {
        if (reward.type.equals(RewardType.COMMAND)) {
            String command = reward.value.startsWith("/") ? reward.value.substring(1) : reward.value;
            command = command
                    .replace("%player%", player.getName().getString())
                    .replace("%uuid%", player.getUuidAsString());
            EventAPI.SERVER.getCommandManager().execute(EventAPI.SERVER.getCommandSource(), command);
        }
        if (reward.type.equals(RewardType.ITEM)) {
            ConfiguredItem item = ItemHandler.getConfiguredItemByIdentifier(reward.value);
            if (item == null) return;

            ItemReward itemReward = parseItemValue(item.rawValue);

            player.giveItemStack(itemReward.asStack());
        }
        if (reward.type.equals(RewardType.STORAGE)) {
            StorageSolution storageSolution = parseStorageSolution(reward.value);

            if (storageSolution.value.startsWith("+")) {
                try {
                    int add = Integer.parseInt(storageSolution.value.substring(1));
                    EventAPI.CONFIG.addToStorage(storageSolution.identifier, player, add);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (storageSolution.value.startsWith("-")) {
                try {
                    int remove = Integer.parseInt(storageSolution.value.substring(1));
                    EventAPI.CONFIG.removeFromStorage(storageSolution.identifier, player, remove);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            try {
                int set = Integer.parseInt(storageSolution.value);
                EventAPI.CONFIG.setInStorage(storageSolution.identifier, player, set);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static StorageSolution parseStorageSolution(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        StorageSolution storageSolution = new StorageSolution();

        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("identifier")) {
                storageSolution.setIdentifier(varContent);
            }
            if (varIdentifier.equals("value")) {
                storageSolution.setValue(varContent);
            }
        }

        return storageSolution;
    }

    public static ItemReward parseItemValue(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        ItemReward itemReward = new ItemReward();

        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("material")) {
                Identifier identifier = new Identifier(varContent);
                itemReward = itemReward.setMaterial(Registry.ITEM.get(identifier));
            }
            if (varIdentifier.equals("amount")) {
                try {
                    int amount = Integer.parseInt(varContent);
                    itemReward = itemReward.setAmount(amount);
                } catch (Exception e) {
                    try {
                        int amount = new AmountJustification(varContent).roll();
                        itemReward = itemReward.setAmount(amount);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            if (varIdentifier.equals("name")) {
                itemReward = itemReward.setName(varContent);
            }
            if (varIdentifier.equals("tags")) {
                String[] pairs = varContent.split(",");
                for (String p : pairs) {
                    String[] pair = p.split(":", 2);
                    itemReward = itemReward.addTag(new SingleSet<>(pair[0], pair[1]));
                }
            }
        }

        return itemReward;
    }
}
