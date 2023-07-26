package com.jaanonim.ngrokapi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

@Environment(value = EnvType.CLIENT)
public class AddNgrokTokenScreen extends Screen {
    private ButtonWidget saveButton;
    private TextFieldWidget tokenField;
    private final Screen parent;
    private TextFieldWidget nameField;

    public AddNgrokTokenScreen(Screen parent) {
        super(Text.of("Add Ngrok Api Token"));
        this.parent = parent;
    }

    @Override
    public void tick() {
        this.tokenField.tick();
        this.nameField.tick();
    }

    @Override
    protected void init() {
        this.nameField = new TextFieldWidget(this.textRenderer, this.width / 2 -
                100, 70, 200, 20,
                Text.translatable("addServer.enterName"));
        this.nameField.setFocused(true);
        this.nameField.setMaxLength(100);
        this.nameField.setChangedListener(serverName -> this.updateSaveButton());
        this.addSelectableChild(this.nameField);

        this.tokenField = new TextFieldWidget(this.textRenderer, this.width / 2 -
                100, 120, 200, 20,
                Text.translatable("addServer.enterName"));
        this.tokenField.setMaxLength(100);
        this.tokenField.setChangedListener(serverName -> this.updateSaveButton());
        this.addSelectableChild(this.tokenField);

        this.saveButton = this.addDrawableChild(ButtonWidget.builder(
                Text.of("Save"), button -> this.save()).dimensions(this.width / 2 - 110,
                        this.height - 55,
                        100, 20)
                .build());

        this.addDrawableChild(ButtonWidget.builder(
                ScreenTexts.CANCEL, button -> this.close())
                .dimensions(this.width / 2 + 10, this.height - 55,
                        100, 20)
                .build());

        this.updateSaveButton();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        String s1 = this.nameField.getText();
        String s2 = this.tokenField.getText();
        this.init(client, width, height);
        this.nameField.setText(s1);
        this.tokenField.setText(s2);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    public void save() {
        new NgrokTokenList().add(new NgrokToken(this.nameField.getText(), this.tokenField.getText()));
        this.close();
    }

    private void updateSaveButton() {
        this.saveButton.active = !this.nameField.getText().isEmpty() && !this.tokenField.getText().isEmpty();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 17,
                0xFFFFFF);

        context.drawTextWithShadow(this.textRenderer, Text.of("Name"), this.width / 2 - 100,
                54,
                0xA0A0A0);
        context.drawTextWithShadow(this.textRenderer, Text.of("Ngrok API token"), this.width / 2 - 100,
                104,
                0xA0A0A0);

        this.nameField.render(context, mouseX, mouseY, delta);
        this.tokenField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
