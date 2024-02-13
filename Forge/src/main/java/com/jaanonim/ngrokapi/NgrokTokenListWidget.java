package com.jaanonim.ngrokapi;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class NgrokTokenListWidget extends ObjectSelectionList<NgrokTokenListWidget.NgrokTokenEntry> {

    public NgrokTokenListWidget(Minecraft client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    public void addElement(NgrokTokenEntry entry) {
        this.addEntry(entry);
    }

    public void clear() {
        this.clearEntries();
    }

    public static class NgrokTokenEntry extends ObjectSelectionList.Entry<NgrokTokenEntry> {
        private NgrokToken entry;
        private NgrokAddress address;
        private Minecraft client;
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
            this.client = Minecraft.getInstance();
            this.address = entry.getAddress();
        }

        @Override
        public void render(PoseStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                int mouseY, boolean hovered, float tickDelta) {
            this.client.font.draw(matrices, Component.nullToEmpty(this.entry.getName()), (float) (x + 32 + 3),
                    (float) (y + 2), 0xFFFFFF);
            String s = this.address.getFull();
            Component t = !s.isEmpty() ? Component.nullToEmpty(s) : Component.nullToEmpty("None");
            this.client.font.draw(matrices, t, (float) (x + 32 + 3),
                    (float) (y + 12), 0xAAAAAA);

        }

        public void refresh() {
            this.address = entry.getAddress();
        }

        @Override
        public Component getNarration() {
            return Component.nullToEmpty(entry.name);
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
