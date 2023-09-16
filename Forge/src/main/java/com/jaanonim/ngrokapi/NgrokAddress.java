package com.jaanonim.ngrokapi;

public class NgrokAddress {
    String host;
    int port;
    String error;

    public static NgrokAddress createAddress(String host, int port) {
        return new NgrokAddress(host, port);
    }

    public static NgrokAddress createError(String error) {
        return new NgrokAddress(error);
    }

    public static NgrokAddress createOffline() {
        return new NgrokAddress(null, 0);
    }

    private NgrokAddress(String host, int port) {
        this.host = host;
        this.port = port;
        this.error = null;
    }

    private NgrokAddress(String error) {
        this.error = error;
        this.host = null;
        this.port = 0;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getError() {
        return error;
    }

    public boolean canConnect() {
        return host != null && error == null;
    }

    public String getFull() {
        if (error != null) {
            return error;
        }
        if (host == null) {
            return "Offline";
        }
        return host + ":" + port;
    }

}
