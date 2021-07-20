package me.aaronakhtar.jaguar.objects;

public class JaguarProxy {

    private String host;
    private int port;

    public JaguarProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
