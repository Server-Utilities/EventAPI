package tv.quaint.utils.text;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class ComponentText {
    public static String updateLegacyStyle(@NotNull final String text) {
        Validate.notNull(text, "Cannot translate null text");
        if (!text.contains("&")) return text;
        String string = text;
        final char[] b = text.toCharArray();
        for (int i = 0; i < b.length; i++) {
            if (b[i] == '&' && i != b.length - 1 && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                final @Nullable Format format = Format.getByChar(b[i + 1]);
                string = string.replace(
                        String.valueOf('&') + b[i + 1],
                        "<" + (format == null ? "reset" : format.name().toLowerCase(Locale.ENGLISH)) + ">"
                );
            }
        }
        return string;
    }

    public static Component of(@NotNull final String string) {
        Validate.notNull(string, "String must not be null!");
        return MiniMessage.miniMessage().deserialize(updateLegacyStyle(string));
    }

    public static MutableText toText(@NotNull final String raw) {
        Validate.notNull(raw, "Input must not be null!");
        return Text.Serializer.fromJson(GsonComponentSerializer.gson().serialize(of(raw)));
    }
}
