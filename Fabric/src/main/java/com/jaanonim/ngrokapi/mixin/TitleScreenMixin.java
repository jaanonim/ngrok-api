package com.jaanonim.ngrokapi.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;
import com.jaanonim.ngrokapi.NgrokApiScreen;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {

        ButtonWidget btn = new ButtonWidget(this.width / 2 - 100 + 205, y, 50, 20, Text.of("Ngrok"),
                button -> this.client.setScreen(new NgrokApiScreen(this)));

        this.addDrawableChild(btn);
    }

}
