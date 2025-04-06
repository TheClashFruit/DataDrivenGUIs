package me.theclashfruit.ddg;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class DataDrivenGUIs implements ModInitializer {
    public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger("DataDrivenGUIs");

    @Override
    public void onInitialize() {
        LOGGER.info("DataDrivenGUIs is initializing...");
    }
}
