package tv.quaint.objects.events;

import com.google.re2j.Matcher;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import tv.quaint.EventAPI;
import tv.quaint.items.ParsedHold;
import tv.quaint.objects.events.conditions.BlockEqualsCondition;
import tv.quaint.objects.events.conditions.ConditionType;
import tv.quaint.objects.events.conditions.ConfigurableCondition;
import tv.quaint.objects.lists.SingleSet;
import tv.quaint.utils.ItemUtils;
import tv.quaint.utils.MainUtils;
import tv.quaint.utils.MessagingUtils;

import java.util.ArrayList;
import java.util.List;

public class EventHandler {
    public static List<ConfigurableEvent> events = new ArrayList<>();

    public static void registerConfiguredEvent(ConfigurableEvent event) {
        if (alreadyRegistered(event)) return;

        EventAPI.LOGGER.info("Registered ConfiguredEvent: < identifier: '" + event.identifier + "', type: '" + event.type.name() + "', value: '" + event.value + "' >");

        events.add(event);
    }

    public static void unregisterConfiguredEvent(ConfigurableEvent event) {
        if (! alreadyRegistered(event)) return;

        events.remove(event);
    }

    public static boolean alreadyRegistered(ConfigurableEvent event) {
        for (ConfigurableEvent configuredEvent : events) {
            if (event.identifier.equals(configuredEvent.identifier)) return true;
        }

        return false;
    }

    public static List<ConfigurableEvent> flushEvents() {
        List<ConfigurableEvent> toReturn = events;

        events = new ArrayList<>();

        return toReturn;
    }

    public static void handleEvents(PlayerEntity player, List<ConfigurableEvent> toHandle) {
        for (ConfigurableEvent event : toHandle) {
            if (! conditionsPassed(player, event.conditions)) continue;
            event.rewardPlayer(player);
        }
    }

    public static boolean conditionsPassed(PlayerEntity player, List<ConfigurableCondition> conditions) {
        for (ConfigurableCondition condition : conditions) {
            if (condition.type.equals(ConditionType.SHIFTING)) {
                if (player.isSneaking() != Boolean.parseBoolean(condition.unparsedValue)) return false;
            }
            if (condition.type.equals(ConditionType.BLOCK_EQUALS)) {
                BlockEqualsCondition bec = parseBlockEqualsCondition(condition.unparsedValue);

                MessagingUtils.info("BEC fromSender = " + bec.fromSender);

                if (bec.fromSender) {
                    int x = bec.x + player.getBlockX();
                    int y = bec.y + player.getBlockY();
                    int z = bec.z + player.getBlockZ();
                    BlockState state = player.getWorld().getBlockState(new BlockPos(x, y, z));

                    String type = ItemUtils.getType(state.getBlock().asItem().getDefaultStack());
                    MessagingUtils.info(type);

                    if (! type.equals(bec.blockType)) return false;
                } else {
                    BlockPos blockPos = new BlockPos(bec.x, bec.y, bec.z);
                    BlockState state = player.getWorld().getBlockState(blockPos);
                    if (! ItemUtils.getType(state.getBlock().asItem().getDefaultStack()).equals(bec.blockType)) return false;
                }
            }
            if (condition.type.equals(ConditionType.ITEM_COUNT)) {
                SingleSet<Integer, Integer> set = parseItemCountData(condition.unparsedValue);

                if (set.value == -1) {
                    if (player.getMainHandStack().getCount() != set.key) return false;
                } else {
                    if (player.getInventory().getStack(set.value) == null) return false;
                    if (player.getInventory().getStack(set.value).getCount() != set.key) return false;
                }
            }
            if (condition.type.equals(ConditionType.ITEM_TAG)) {
                SingleSet<String, String> set = parseItemTagData(condition.unparsedValue);
                ItemStack stack = player.getMainHandStack();

                if (! ItemUtils.hasStringedTagValue(stack, set.key, set.value)) return false;
            }
        }

        MessagingUtils.info("Conditions passed!");
        return true;
    }

    public static SingleSet<String, String> parseItemTagData(String from) {
        String[] split = from.split(",", 2);

        return new SingleSet<>(split[0], split[1]);
    }

    public static SingleSet<Integer, Integer> parseItemCountData(String from) {
        String[] split = from.split(",", 2);

        try {
            int amount = Integer.parseInt(split[0]);
            if (split[1].equals("@")) split[1] = "-1";
            int slot = Integer.parseInt(split[1]);
            return new SingleSet<>(amount, slot);
        } catch (Exception e) {
            e.printStackTrace();

            return new SingleSet<>(1, -1);
        }
    }

    public static BlockEqualsCondition parseBlockEqualsCondition(String from) {
        String[] split = from.split(",", 5);
        BlockEqualsCondition condition = new BlockEqualsCondition();

        condition = condition.setFromSender(Boolean.parseBoolean(split[0]));
        condition = condition.setX(Integer.parseInt(split[1]));
        condition = condition.setY(Integer.parseInt(split[2]));
        condition = condition.setZ(Integer.parseInt(split[3]));
        condition = condition.setBlockType(split[4]);

        return condition;
    }

    public static ConfigurableCondition parseCondition(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        ConfigurableCondition condition = new ConfigurableCondition();
        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("type")) {
                condition = condition.setType(ConditionType.UNDEFINED);

                if (varContent.equalsIgnoreCase("shifting")) condition = condition.setType(ConditionType.SHIFTING);
                if (varContent.equalsIgnoreCase("block_equals")) condition = condition.setType(ConditionType.BLOCK_EQUALS);
                if (varContent.equalsIgnoreCase("item_count")) condition = condition.setType(ConditionType.ITEM_COUNT);
                if (varContent.equalsIgnoreCase("item_tag")) condition = condition.setType(ConditionType.ITEM_TAG);
            }
            if (varIdentifier.equals("value")) {
                condition = condition.setUnparsedValue(varContent);
            }
        }

        return condition;
    }

    public static String parseKillEntity(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("entity")) {
                return varContent;
            }
        }

        return "";
    }

    public static String parseDeathByEntity(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("entity")) {
                return varContent;
            }
        }

        return "";
    }

    public static String parseConsumeType(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("material")) {
                return varContent;
            }
        }

        return "";
    }

    public static ParsedHold parseHeldType(String from) {
        Matcher matcher = MainUtils.setupMatcher("((.*?)[:](.*?)[;])", from);

        ParsedHold hold = new ParsedHold();
        while (matcher.find()) {
            String unparsed = matcher.group(1);
            String varIdentifier = matcher.group(2);
            String varContent = matcher.group(3);

            if (varIdentifier.equals("material")) {
                hold.setMaterial(varContent);
            }

            if (varIdentifier.equals("main-hand")) {
                hold.setMainHand(Boolean.parseBoolean(varContent));
            }
        }

        return hold;
    }

    public static List<ConfigurableEvent> getEventsByType(EventType type) {
        List<ConfigurableEvent> toReturn = new ArrayList<>();

        for (ConfigurableEvent event : events) {
            if (event.type.equals(type)) toReturn.add(event);
        }

        return toReturn;
    }

    public static List<ConfigurableEvent> getEventsWithCertainKillMob(String killMobType) {
        List<ConfigurableEvent> toReturn = new ArrayList<>();

        for (ConfigurableEvent event : getEventsByType(EventType.KILL)) {
            if (parseKillEntity(event.value).equals(killMobType)) toReturn.add(event);
        }

        return toReturn;
    }

    public static List<ConfigurableEvent> getEventsWithCertainDeathByMob(String deathByMobType) {
        List<ConfigurableEvent> toReturn = new ArrayList<>();

        for (ConfigurableEvent event : getEventsByType(EventType.DEATH)) {
            if (parseDeathByEntity(event.value).equals(deathByMobType)) toReturn.add(event);
        }

        return toReturn;
    }

    public static List<ConfigurableEvent> getEventsWithCertainConsumeType(String consumeType) {
        List<ConfigurableEvent> toReturn = new ArrayList<>();

        for (ConfigurableEvent event : getEventsByType(EventType.CONSUME)) {
            if (parseConsumeType(event.value).equals(consumeType)) toReturn.add(event);
        }

        return toReturn;
    }

    public static List<ConfigurableEvent> getEventsWithCertainHoldType(String consumeType, boolean main) {
        List<ConfigurableEvent> toReturn = new ArrayList<>();

        for (ConfigurableEvent event : getEventsByType(EventType.HOLD)) {
            ParsedHold hold = parseHeldType(event.value);

            if (hold.material.equals(consumeType) && hold.mainHand == main) toReturn.add(event);
        }

        return toReturn;
    }
}
