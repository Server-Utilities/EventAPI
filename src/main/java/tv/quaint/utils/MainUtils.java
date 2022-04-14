package tv.quaint.utils;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import net.minecraft.server.network.ServerPlayerEntity;
import tv.quaint.EventAPI;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class MainUtils {
    public static List<ServerPlayerEntity> getOnlinePlayers() {
        return EventAPI.SERVER.getPlayerManager().getPlayerList();
    }

    public static Config loadConfigFromSelf(File file, String fileString) {
        if (! file.exists()) {
            try {
                EventAPI.getDataFolder().mkdirs();
                try (InputStream in = EventAPI.INSTANCE.getResource(fileString)) {
                    assert in != null;
                    Files.copy(in, file.toPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return LightningBuilder.fromFile(file).createConfig();
    }

    public static Config loadConfigNoDefault(File file) {
        if (! file.exists()) {
            try {
                EventAPI.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return LightningBuilder.fromFile(file).createConfig();
    }

    public static String resize(String text, int digits) {
        try {
            digits = getDigits(digits, text.length());
            return text.substring(0, digits);
        } catch (Exception e) {
            return text;
        }
    }

    public static String truncate(String text, int digits) {
        if (! text.contains(".")) return text;

        try {
            digits = getDigits(text.indexOf(".") + digits + 1, text.length());
            return text.substring(0, digits);
        } catch (Exception e) {
            return text;
        }
    }

    public static int getDigits(int start, int otherSize){
        return Math.min(start, otherSize);
    }

    public static Matcher setupMatcher(String regex, String from) {
        Pattern search = Pattern.compile(regex);

        return search.matcher(from);
    }
}
