package tv.quaint.objects.events;

import com.google.re2j.Matcher;
import net.minecraft.entity.player.PlayerEntity;
import tv.quaint.EventAPI;
import tv.quaint.items.ParsedHold;
import tv.quaint.utils.MainUtils;

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
            event.rewardPlayer(player);
        }
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
