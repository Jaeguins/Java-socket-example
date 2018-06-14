package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;

public class Server implements Runnable{
    int port,count=0;
    ServerSocket serverSock;
    Socket tSock;
    Vector<ServerThread> thrs;
    public Server(int portNum) {
        port=portNum;
        thrs=new Vector<ServerThread>();
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
        Enumeration en = thrs.elements();
        while (en.hasMoreElements()) {
            ServerThread c = (ServerThread) en.nextElement();
            c.o.println(msg);
            c.o.flush();
        }
    }
    public synchronized void cut(String input) {
        boolean t=false;
        Enumeration en = thrs.elements();
        while (en.hasMoreElements()) {
            ServerThread c = (ServerThread) en.nextElement();
            if(c.id.equals(input)) {
                thrs.remove(c);
                count-=1;
                return;
            }
        }

    }
    @Override
    public void run() {
        while(true) {
            try {
                tSock=serverSock.accept();
                //System.out.println(tSock.getInetAddress()+" connected");
                thrs.add(new ServerThread(tSock,this));
                new Thread(thrs.get(count)).start();
                count+=1;
                broadcast("Server : "+thrs.get(count-1)+" joined channel.");
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
    public String toString() {
        return id;
    }
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
        while(true) {
            try {
                msg=i.readLine();
                if(msg.equals("/q")) {
                    broadcast(id+" exit channel.");
                    server.cut(id);
                    break;
                }else if(msg!=null){
                    server.chatCast(id,msg);
                }
            }
            catch(Exception e) {
                broadcast(id+" exit channel.");
                //System.out.println(ip+" disconnected : "+id);
                server.cut(id);
                break;
            }
        }
    }

}