package com.feup.pesi.businesscard.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Utils {


    public static final int REQUEST_CODE_SETTINGS_ACTIVITY    = 1;
    public static final int REQUEST_CODE_ADD_ACTIVITY         = 2;
    public static final int REQUEST_CODE_DELETE_ACTIVITY      = 3;
    public static final int REQUEST_CODE_EDIT_ACTIVITY        = 4;



    public static final String MODE                         = "MODE";

    public static final int ACTIVITY_MODE_NOTHING           = 0;
    public static final int ACTIVITY_MODE_ADDING            = 1;
    public static final int ACTIVITY_MODE_DELETING          = 2;
    public static final int ACTIVITY_MODE_EDITING           = 3;
    public static final int ACTIVITY_MODE_DETAILS           = 4;

    public static final int ID_DEFAULT_VALUE                = -1;

    public static final String ID                           = "ID";


    public static final String OPERATION_ADD_SUCESSS        = "Inserido com sucesso";
    public static final String OPERATION_UPDATE_SUCESSS     = "Alterado com sucesso";
    public static final String OPERATION_DELETE_SUCESSS     = "Eliminado com sucesso";
    public static final String OPERATION_NO_DATA            = "Sem dados";
    public static final String UNKNOWN_MODE                 = "Modo desconhecido";



    public static final String UNKNOWN_ACTION               = "Acção desconhecida";



    public static final String MYSHPREFS                       = "MySharedPreferences";

    public static final String IP1 = "IP1";
    public static final String IP2 = "IP2";
    public static final String IP3 = "IP3";
    public static final String IP4 = "IP4";
    public static final String PORT = "PORT";

    private static final int IP1_DEFAULT_VALUE = 192;
    private static final int IP2_DEFAULT_VALUE = 168;
    private static final int IP3_DEFAULT_VALUE = 1;
    private static final int IP4_DEFAULT_VALUE = 5;
    private static final int PORT_DEFAULT_VALUE = 8080;

    public static String getWSAddress(Context context) {
        SharedPreferences settings = getSharedPreferences(context);
        int ip1 = settings.getInt(IP1, IP1_DEFAULT_VALUE);
        int ip2 = settings.getInt(IP2, IP2_DEFAULT_VALUE);
        int ip3 = settings.getInt(IP3, IP3_DEFAULT_VALUE);
        int ip4 = settings.getInt(IP4, IP4_DEFAULT_VALUE);
        int port = settings.getInt(PORT, PORT_DEFAULT_VALUE);

        return "http://" + ip1 + "." + ip2 + "." + ip3 + "." + ip4 + ":" + port +"/api";
    }

    public static void setWSAddress(Context context, int ip1, int ip2, int ip3, int ip4, int port) {
        SharedPreferences settings = getSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(IP1, ip1);
        editor.putInt(IP2, ip2);
        editor.putInt(IP3, ip3);
        editor.putInt(IP4, ip4);
        editor.putInt(PORT, port);
        editor.commit();
    }

    public static int getIPNumber(Context context, String IP) {
        SharedPreferences settings = getSharedPreferences(context);
        int ip = 0;
        switch(IP) {
            case IP1:
                ip = settings.getInt(IP1, IP1_DEFAULT_VALUE);
                break;
            case IP2:
                ip = settings.getInt(IP2, IP2_DEFAULT_VALUE);
                break;
            case IP3:
                ip = settings.getInt(IP3, IP3_DEFAULT_VALUE);
                break;
            case IP4:
                ip = settings.getInt(IP4, IP4_DEFAULT_VALUE);
                break;
            default:
                ip = 0;
        }
        return ip;
    }

    public static String getIPAddress(Context context) {
        SharedPreferences settings = getSharedPreferences(context);
        int ip1 = settings.getInt(IP1, IP1_DEFAULT_VALUE);
        int ip2 = settings.getInt(IP2, IP2_DEFAULT_VALUE);
        int ip3 = settings.getInt(IP3, IP3_DEFAULT_VALUE);
        int ip4 = settings.getInt(IP4, IP4_DEFAULT_VALUE);

        return ip1 + "." + ip2 + "." + ip3 + "." + ip4;
    }
    public static int getPortNumber(Context context) {
        SharedPreferences settings = getSharedPreferences(context);
        int port = settings.getInt(PORT, PORT_DEFAULT_VALUE);
        return port;
    }
    private static SharedPreferences getSharedPreferences(Context context){
        SharedPreferences settings = context.getSharedPreferences(MYSHPREFS, Context.MODE_PRIVATE);
        return settings;

    }

    public String getIpAddress(Context context) {
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ip;
    }


}
