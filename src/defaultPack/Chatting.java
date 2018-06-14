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
        System.out.println("닉네임을 입력하세요.");
        String nick=scan.nextLine();
        System.out.println("채팅을 열려면 h,참여할려면 j를 입력하세요.");
        String input=scan.nextLine();
        if(input.equals("h")){
            System.out.println("포트번호를 입력하세요.");
            int port=scan.nextInt();
                chat.server=new Server(port);
                chat.client=new Client(nick,"127.0.0.1",port);
        }else if(input.equals("j")) {
            System.out.println("아이피를 입력하세요.");
            String ip=scan.nextLine();
            System.out.println("포트번호를 입력하세요.");
            int port=scan.nextInt();
            chat.client=new Client(nick,ip,port);
        }
        while(true) {
            chat.client.writeLine(scan.nextLine());
        }
    }
}

