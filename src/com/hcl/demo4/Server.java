package com.hcl.demo4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/*
    实现昵称功能以及系统提示功能
 */

public class Server {
    private List<Channel> all = new ArrayList<Channel>();

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    private void start() throws IOException {
        ServerSocket server = new ServerSocket(9999);
        while (true){
            Socket client = server.accept();
            Channel channel = new Channel(client);
            all.add(channel);
            new Thread(channel).start();
        }
    }

    private class Channel implements Runnable{

        private DataInputStream dis;
        private DataOutputStream dos;
        private boolean isRunning = true;
        private String name;

        private Channel(Socket client){
            try {
                dis = new DataInputStream(client.getInputStream());
                dos = new DataOutputStream(client.getOutputStream());
                this.name = dis.readUTF();
                this.send("欢迎您，"+this.name);
                sendNameToOthers(this.name);
            } catch (IOException e) {
//                e.printStackTrace();
                CloseUtil.closeAll(dis,dos);
                isRunning = false;
            }
        }

        private String receive(){
            String msg = "";
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
//                e.printStackTrace();
                CloseUtil.closeAll(dis);
                isRunning = false;
            }
            return msg;
        }

        private void send(String msg){
            try {
                dos.writeUTF(msg);
                dos.flush();
            } catch (IOException e) {
//                e.printStackTrace();
                CloseUtil.closeAll(dos);
                isRunning = false;
            }
        }

        private void sendOthers(String msg){
            for(Channel other:all){
                if(other == this){
                    continue;
                }
                other.send(other.name+"说： "+msg);
            }
        }

        private void sendNameToOthers(String name){
            for(Channel other:all){
                if(other == this){
                    continue;
                }
                other.send(other.name+"进入了聊天室！！");
            }
        }

        @Override
        public void run() {
            while (isRunning){
                sendOthers(receive());
            }

        }
    }

}
