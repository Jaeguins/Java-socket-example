package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client implements Runnable{
    Socket sock;
    BufferedReader i;
    PrintWriter o;
    String id;
    InetAddress ip;
    public Client(String name,String ip,int port) {
        this.id=name;
        try {
            sock=new Socket(ip,port);
            System.out.println("connect to "+ip);
            i=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            o=new PrintWriter(sock.getOutputStream());
            o.println(name);
            o.flush();
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeLine(String msg) {
        //System.out.println("Client : "+msg);
        if(!msg.equals("")) {
            o.println(msg);
            o.flush();
        }
    }
    @Override
    public void run() {
        while(true)
            try {
                System.out.println(i.readLine());
            } catch (IOException e) {
                System.out.println("host connection lost.");
                break;
            }
    }
}