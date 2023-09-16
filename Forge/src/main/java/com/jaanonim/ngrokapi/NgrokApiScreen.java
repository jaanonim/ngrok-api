package com.jaanonim.ngrokapi;

import com.jaanonim.ngrokapi.NgrokTokenListWidget.NgrokTokenEntry;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.TextComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NgrokApiScreen extends Screen {
    private Button delButton;
    private Button joinButton;
    private NgrokTokenListWidget listWidget;
    private NgrokTokenList list;
    private final Screen lastScreen;
    private NgrokTokenEntry selectedEntry = null;

    public NgrokApiScreen(Screen lastScreen) {
        super(new TextComponent("Ngrok Api"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.list = new NgrokTokenList();

        this.joinButton = this.addRenderableWidget(new Button(this.width / 2 - 110,
                this.height - 55,
                100, 20,
                new TextComponent("Join"), button -> this.join()));

        this.delButton = this.addRenderableWidget(new Button(this.width / 2 + 10, this.height - 55,
                100, 20,
                new TextComponent("Delete"), button -> this.delete()));

        this.addRenderableWidget(new Button(this.width / 2 - 140, this.height - 30,
                80, 20,
                new TextComponent("Add"), button -> this.add()));

        this.addRenderableWidget(new Button(this.width / 2 - 40, this.height - 30,
                80, 20,
                new TextComponent("Refresh"), button -> this.refresh()));

        this.addRenderableWidget(new Button(this.width / 2 + 60,
                this.height - 30,
                80, 20,
                new TextComponent("Cancel"), button -> this.onClose()));

        this.listWidget = new NgrokTokenListWidget(minecraft, width, height - 120, 40, height
                - 60, 30);
        this.addRenderableWidget(this.listWidget);
        this.repopulateWidgetEnters();
        this.resetSelection();
    }

    private void add() {
        this.minecraft.setScreen(new AddNgrokTokenScreen(this));
    }

    private void delete() {
        NgrokToken entry = this.selectedEntry.getEntry();
        this.list.remove(entry);
        this.resetSelection();
        this.repopulateWidgetEnters();

    }

    private void resetSelection() {
        this.selectedEntry = null;
        updateButtons();
    }

    private void repopulateWidgetEnters() {
        this.listWidget.clear();
        this.list.getList().forEach((ele) -> {
            this.listWidget.addElement(new NgrokTokenListWidget.NgrokTokenEntry(this, ele));
        });
    }

    private void refresh() {
        this.listWidget.children().forEach((ele) -> {
            ele.refresh();
        });
        updateButtons();
    }

    private void updateButtons() {
        if (this.selectedEntry == null) {
            this.delButton.active = false;
            this.joinButton.active = false;
        } else {
            this.delButton.active = true;
            this.joinButton.active = this.selectedEntry.getAddress().canConnect();
        }
    }

    public void selected(NgrokTokenListWidget.NgrokTokenEntry entry) {
        this.selectedEntry = entry;
        this.listWidget.setSelected(entry);
        updateButtons();
    }

    @Override
    public void resize(Minecraft client, int width, int height) {
        this.init(client, width, height);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    public void join() {
        NgrokAddress adr = this.selectedEntry.getAddress();
        if (adr.canConnect()) {
            ServerAddress sa = new ServerAddress(adr.getHost(), adr.getPort());
            ConnectScreen.startConnecting(this, this.minecraft, sa, null);
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.listWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
