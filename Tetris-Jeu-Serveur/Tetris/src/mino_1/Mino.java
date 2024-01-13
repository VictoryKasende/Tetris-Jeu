package mino_1;

import main.KeyHandler_1;
import main.PlayManager_1;
import main.Server;

import java.awt.*;
import java.io.IOException;

public class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1; // There are 4 directions (1/2/3/4)
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    public static boolean rotat = false;
    public static boolean left = false;
    public static boolean right = false;
    public static boolean dowm = false;
    int deactivateCounter = 0;
    public void create(Color c){
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }
    public void setXY(int x, int y){

    }
    public void update(){

        if (deactivating){
            deactivating();
        }
        // Move the mino
        if (KeyHandler_1.upPressed){
            Mino.rotat = !Mino.rotat;
            switch (direction){
                case 1: getDirection2();break;
                case 2: getDirection3();break;
                case 3: getDirection4();break;
                case 4: getDirection1();break;
            }
            KeyHandler_1.upPressed = false;
        }
        checkMovementCollision();
        if (KeyHandler_1.downPressed){
            dowm = !dowm;
            if (bottomCollision == false){
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

                //When moved down, reset the autoDropCounter
                autoDropCounter = 0;
            }
            KeyHandler_1.downPressed = false;
        }
        if (KeyHandler_1.leftPressed){
            left = !left;
            if (leftCollision == false){
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            KeyHandler_1.leftPressed = false;
        }
        if (KeyHandler_1.rightPressed){
            right = !right;
            if(rightCollision == false){
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }
            KeyHandler_1.rightPressed = false;

        }
        if (bottomCollision){
            deactivating = true;
        }
        else {
            autoDropCounter++;
            if (autoDropCounter == PlayManager_1.dropInterval) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }
        }
    }

    private void deactivating() {
        deactivateCounter++;
        // Wait 45 frames unitl deactivate
        if (deactivateCounter == 45){
            deactivateCounter = 0;
            checkMovementCollision();

            // if the bottom is still hitting after 45 frames, deactivate the mino
            if (bottomCollision){
                active = false;
            }
        }
    }

    public void updateXY(int direction){

        checkRotationCollision();
        if (leftCollision == false && rightCollision == false && bottomCollision == false){
            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
      }
    public void getDirection1(){}
    public void getDirection2(){}
    public void getDirection3(){}
    public void getDirection4(){}
    public void checkMovementCollision(){
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        // check static block collision
        checkStaticBlockCollision();

        // check frame collision
        // left wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager_1.left_x){
                leftCollision = true;
            }
        }
        //Right wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == PlayManager_1.right_x){
                rightCollision = true;
            }
        }
        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == PlayManager_1.bottom_y){
                bottomCollision = true;
            }
        }
    }
    public void checkRotationCollision(){
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;
        // check static block collision
        checkStaticBlockCollision();

        // check frame collision
        // left wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager_1.left_x){
                leftCollision = true;
            }
        }
        //Right wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager_1.right_x){
                rightCollision = true;
            }
        }
        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager_1.bottom_y){
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision(){
        for (int i = 0; i < PlayManager_1.staticBlocks.size(); i++) {
            int targetX = PlayManager_1.staticBlocks.get(i).x;
            int targety = PlayManager_1.staticBlocks.get(i).y;

            //check down
            for (int j = 0; j < b.length; j++) {
                if (b[j].y + Block.SIZE == targety && b[j].x == targetX){
                    bottomCollision = true;
                }
            }
            //check left
            for (int j = 0; j < b.length; j++) {
                if (b[j].x - Block.SIZE == targetX && b[j].y == targety){
                    leftCollision = true;
                }
            }
            for (int j = 0; j < b.length; j++) {
                if (b[j].x - Block.SIZE == targetX && b[j].y == targety){
                    rightCollision = true;
                }
            }
        }
    }
    public void draw(Graphics2D g2){
        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));

    }
}
