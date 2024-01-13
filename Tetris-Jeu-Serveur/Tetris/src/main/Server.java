package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serveur;
    private Socket socket;
    private BufferedWriter w;
    private BufferedReader r;
    public static String messageLue;
    private String messageRecue;

    public Server() throws IOException {
        serveur = new ServerSocket(2000);
        socket = serveur.accept();
        System.out.println("Client connecté");
        w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) throws IOException {
        w.write(message);
        w.newLine();
        w.flush();
        messageRecue = message;
    }

    public void readMessage() {
        try {
            while ((messageLue = r.readLine()) != null) {
                System.out.println("Victory : " + messageLue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        new Thread(server::readMessage).start();

        new Thread(() -> {
            try {
                server.sendMessage("left");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        Scanner sc = new Scanner(System.in);
        Thread tw = new Thread(() -> {
            while (true) {
                String m = sc.nextLine();
                try {
                    server.sendMessage(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tw.start();
    }

}
