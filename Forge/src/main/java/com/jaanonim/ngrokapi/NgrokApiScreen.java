package com.jaanonim.ngrokapi;

import com.jaanonim.ngrokapi.NgrokTokenListWidget.NgrokTokenEntry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
        super(Component.nullToEmpty("Ngrok Api"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.list = new NgrokTokenList();

        this.joinButton = this.addRenderableWidget(Button.builder(
                Component.nullToEmpty("Join"), button -> this.join()).bounds(this.width / 2 - 110,
                        this.height - 55,
                        100, 20)
                .build());

        this.delButton = this.addRenderableWidget(Button.builder(
                Component.nullToEmpty("Delete"), button -> this.delete())
                .bounds(this.width / 2 + 10, this.height - 55,
                        100, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                Component.nullToEmpty("Add"), button -> this.add())
                .bounds(this.width / 2 - 140, this.height - 30,
                        80, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                Component.nullToEmpty("Refresh"), button -> this.refresh())
                .bounds(this.width / 2 - 40, this.height - 30,
                        80, 20)
                .build());

        this.addRenderableWidget(Button.builder(
                CommonComponents.GUI_CANCEL, button -> this.onClose())
                .bounds(this.width / 2 + 60,
                        this.height - 30,
                        80, 20)
                .build());

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
            ConnectScreen.startConnecting(this, this.minecraft, sa,
                    new ServerData(this.selectedEntry.getEntry().getName(), adr.getFull(), false), false);
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        this.listWidget.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
