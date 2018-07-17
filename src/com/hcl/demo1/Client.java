package com.hcl.demo1;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost",9999);
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream dis = new DataInputStream(client.getInputStream());

        while (true){
            String info = console.readLine();
            dos.writeUTF(info);
            dos.flush();

            String msg = dis.readUTF();
            System.out.println(msg);
        }

    }
}
