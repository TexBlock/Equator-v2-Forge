package net.krlite.equator;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Mod(Equator.ID)
public class Equator {
	public static final String NAME = "Equator", ID = "equator";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);
	public static final boolean DEBUG = false;

	private static long lastFrame = 0, frameDiff = 0;

	public Equator() {
		if (FMLLoader.getDist().isClient()) {
			ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
		} else if (FMLLoader.getDist().isDedicatedServer()) {
			throw new RuntimeException("Equator can only run on the client!");
		}
	}

	public static void updateFrame(long currentFrame) {
		frameDiff = currentFrame - lastFrame;
		lastFrame = currentFrame;
	}

	public static Optional<Double> fps() {
		if (frameDiff <= 0) return Optional.empty();

		return Optional.of(1000.0 / frameDiff);
	}
}
