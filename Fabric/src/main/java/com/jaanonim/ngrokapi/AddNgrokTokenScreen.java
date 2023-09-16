package com.jaanonim.ngrokapi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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
                new TranslatableText("addServer.enterName"));
        this.nameField.setMaxLength(100);
        this.nameField.setChangedListener(serverName -> this.updateSaveButton());
        this.addSelectableChild(this.nameField);

        this.tokenField = new TextFieldWidget(this.textRenderer, this.width / 2 -
                100, 120, 200, 20,
                new TranslatableText("addServer.enterName"));
        this.tokenField.setMaxLength(100);
        this.tokenField.setChangedListener(serverName -> this.updateSaveButton());
        this.addSelectableChild(this.tokenField);

        this.saveButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 110,
                this.height - 55,
                100, 20,
                Text.of("Save"), button -> this.save()));

        this.addDrawableChild(new ButtonWidget(
                this.width / 2 + 10, this.height - 55,
                100, 20, ScreenTexts.CANCEL, button -> this.close()));

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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        NgrokApiScreen.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title.asOrderedText(),
                this.width / 2, 17,
                0xFFFFFF);

        NgrokApiScreen.drawTextWithShadow(matrices, this.textRenderer, Text.of("Name"), this.width / 2 - 100,
                54,
                0xA0A0A0);
        NgrokApiScreen.drawTextWithShadow(matrices, this.textRenderer, Text.of("Ngrok API token"), this.width / 2 - 100,
                104,
                0xA0A0A0);

        this.nameField.render(matrices, mouseX, mouseY, delta);
        this.tokenField.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
