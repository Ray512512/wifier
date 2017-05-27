package com.traffic.wifiapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.traffic.wifiapp.common.WifiApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by ray on 2015/12/15.
 */
public class NetworkTools {
   /** 没有网络 */
   public static final int NETWORKTYPE_INVALID = 0;
   /** wap网络 */
   public static final int NETWORKTYPE_WAP = 1;
   /** 2G网络 */
   public static final int NETWORKTYPE_2G = 2;
   /** 3G和3G以上网络，或统称为快速网络 */
   public static final int NETWORKTYPE_3G = 3;
   /** wifi网络 */
   public static final int NETWORKTYPE_WIFI = 4;

   private static final String TAG="MyWebSocketClient";

   /**
    * 检测网络是否可用
    * @return
    */
   public static boolean isNetworkConnected() {
     /* ConnectivityManager cm = (ConnectivityManager) MyApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo ni = cm.getActiveNetworkInfo();
      return ni != null && ni.isConnectedOrConnecting();*/

      // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
      ConnectivityManager connectivityManager = (ConnectivityManager) WifiApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

      if (connectivityManager == null)
      {
         return false;
      }
      else
      {
         // 获取NetworkInfo对象
         NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

         if (networkInfo != null && networkInfo.length > 0)
         {
            for (int i = 0; i < networkInfo.length; i++)
            {

//               Log.v(TAG, i + "===状态===" + networkInfo[i].getState());
//               Log.v(TAG, i + "===类型===" + networkInfo[i].getTypeName());
               // 判断当前网络状态是否为连接状态
               if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
               {
                  return true;
               }
            }
         }
      }
      return false;
   }

   /**
    * 获取网络状态，wifi,wap,2g,3g.
    *
    * @param context 上下文
    * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},
    * {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
    */
   public static int getNetWorkType(Context context) {
      int mNetWorkType=0;
      ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo networkInfo = manager.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isConnected()) {
         String type = networkInfo.getTypeName();
         if (type.equalsIgnoreCase("WIFI")) {
            mNetWorkType = NETWORKTYPE_WIFI;
         } else if (type.equalsIgnoreCase("MOBILE")) {
            String proxyHost = android.net.Proxy.getDefaultHost();
            mNetWorkType = TextUtils.isEmpty(proxyHost)
                    ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                    : NETWORKTYPE_WAP;
         }
      } else {
         mNetWorkType = NETWORKTYPE_INVALID;
      }
      return mNetWorkType;
   }

private static boolean isFastMobileNetwork(Context context) {
      TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
      switch (telephonyManager.getNetworkType()) {
         case TelephonyManager.NETWORK_TYPE_1xRTT://7
            return false; // ~ 50-100 kbps
         case TelephonyManager.NETWORK_TYPE_CDMA://4
            return false; // ~ 14-64 kbps
         case TelephonyManager.NETWORK_TYPE_EDGE://2
            return false; // ~ 50-100 kbps
         case TelephonyManager.NETWORK_TYPE_EVDO_0://5
            return true; // ~ 400-1000 kbps
         case TelephonyManager.NETWORK_TYPE_EVDO_A://6
            return true; // ~ 600-1400 kbps
         case TelephonyManager.NETWORK_TYPE_GPRS://1
            return false; // ~ 100 kbps
         case TelephonyManager.NETWORK_TYPE_HSDPA://8
            return true; // ~ 2-14 Mbps
         case TelephonyManager.NETWORK_TYPE_HSPA://10
            return true; // ~ 700-1700 kbps
         case TelephonyManager.NETWORK_TYPE_HSUPA://9
            return true; // ~ 1-23 Mbps
         case TelephonyManager.NETWORK_TYPE_UMTS://3
            return true; // ~ 400-7000 kbps
         case TelephonyManager.NETWORK_TYPE_EHRPD://14
            return true; // ~ 1-2 Mbps
         case TelephonyManager.NETWORK_TYPE_EVDO_B://12
            return true; // ~ 5 Mbps
         case TelephonyManager.NETWORK_TYPE_HSPAP://15
            return true; // ~ 10-20 Mbps
         case TelephonyManager.NETWORK_TYPE_IDEN://11
            return false; // ~25 kbps
         case TelephonyManager.NETWORK_TYPE_LTE://13
            return true; // ~ 10+ Mbps
         case TelephonyManager.NETWORK_TYPE_UNKNOWN://0
            return false;
         default:
            return false;
      }
   }



   /**
    * 获取用户当前链接网络 ip
    * */
   public static String getLocalIpAddr(Context context){
      String ip=getWifiIp(context);
      if (TextUtils.isEmpty(ip)) {
         ip=getLocalIpAddress();
      }
      L.v("getLocalIpAddr",ip);
      return ip;
   }


   public static String getWifiIp(Context context){
      String  ip="";
      WifiManager wifiManager = (WifiManager)context. getSystemService(Context.WIFI_SERVICE);
      //判断wifi是否开启
      if (wifiManager.isWifiEnabled()) {
          DhcpInfo d=wifiManager.getDhcpInfo();
          int ipAddress=d.gateway;
          ip = intToIp(ipAddress);
          L.v("getWifiIp",d.toString());
      }
      return ip;
   }
   private static String intToIp(int i) {
      return (i & 0xFF ) + "." +
              ((i >> 8 ) & 0xFF) + "." +
              ((i >> 16 ) & 0xFF) + "." +
              ( i >> 24 & 0xFF) ;
   }

   private static String getLocalIpAddress()
   {
      try
      {
         for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
         {
            NetworkInterface intf = en.nextElement();
            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
            {
               InetAddress inetAddress = enumIpAddr.nextElement();
               if (!inetAddress.isLoopbackAddress())
               {
                  return inetAddress.getHostAddress().toString();
               }
            }
         }
      }
      catch (SocketException ex)
      {
         L.e("WifiPreference IpAddress", ex.toString());
      }
      return null;
   }



}