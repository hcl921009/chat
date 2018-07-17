package com.hcl.demo2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable{
    private DataInputStream dis;
    private boolean isRunning = true;
    private String msg = "";

    public Receive(){

    }

    public Receive(Socket client){
        try {
            dis = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dis);
        }

    }

    private String receive(){
        try {
            msg = dis.readUTF();
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dis);
        }
        return msg;
    }

    @Override
    public void run() {
        while (isRunning){
            if(msg.equals("")){
                System.out.println("未收到信息");
            }
            System.out.println(receive());
        }

    }
}
