package network;

import lombok.Data;

@Data
public class Proxy {
    private String ipPort;
    private String ip;
    private int port;
    private String country;
    private String last_checked;
    private String proxy_level;
    private String type;
    private int speed;
    private Support support;
    private boolean isWorks;

    public Proxy(String host, int port) {
        this.ip = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "network.Proxy host : " + ip + " port : " + port;
    }

    private class Support {
        int https;
        int get;
        int post;
        int cookies;
        int referer;
        int user_agent;
        int google;

    }
}
