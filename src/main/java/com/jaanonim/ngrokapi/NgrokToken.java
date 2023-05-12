package com.jaanonim.ngrokapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.nbt.NbtCompound;

public class NgrokToken {
    String name;
    String token;

    public NgrokToken(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public NgrokAddress getAddress() {
        String url = "https://api.ngrok.com/endpoints";
        HttpURLConnection con;

        try {
            con = (HttpURLConnection) new URL(url).openConnection();

            con.setDoOutput(true);
            con.setRequestProperty("Ngrok-Version", "2");
            con.setRequestProperty("Authorization", "Bearer " + this.token);

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            con.disconnect();

            String response = content.toString();

            // TODO: I know that it's deprecated but don't care enough to fix it.
            JsonParser parser = new JsonParser();
            JsonObject rootObj = parser.parse(response).getAsJsonObject();
            JsonArray arr = rootObj.getAsJsonArray("endpoints");

            if (arr.size() > 0) {
                var hp = arr.get(0).getAsJsonObject().get("hostport").getAsString().split("\\:");
                return NgrokAddress.createAddress(hp[0], Integer.parseInt(hp[1]));
            } else {
                return NgrokAddress.createOffline();
            }
        } catch (Exception e) {
            NgrokApiMod.LOGGER.error(e.toString());
            NgrokAddress.createError(e.toString());
        }
        return NgrokAddress.createOffline();
    }

    public NbtCompound toNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString("name", this.name);
        nbtCompound.putString("token", this.token);
        return nbtCompound;
    }

    public static NgrokToken fromNbt(NbtCompound c) {
        return new NgrokToken(c.getString("name"), c.getString("token"));
    }
}
