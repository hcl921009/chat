package com.hcl.demo3;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost",9999);
        new Thread(new Send(client)).start();
        new Thread(new Receive(client)).start();
    }
}
