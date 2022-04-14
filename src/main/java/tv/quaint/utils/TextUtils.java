package tv.quaint.utils;

import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import tv.quaint.utils.text.ComponentText;

public class TextUtils {
    private static final String SEPARATOR = "-----------------------------------------------------";

    public static MutableText newText(final String str) {
        return ComponentText.toText(str);
    }

    public static MutableText newText() {
        return new LiteralText("");
    }

    public static MutableText newRawText(final String string) {
        return new LiteralText(string);
    }

    public static MutableText blockStyle(MutableText text) {
        MutableText separator = new LiteralText(SEPARATOR).formatted(Formatting.GRAY);
        return new LiteralText("").append(separator).append(text).append(separator);
    }

    public static MutableText appendButton(MutableText text, MutableText hoverText, ClickEvent.Action action, String actionValue) {
        return text.styled((style) -> {
            style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
            style.withClickEvent(new ClickEvent(action, actionValue));
            return style;
        });
    }

    public static MutableText getButton(String title, String command, MutableText hoverText) {
        return newText(title).styled((style) -> style.withHoverEvent(Events.onHover(hoverText)).withClickEvent(Events.onClickRun(command)));
    }

    public static MutableText getButton(String title, String command, String string) {
        return newText(title).styled((style) -> style.withHoverEvent(Events.onHover(string)).withClickEvent(Events.onClickRun(command)));
    }

    public static class Events {
        public static ClickEvent onClickCopy(String string) {
            return new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, string);
        }

        public static ClickEvent onClickSuggest(String command) {
            return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
        }

        public static ClickEvent onClickRun(String command) {
            return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        }

        public static ClickEvent onClickRun(String... command) {
            StringBuilder builder = new StringBuilder();
            for (String s : command) {
                builder.append(s);
            }

            return onClickRun("/" + builder);
        }

        public static ClickEvent onClickOpen(String url) {
            return new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        }

        public static HoverEvent onHover(String text) {
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentText.toText(text));
        }

        public static HoverEvent onHover(MutableText text) {
            return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
        }
    }
}
