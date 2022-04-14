package tv.quaint.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import tv.quaint.EventAPI;
import tv.quaint.messages.CommandMessages;
import tv.quaint.utils.MainUtils;
import tv.quaint.utils.MessagingUtils;
import tv.quaint.utils.TextUtils;

public class ReloadCommand implements Command<ServerCommandSource> {
    public ReloadCommand() {
        info("Loading!");
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = context.getSource().getPlayer();

        EventAPI.CONFIG.reloadConfig();
        player.sendMessage(MutableText.Serializer.fromJson(CommandMessages.RELOAD_CONFIG_RELOADED.parse()), false);

        EventAPI.CONFIG.reloadStorage();
        player.sendMessage(MutableText.Serializer.fromJson(CommandMessages.RELOAD_STORAGE_RELOADED.parse()), false);

        return 0;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("eventapi:reload").executes(this));
        info("Registered!");
    }

    public void info(String message) {
        MessagingUtils.info(getConsolePrefix() + message);
    }

    public String getConsolePrefix() {
        return this.getClass().getSimpleName() + " ><> ";
    }
}
