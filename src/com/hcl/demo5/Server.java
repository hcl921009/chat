package com.hcl.demo5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/*
    实现私聊功能
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
                this.send("欢迎您，"+this.name+"，您可以使用 \"@昵称：\"来私聊其他人噢~！");
                this.sysMessage(this.name+"进入了聊天室！！");
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
            if (msg.startsWith("@") && msg.indexOf("：")>-1){
                String name = msg.substring(1,msg.indexOf("："));
                String content = msg.substring(msg.indexOf("：")+1);
                for(Channel other:all) {
                    if (other.name.equals(name)) {
                        other.send(this.name + "悄悄对你说： " + content);
                    }
                }
            }else{
                    for(Channel other:all){
                        if(other == this){
                            continue;
                        }
                        other.send(this.name+"说： "+msg);
                    }

                }
        }

        private void sysMessage(String msg){
            for(Channel other:all){
                if(other == this){
                    continue;
                }
                other.send(msg);
            }
        }

        @Override
        public void run(){
            while (isRunning){
                sendOthers(receive());
            }
        }

        }
    }
