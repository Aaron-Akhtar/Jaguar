package me.aaronakhtar.jaguar;

import me.aaronakhtar.jaguar.objects.JaguarCombo;
import me.aaronakhtar.jaguar.objects.JaguarProxy;
import me.aaronakhtar.jaguar.threads.JaguarRandomThread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Jaguar {

    public static final List<JaguarProxy> proxy_list = new ArrayList<>();
    public static final List<JaguarCombo> combo_list = new ArrayList<>();

    public static File results_file = null;

    public static int target_port;
    public static int max_concurrent_sessions;
    public static int uni_timeout;

    public static void main(String[] args) {
                                                // threads target_port proxy_list max_cons
        if(args.length != 7){
            System.out.println("Incorrect Arguments: 'java -jar jaguar.jar %threads% %target_port% %proxy_list% %max_cons% %combo_list% %universal_timeout% %res_file%'");
            return;
        }

        try{
            final int threads = Integer.parseInt(args[0]);
            target_port = Integer.parseInt(args[1]);
            max_concurrent_sessions = Integer.parseInt(args[3]);
            uni_timeout = Integer.parseInt(args[5]);
            final File
                    proxy_file = new File(args[2]),
                    combo_list = new File(args[4]);
            results_file = new File(args[6]);
            JaguarUtilFunctions.getProxyList(proxy_file);
            JaguarUtilFunctions.getComboList(combo_list);

            System.out.println("Developed by Aaron Akhtar...");
            System.out.println();

            for (int x = 0; x < threads; x++){
                new Thread(new JaguarRandomThread()).start();
            }

            while(true){
                Thread.sleep(10000);
                System.out.println("Current Active Sessions: " + JaguarRandomThread.currentSessions);
                System.out.println("Number of Tried Hosts: " + JaguarRandomThread.tried.size());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
