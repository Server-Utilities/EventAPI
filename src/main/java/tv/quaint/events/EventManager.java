package tv.quaint.events;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import tv.quaint.EventAPI;
import tv.quaint.items.ItemHandler;
import tv.quaint.objects.events.EventHandler;
import tv.quaint.utils.ItemUtils;
import tv.quaint.utils.MessagingUtils;

public class EventManager {
    public static void registerEvents() {
        EntityEvents.KILL_ENTITY_EVENT.register(((player, entity) -> {
            String entityName = entity.getType().getUntranslatedName();

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainKillMob(entityName));
        }));
        EntityEvents.PLAYER_DEATH_BY_ENTITY_EVENT.register((player, entity) -> {
            String entityName = entity.getType().getUntranslatedName();

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainDeathByMob(entityName));
        });
        ItemEvents.PLAYER_CONSUME_ITEM_EVENT.register((player, itemStack, ci) -> {
            String itemType = ItemUtils.getType(itemStack);

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainConsumeType(itemType));
        });
        ItemEvents.PLAYER_HOLD_ITEM_FOR_ONE_SECOND_EVENT.register((player, mainHand, offHand) -> {
            String mainType = ItemUtils.getType(mainHand);
            String offType = ItemUtils.getType(offHand);

            BlockPos under = player.getBlockPos().add(0, -1, 0);
            Block block = player.getWorld().getBlockState(under).getBlock();

            if (player.isSneaking() && ItemUtils.getType(block.asItem().getDefaultStack()).equals("bedrock")) {
                if (ItemUtils.isShopItem(mainHand)) {
                    if (mainHand.getCount() == 4) {
                        if (ItemUtils.removeAmountOf(player, mainHand, 4)) {
                            EventAPI.SERVER.getCommandManager().execute(EventAPI.SERVER.getCommandSource(), "claim blocks add " + player.getEntityName() + " 3000");
                        }
                    }
                    if (mainHand.getCount() == 2) {
                        if (ItemUtils.removeAmountOf(player, mainHand, 2)) {
                            EventAPI.SERVER.getCommandManager().execute(EventAPI.SERVER.getCommandSource(), "rtp add " + player.getEntityName() + " 1");
                        }
                    }
                }
            }

            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainHoldType(mainType, true));
            EventHandler.handleEvents(player, EventHandler.getEventsWithCertainHoldType(offType, false));
        });
    }
}
