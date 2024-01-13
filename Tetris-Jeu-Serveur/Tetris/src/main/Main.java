package main;
import mino_1.Mino;
import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        JFrame window = new JFrame("Player 1");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // Ajouter GamePanel a la fenetre
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();

        Server server = new Server();
        new Thread(server::readMessage).start();

        Thread tw = new Thread(() -> {
            while (true){
            try {

                if (Mino.rotat == true){
                    server.sendMessage("rotation");
                }
                if (Mino.left == true){
                    server.sendMessage("left");
                }
                if (Mino.right == true){
                    server.sendMessage("right");
                }
                if (Mino.dowm == true){
                    server.sendMessage("dowm");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            }
        });
        tw.start();

    }
}