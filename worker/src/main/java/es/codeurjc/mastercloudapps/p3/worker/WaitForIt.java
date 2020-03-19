package es.codeurjc.mastercloudapps.p3.worker;

import java.nio.channels.SocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;

public class WaitForIt {

    public static void exec(String hostName, int port) {

        System.out.println("WaitForIt: waiting for "+hostName+":"+port+" without a timeout");
        
        int waitTime = 0;

        while (true) {
            try {
                SocketChannel socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(hostName, port));
                socketChannel.close();
                
                System.out.println("WaitForIt: "+ hostName+" is available after "+waitTime+" seconds");

                return;

            } catch (IOException e) {

                waitTime++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e2) { }
            }
        }

    }
}