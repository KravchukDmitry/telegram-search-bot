import java.util.ArrayList;

public class ProxyData {
    private ArrayList<Proxy> data;
    private int count;

    public ArrayList<Proxy> getProxies() {
        return data;
    }

    public void setProxies(ArrayList<Proxy> proxies) {
        this.data = proxies;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
