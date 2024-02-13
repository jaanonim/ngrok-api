package com.jaanonim.ngrokapi;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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

        this.saveButton = this.addRenderableWidget(new Button(this.width / 2 - 110,
                this.height - 55,
                100, 20,
                Component.nullToEmpty("Save"), button -> this.save()));

        this.addRenderableWidget(new Button(
                this.width / 2 + 10, this.height - 55,
                100, 20, CommonComponents.GUI_CANCEL, button -> this.onClose()));

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
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        GuiComponent.drawCenteredString(matrices, this.font, this.title.getVisualOrderText(),
                this.width / 2, 17,
                0xFFFFFF);

        GuiComponent.drawCenteredString(matrices, this.font, Component.nullToEmpty("Name"), this.width / 2 - 100,
                54,
                0xA0A0A0);
        GuiComponent.drawCenteredString(matrices, this.font, Component.nullToEmpty("Ngrok API token"),
                this.width / 2 - 100,
                104,
                0xA0A0A0);

        this.nameField.render(matrices, mouseX, mouseY, delta);
        this.tokenField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
