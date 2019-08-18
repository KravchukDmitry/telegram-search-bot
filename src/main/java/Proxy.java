import java.io.IOException;
import java.net.InetAddress;

public class Proxy {
    protected String ipPort;
    protected String ip;
    protected int port;
    protected String country;
    protected String last_checked;
    protected String proxy_level;
    protected String type;
    protected int speed;
    protected Support support;

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

    @Override
    public String toString() {
        return "Proxy host : " + ip + " port : " + port;
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
