package com.jaanonim.ngrokapi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.text.Text;

public class NgrokTokenListWidget extends AlwaysSelectedEntryListWidget<NgrokTokenListWidget.NgrokTokenEntry> {

    public NgrokTokenListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    public void addElement(NgrokTokenEntry entry) {
        this.addEntry(entry);
    }

    public void clear() {
        this.clearEntries();
    }

    public static class NgrokTokenEntry extends AlwaysSelectedEntryListWidget.Entry<NgrokTokenEntry> {
        private NgrokToken entry;
        private NgrokAddress address;
        private MinecraftClient client;
        private NgrokApiScreen screen;

        public NgrokToken getEntry() {
            return entry;
        }

        public NgrokAddress getAddress() {
            return address;
        }

        public NgrokTokenEntry(NgrokApiScreen screen, NgrokToken entry) {
            this.entry = entry;
            this.screen = screen;
            this.client = MinecraftClient.getInstance();
            this.address = entry.getAddress();
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                int mouseY, boolean hovered, float tickDelta) {
            this.client.textRenderer.draw(matrices, Text.of(this.entry.getName()), (float) (x + 32 + 3),
                    (float) (y + 2), 0xFFFFFF);
            String s = this.address.getFull();
            Text t = !s.isEmpty() ? Text.of(s) : Text.of("None");
            this.client.textRenderer.draw(matrices, t, (float) (x + 32 + 3),
                    (float) (y + 12), 0xAAAAAA);

        }

        public void refresh() {
            this.address = entry.getAddress();
        }

        @Override
        public Text getNarration() {
            throw new UnsupportedOperationException("Unimplemented method 'getNarration'");
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.screen.selected(this);
                return true;
            }
            return false;
        }

    }
}
