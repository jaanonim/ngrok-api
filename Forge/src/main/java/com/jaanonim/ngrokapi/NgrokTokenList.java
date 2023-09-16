package com.jaanonim.ngrokapi;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.Util;
import java.io.File;

public class NgrokTokenList {

    public static final Logger LOGGER = LoggerFactory.getLogger("ngrokapi");

    private ArrayList<NgrokToken> list;

    public ArrayList<NgrokToken> getList() {
        return list;
    }

    private Minecraft client;

    public NgrokTokenList() {
        this.list = new ArrayList<NgrokToken>();
        this.client = Minecraft.getInstance();
        this.loadFile();
    }

    public void loadFile() {
        try {
            CompoundTag nbtCompound = NbtIo.read(new File(this.client.gameDirectory, "ngrok.dat"));
            if (nbtCompound == null) {
                return;
            }
            ListTag nbtList = nbtCompound.getList("ngrok", Tag.TAG_COMPOUND);
            for (int i = 0; i < nbtList.size(); ++i) {
                CompoundTag nbtCompound2 = nbtList.getCompound(i);
                NgrokToken info = NgrokToken.fromNbt(nbtCompound2);
                this.list.add(info);
            }
        } catch (Exception exception) {
            LOGGER.error("Couldn't load server list", exception);
        }
    }

    public void saveFile() {
        try {
            CompoundTag nbtCompound;
            ListTag nbtList = new ListTag();
            for (NgrokToken serverInfo : this.list) {
                nbtCompound = serverInfo.toNbt();
                nbtList.add(nbtCompound);
            }

            CompoundTag nbtCompound2 = new CompoundTag();
            nbtCompound2.put("ngrok", nbtList);
            File file = File.createTempFile("ngrok", ".dat", this.client.gameDirectory);
            NbtIo.write(nbtCompound2, file);
            File file2 = new File(this.client.gameDirectory, "ngrok.dat_old");
            File file3 = new File(this.client.gameDirectory, "ngrok.dat");
            Util.safeReplaceFile(file3, file, file2);
        } catch (Exception exception) {
            LOGGER.error("Couldn't save server list", exception);
        }
    }

    public void add(NgrokToken entry) {
        this.list.add(entry);
        this.saveFile();
    }

    public void remove(NgrokToken entry) {
        this.list.remove(entry);
        this.saveFile();
    }
}
