package com.jaanonim.ngrokapi;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Util;

import java.io.File;

public class NgrokTokenList {

    public static final Logger LOGGER = LoggerFactory.getLogger("ngrokapi");

    private ArrayList<NgrokToken> list;

    public ArrayList<NgrokToken> getList() {
        return list;
    }

    private MinecraftClient client;

    public NgrokTokenList() {
        this.list = new ArrayList<NgrokToken>();
        this.client = MinecraftClient.getInstance();
        this.loadFile();
    }

    public void loadFile() {
        try {
            NbtCompound nbtCompound = NbtIo.read(new File(this.client.runDirectory, "ngrok.dat"));
            if (nbtCompound == null) {
                return;
            }
            NbtList nbtList = nbtCompound.getList("ngrok", NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                NgrokToken info = NgrokToken.fromNbt(nbtCompound2);
                this.list.add(info);
            }
        } catch (Exception exception) {
            LOGGER.error("Couldn't load server list", exception);
        }
    }

    public void saveFile() {
        try {
            NbtCompound nbtCompound;
            NbtList nbtList = new NbtList();
            for (NgrokToken serverInfo : this.list) {
                nbtCompound = serverInfo.toNbt();
                nbtList.add(nbtCompound);
            }

            NbtCompound nbtCompound2 = new NbtCompound();
            nbtCompound2.put("ngrok", nbtList);
            File file = File.createTempFile("ngrok", ".dat", this.client.runDirectory);
            NbtIo.write(nbtCompound2, file);
            File file2 = new File(this.client.runDirectory, "ngrok.dat_old");
            File file3 = new File(this.client.runDirectory, "ngrok.dat");
            Util.backupAndReplace(file3, file, file2);
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
