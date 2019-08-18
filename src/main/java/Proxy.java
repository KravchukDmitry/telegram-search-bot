import java.io.IOException;
import java.net.InetAddress;

public class Proxy {
    protected String host;
    protected int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean isReachable() {
        boolean isReachable = false;
        try {
            isReachable = InetAddress.getByName(host).isReachable(1000);
        } catch (IOException ioe) {
            System.out.println("host validation error");
            ioe.printStackTrace();
        }
        return isReachable;
    }

    @Override
    public String toString() {
        return "Proxy host : " + host + " port : " + port;
    }
}
