package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager_1 pm;
    PlayManager_2 pm2;
    public GamePanel(){
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setLayout(null);
        //Implement KeyListener
        this.addKeyListener(new KeyHandler_1());
        this.addKeyListener(new KeyHandler_2());
        this.setFocusable(true);

        pm = new PlayManager_1();
        pm2 = new PlayManager_2();
    }
    public void launchGame(){
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

        //Game loop
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null){
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update(){
        if (KeyHandler_1.pausePressed == false && pm.gameOver == false){
            pm.update();
        }
        if (KeyHandler_2.pausePressed == false && pm2.gameOver == false){
            pm2.update();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        Graphics2D g3 = (Graphics2D) g;

        pm2.draw(g2);
        pm.draw(g2);
    }
}
