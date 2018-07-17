package com.hcl.demo3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable{
    private BufferedReader console;
    private DataOutputStream dos;
    private boolean isRunning = true;

    public Send(){
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public Send(Socket client){
        this();
        try {
            dos = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dos,console);
        }
    }

    private String getConsole() {//获取字符串
        try {
            return console.readLine();
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dos, console);
        }
        return "";
    }

    private void send(){//发送命令
        String msg = this.getConsole();
        try {
            if(msg != null && !msg.equals("")) {
                dos.writeUTF(msg);
                dos.flush();
            }
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dos,console);
        }
    }


    @Override
    public void run() {
        while (isRunning){//主方法
            send();
        }
    }
}
