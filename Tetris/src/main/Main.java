package main;

import mino_2.Mino;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame("Player 2");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Ajouter GamePanel a la fenetre
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();

        Client client = new Client();
        new Thread(() -> {
            client.readMessage();
            System.out.println(Client.messageLue);
        }).start();

        Thread tw = new Thread(() -> {
            while (true){
                try {

                    if (Mino.rotat == true){
                        client.sendMessage("rotation");
                    }
                    if (Mino.left == true){
                        client.sendMessage("left");
                    }
                    if (Mino.right == true){
                        client.sendMessage("right");
                    }
                    if (Mino.dowm == true){
                        client.sendMessage("dowm");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tw.start();
    }
}