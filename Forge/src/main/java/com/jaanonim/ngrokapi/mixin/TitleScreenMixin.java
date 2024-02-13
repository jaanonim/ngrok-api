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
import net.minecraft.network.chat.TextComponent;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "createNormalMenuOptions")
    private void createNormalMenuOptions(int p_96764_, int p_96765_, CallbackInfo ci) {
        Button btn = new Button(this.width / 2 - 100 + 205, p_96764_, 50, 20, new TextComponent("Ngrok"),
                (p_96771_) -> {
                    this.minecraft.setScreen(new NgrokApiScreen(this));
                });
        this.addRenderableWidget(btn);
    }

}
