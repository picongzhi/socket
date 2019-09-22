package com.pcz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author picongzhi
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("服务器开始监听...");
        System.out.println("服务器IP: " + serverSocket.getInetAddress() + " 端口: " + serverSocket.getLocalPort());

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket);
            clientHandler.start();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag;

        ClientHandler(Socket socket) {
            this.socket = socket;
            this.flag = true;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("客户端连接成功，客户端IP: " + socket.getInetAddress() + " 端口: " + socket.getPort());

            try {
                BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream socketPrintStream = new PrintStream(socket.getOutputStream());

                do {
                    String input = socketBufferedReader.readLine();
                    if ("bye".equalsIgnoreCase(input)) {
                        flag = true;
                        socketPrintStream.println("bye");
                    } else {
                        System.out.println(input);
                        socketPrintStream.println("回送: " + input.length());
                    }
                } while (flag);

                socketBufferedReader.close();
                socketPrintStream.close();
            } catch (Exception e) {
                System.out.println("连接发生异常断开");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("客户端已退出，客户端IP: " + socket.getInetAddress() + " 端口: " + socket.getPort());
        }
    }
}
