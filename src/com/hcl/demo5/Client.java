package com.hcl.demo5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //新用户接入，输入昵称
        System.out.println("请输入您的昵称：");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name = br.readLine();
        if(name.equals("")){
            return;
        }

        Socket client = new Socket("localhost",9999);
        new Thread(new Send(client,name)).start();
        new Thread(new Receive(client)).start();
    }
}
