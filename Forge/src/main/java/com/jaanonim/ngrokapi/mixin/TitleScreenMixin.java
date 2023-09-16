package com.jaanonim.ngrokapi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.jaanonim.ngrokapi.NgrokApiScreen;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "createNormalMenuOptions")
    private void createNormalMenuOptions(int y, int spacingY, CallbackInfo ci) {
        Button btn = Button
                .builder(Component.nullToEmpty("Ngrok"),
                        button -> this.minecraft.setScreen(new NgrokApiScreen(this)))
                .bounds(this.width / 2 - 100 + 205, y, 50, 20).build();

        this.addRenderableWidget(btn);
    }

}
