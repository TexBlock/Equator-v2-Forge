package net.krlite.equator;

import net.fabricmc.api.ModInitializer;
import net.krlite.equator.base.Visual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Visual("main")
public class EquatorVisual implements ModInitializer {
	public static final String NAME = "Equator:Visual", ID = "equator-visual";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
	}
}
