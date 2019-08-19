package network;

import java.io.IOException;
import java.net.InetAddress;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Proxy(String host, int port) {
        this.ip = host;
        this.port = port;
    }

    public boolean isReachable() {
        boolean isReachable = false;
        try {
            isReachable = InetAddress.getByName(ip).isReachable(1000);
        } catch (IOException ioe) {
            System.out.println("host validation error");
            ioe.printStackTrace();
        }
        return isReachable;
    }

    public boolean isWorks() {
        return isWorks;
    }

    public void setWorks(boolean works) {
        isWorks = works;
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
