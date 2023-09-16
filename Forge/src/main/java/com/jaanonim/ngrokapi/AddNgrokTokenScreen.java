package com.jaanonim.ngrokapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AddNgrokTokenScreen extends Screen {
    private Button saveButton;
    private EditBox tokenField;
    private EditBox nameField;
    private final Screen lastScreen;

    public AddNgrokTokenScreen(Screen lastScreen) {
        super(Component.nullToEmpty("Add Ngrok Api Token"));
        this.lastScreen = lastScreen;
    }

    @Override
    public void tick() {
        this.tokenField.tick();
        this.nameField.tick();
    }

    @Override
    protected void init() {

        this.nameField = new EditBox(this.font, this.width / 2 -
                100, 70, 200, 20,
                Component.translatable("addServer.enterName"));
        this.nameField.setMaxLength(100);
        this.nameField.setResponder(serverName -> this.updateSaveButton());
        this.addWidget(this.nameField);

        this.tokenField = new EditBox(this.font, this.width / 2 -
                100, 120, 200, 20,
                Component.translatable("addServer.enterName"));
        this.tokenField.setMaxLength(100);
        this.tokenField.setResponder(serverName -> this.updateSaveButton());
        this.addWidget(this.tokenField);

        this.saveButton = this.addRenderableWidget(Button.builder(
                Component.nullToEmpty("Save"), button -> this.save()).bounds(this.width / 2 - 110,
                        this.height - 55,
                        100, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_CANCEL, button -> this.onClose())
                .bounds(this.width / 2 + 10, this.height - 55,
                        100, 20)
                .build());

        this.updateSaveButton();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s1 = this.nameField.getValue();
        String s2 = this.tokenField.getValue();
        this.init(minecraft, width, height);
        this.nameField.setValue(s1);
        this.tokenField.setValue(s2);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    public void save() {
        new NgrokTokenList().add(new NgrokToken(this.nameField.getValue(), this.tokenField.getValue()));
        this.onClose();
    }

    private void updateSaveButton() {
        this.saveButton.active = !this.nameField.getValue().isEmpty() && !this.tokenField.getValue().isEmpty();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredString(this.font, this.title.getVisualOrderText(),
                this.width / 2, 17,
                0xFFFFFF);

        context.drawCenteredString(this.font, Component.nullToEmpty("Name"), this.width / 2 - 100,
                54,
                0xA0A0A0);
        context.drawCenteredString(this.font, Component.nullToEmpty("Ngrok API token"),
                this.width / 2 - 100,
                104,
                0xA0A0A0);

        this.nameField.render(context, mouseX, mouseY, delta);
        this.tokenField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
