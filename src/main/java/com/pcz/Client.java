package com.pcz;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author picongzhi
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 8888), 3000);

        System.out.println("已发起连接...");
        System.out.println("客户端IP: " + socket.getLocalAddress() + " 客户端端口: " + socket.getLocalPort());
        System.out.println("服务器IP: " + socket.getInetAddress() + " 服务器端口: " + socket.getPort());

        try {
            todo(socket);
        } catch (Exception e) {
            System.out.println("异常关闭");
        }

        socket.close();
        System.out.println("客户端退出...");
    }

    private static void todo(Socket socket) throws IOException {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
        PrintStream socketPrintStream = new PrintStream(socket.getOutputStream());
        BufferedReader socketInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        boolean flag = true;
        do {
            String input = inputReader.readLine();
            socketPrintStream.println(input);

            String echo = socketInputReader.readLine();
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        inputReader.close();
        socketInputReader.close();
        socketPrintStream.close();
    }
}
