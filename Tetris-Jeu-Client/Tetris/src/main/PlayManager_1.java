package main;

import mino_1.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager_1 {
    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMIND_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block>  staticBlocks = new ArrayList<>();
    // Others
    public static int dropInterval = 60;
    boolean gameOver;
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    // Score
    int level = 1;
    int lines;
    int score;
    public PlayManager_1(){
        // Main Play Area Frame
        left_x = 40;
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMIND_X = right_x + 90;
        NEXTMINO_Y = top_y + 500;

        // Set the starting Mino
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMIND_X, NEXTMINO_Y);
    }
    private Mino pickMino(){
        // Pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);
        switch (i){
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new MIno_T();break;
            case 5: mino = new Mino_Z1();break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino;
    }
    public void update() {
        // check if this currentMino is active
        if (currentMino.active == false){
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            currentMino .deactivating = false;
            if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                gameOver = true;

            }
            // replace the currentMino with the nextMino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMIND_X, NEXTMINO_Y);

            // when a mino becomes inactive, check if lines (s) can be deleted
            checkDelete();

        }else {
            currentMino.update();
        }
    }
    private void checkDelete(){
        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while (x < right_x && y < bottom_y){

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    // increase the count if there is a static block
                    blockCount++;
                }
            }
            x+= Block.SIZE;
            if (x == right_x){
                if (blockCount == 12){
                    effectCounterOn = true;
                    effectY.add(y);
                    for (int i =  staticBlocks.size()-1; i > -1; i--) {
                        if (staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }
                    lineCount++;
                    lines++;
                    if (lines % 10 == 0 && dropInterval > 1){
                        level++;
                        if (dropInterval > 10){
                            dropInterval -= 10;
                        }else {
                            dropInterval -= 1;
                        }
                    }
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }
                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }
        if (lineCount > 0){

            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }
    public void draw(Graphics2D g2){
        // Draw Play Area Frame
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDTH+0, HEIGHT+8);

        // Draw next Mino Frame
        int x = right_x + 30;
        int y = bottom_y - 180;
        g2.drawRect(x, y, 150, 150);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+40, y+40);

        //Draw Score Frame
        g2.drawRect(x, top_y, 150, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y); y+= 50;
        g2.drawString("LINES: " + lines, x, y); y+= 50;
        g2.drawString("SCORE: " + score, x, y);

        if (currentMino != null){
            currentMino.draw(g2);
        }

        // Draw the nextMino
        nextMino.draw(g2);

        // Draw Static Blocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //Draw Effect
        if (effectCounterOn){
            effectCounter++;
            g2.setColor(Color.red);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter == 10){
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }
        // Draw Pause or Game Over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver){
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
        else if (KeyHandler_1.pausePressed){
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}
