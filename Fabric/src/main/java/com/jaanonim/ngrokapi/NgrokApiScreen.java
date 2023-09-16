package com.jaanonim.ngrokapi;

import com.jaanonim.ngrokapi.NgrokTokenListWidget.NgrokTokenEntry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(value = EnvType.CLIENT)
public class NgrokApiScreen extends Screen {
    private ButtonWidget delButton;
    private ButtonWidget joinButton;
    private NgrokTokenListWidget listWidget;
    private NgrokTokenList list;
    private final Screen parent;
    private NgrokTokenEntry selectedEntry = null;

    public NgrokApiScreen(Screen parent) {
        super(Text.of("Ngrok Api"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.list = new NgrokTokenList();

        this.joinButton = this.addDrawableChild(new ButtonWidget(this.width / 2 - 110,
                this.height - 55,
                100, 20,
                Text.of("Join"), button -> this.join()));

        this.delButton = this.addDrawableChild(new ButtonWidget(this.width / 2 + 10, this.height - 55,
                100, 20,
                Text.of("Delete"), button -> this.delete()));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 140, this.height - 30,
                80, 20,
                Text.of("Add"), button -> this.add()));

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 40, this.height - 30,
                80, 20,
                Text.of("Refresh"), button -> this.refresh()));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 60,
                this.height - 30,
                80, 20,
                Text.of("Cancel"), button -> this.close()));

        this.listWidget = new NgrokTokenListWidget(client, width, height - 120, 40, height
                - 60, 30);
        this.addSelectableChild(this.listWidget);
        this.repopulateWidgetEnters();
        this.resetSelection();
    }

    private void add() {
        this.client.setScreen(new AddNgrokTokenScreen(this));
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
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

    public void join() {
        NgrokAddress adr = this.selectedEntry.getAddress();
        if (adr.canConnect()) {
            ServerAddress sa = new ServerAddress(adr.getHost(), adr.getPort());
            ConnectScreen.connect(this, this.client, sa, null);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        this.listWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
