package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server implements Runnable{
    int port,count=0;
    ServerSocket serverSock;
    Socket tSock;
    ServerThread[] thrs;
    public Server(int portNum) {
        port=portNum;
        thrs=new ServerThread[100];
        try {
            serverSock=new ServerSocket(port);
            System.out.println("server started");
            new Thread(this).start();
        }catch(IOException e) {
            e.printStackTrace();
        }

    }
    public void chatCast(String id,String msg) {
        broadcast(id+" : "+msg);
        //System.out.println("Server : "+msg);
    }
    public void broadcast(String msg) {
        for(int i=0;i<count;i++) {
            thrs[i].o.println(msg);
            thrs[i].o.flush();
        }
    }
    public synchronized void cut(InetAddress input) {
        boolean t=false;
        for(int i=0;i<count;i++) {
            if(thrs[i].ip.equals(input)) {
                t=true;
            }
            if(t) {
                thrs[i]=thrs[i+1];
            }else continue;
        }
        count-=1;
    }
    @Override
    public void run() {
        while(true) {
            try {
                tSock=serverSock.accept();
                //System.out.println(tSock.getInetAddress()+" connected");
                thrs[count]=new ServerThread(tSock,this);
                new Thread(thrs[count]).start();
                count+=1;
                broadcast("Server : "+thrs[count-1].id+" joined channel.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
class ServerThread implements Runnable{
    Socket sock;
    BufferedReader i;
    PrintWriter o;
    String id;
    InetAddress ip;
    Server server;
    public ServerThread(Socket sock, Server server) {
        this.server=server;
        this.sock=sock;
        try {
            i=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            o=new PrintWriter(sock.getOutputStream());
            id=i.readLine();
            ip=sock.getInetAddress();
            //System.out.println(ip+" connected : "+id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void broadcast(String msg) {
        server.broadcast(msg);
    }
    @Override
    public void run() {
        String msg;
        try {
            while(true) {
                msg=i.readLine();
                if(msg.equals("\\q")) {
                    server.cut(ip);
                    break;
                }else if(msg!=null){
                    server.chatCast(id,msg);
                }
            }
        }catch(Exception e) {
            broadcast(id+" exit channel.");
            //System.out.println(ip+" disconnected : "+id);
            try {
            if(sock!=null) {
                i.close();
                o.close();
                sock.close();
            }
            server.cut(ip);
            }catch(IOException ej) {}
        }
    }

}