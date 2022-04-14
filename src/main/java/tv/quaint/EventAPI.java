package tv.quaint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.quaint.commands.ReloadCommand;
import tv.quaint.config.ConfigHandler;
import tv.quaint.events.EventManager;
import tv.quaint.tickables.SecondTicker;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

public class EventAPI implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("EventAPI");
	public static MinecraftServer SERVER;
	public static SecondTicker secondTicker;
	public static EventAPI INSTANCE;
	public static ConfigHandler CONFIG;

	@Override
	public void onInitialize() {
		INSTANCE = this;

		secondTicker = new SecondTicker(20);

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SERVER = server;
		});

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			secondTicker.tick();
		});

		CONFIG = new ConfigHandler();

		EventManager.registerEvents();

		LOGGER.info("Initialized!");

		registerCommands();
	}

	public void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			new ReloadCommand().register(dispatcher);
		});
	}

	public static File getWorkingDir() {
		return Paths.get("").toAbsolutePath().toFile();
	}

	public static File getDataFolder() {
		return new File(getWorkingDir(), "data" + File.separator);
	}

	public InputStream getResource(String fileName) {
		return this.getClass().getResourceAsStream(fileName);
	}
}
