package com.hcl.demo2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
    封装客户端的发送接收线程，实现异步通信
 */

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        Socket client = server.accept();

        DataInputStream dis = new DataInputStream(client.getInputStream());
        String msg = dis.readUTF();

        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeUTF("服务器-->"+msg);
        dos.flush();
        dos.close();
    }

}
