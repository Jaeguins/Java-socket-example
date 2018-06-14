package defaultPack;

import java.io.*;
import java.net.*;
import client.*;
import server.*;
import java.util.Scanner;
public class Chatting {
    Server server;
    Client client;
    public static void main(String[] args) {
        Chatting chat=new Chatting();
        Scanner scan=new Scanner(System.in);
        System.out.print("Names : ");
        String nick=scan.nextLine();
        System.out.println("Type h to host, j to join others");
        String input=scan.nextLine();
        if(input.equals("h")){
            System.out.print("Port number : ");
            int port=scan.nextInt();
                chat.server=new Server(port);
                chat.client=new Client(nick,"localhost",port);
        }else if(input.equals("j")) {
            System.out.print("IP : ");
            String ip=scan.nextLine();
            System.out.print("Port number : ");
            int port=scan.nextInt();
            chat.client=new Client(nick,ip,port);
        }
        while(true) {
            chat.client.writeLine(scan.nextLine());
        }
    }
}

