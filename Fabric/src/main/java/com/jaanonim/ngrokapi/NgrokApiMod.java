package com.jaanonim.ngrokapi;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NgrokApiMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ngrokapi");

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Ngrok Api!");
    }

}
