package org.odhs.happydinner.util;

import java.net.InetSocketAddress;
import java.net.Socket;

public class NetManager {
    public static boolean isConnect() {
        Socket socket = new Socket();
        InetSocketAddress address = new InetSocketAddress("1.1.1.1", 80);

        try {
            socket.connect(address, 1000);
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
