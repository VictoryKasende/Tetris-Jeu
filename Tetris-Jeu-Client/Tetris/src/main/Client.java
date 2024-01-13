package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter w;
    private BufferedReader r;
    public static String messageLue;
    private String messageRecue;

    public Client() throws IOException {
        socket = new Socket("localhost", 2000);
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
                System.out.println("Service : " + messageLue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();

        // Démarrer le client dans un thread séparé
        new Thread(() -> {
            client.readMessage();
            System.out.println(Client.messageLue);
        }).start();

    }
    /*
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        Scanner sc = new Scanner(System.in);

        Thread tw = new Thread(() -> {
            while (true) {
                String m = sc.nextLine();
                try {
                    client.sendMessage(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread tr = new Thread(client::readMessage);

        tw.start();
        tr.start();
    }*/
}
