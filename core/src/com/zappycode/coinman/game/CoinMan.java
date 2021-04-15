package com.zappycode.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.sun.org.apache.regexp.internal.RE;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture[] man;
    int manState = 0;
    int pause = 0;
    float gravity = 0.9f;
    float velocity = 0;
    int manY = 0;
    Random random ;
    com.badlogic.gdx.math.Rectangle manRectangle;
    int score = 0;
    int gameState = 0;
    Texture dizzy;

    BitmapFont font;

    ArrayList<Integer> coinXs = new ArrayList<>();
    ArrayList<Integer> coinYs = new ArrayList<>();
    ArrayList<com.badlogic.gdx.math.Rectangle> coinRectangle = new ArrayList<>();
    Texture coin;
    int coinCount;

    ArrayList<Integer> bombXs = new ArrayList<>();
    ArrayList<Integer> bombYs = new ArrayList<>();
    ArrayList<com.badlogic.gdx.math.Rectangle> bombRectangle = new ArrayList<>();
    Texture bomb;
    int bombCount;



    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        man = new Texture[4];
        man[0]= new Texture("frame-1.png");
        man[1]= new Texture("frame-2.png");
        man[2]= new Texture("frame-3.png");
        man[3]= new Texture("frame-4.png");
        manY = Gdx.graphics.getHeight() / 2;
        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");
        font = new BitmapFont();
        font.setColor(Color.BLUE);
        font.getData().setScale(10);
        dizzy = new Texture("dizzy-1.png");


        random = new Random();
    }

    public void makeCoin(){
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        coinYs.add((int)height);
        coinXs.add(Gdx.graphics.getWidth());
    }

    public void makeBomb(){
        float height = random.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int)height);
        bombXs.add(Gdx.graphics.getWidth());
    }

    @Override
    public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if (gameState == 1) {
            //game is live
            //bombs
            if (bombCount < 250){
                bombCount++;
            }else{
                bombCount =0;
                makeBomb();
            }
            bombRectangle.clear();
            for (int i =0;i<bombXs.size();i++){
                batch.draw(bomb,bombXs.get(i),bombYs.get(i));
                bombXs.set(i,bombXs.get(i)-8);
                bombRectangle.add(new com.badlogic.gdx.math.Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
            }

            if (coinCount < 100){
                coinCount++;
            }else {
                coinCount = 0;
                makeCoin();
            }
            //coin
            coinRectangle.clear();
            for (int i = 0;i<coinXs.size();i++){
                batch.draw(coin,coinXs.get(i),coinYs.get(i));
                coinXs.set(i,coinXs.get(i)-4);
                coinRectangle.add(new com.badlogic.gdx.math.Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
            }


            if (Gdx.input.justTouched()){
                velocity = -20;
            }

            if (pause < 8){
                pause++;
            }else{
                pause = 0;
                if (manState < 3){
                    manState++;
                }else{
                    manState = 0;
                }

            }

            velocity += gravity;
            manY -= velocity;

            if (manY <= 0){
                manY = 0;
            }



        } else if (gameState == 0) {
            //to start
            if (Gdx.input.justTouched()){
                gameState = 1;
            }

        } else if (gameState == 2) {
            //game over
            if (Gdx.input.justTouched()) {
                gameState = 1;
                velocity = 0;
                manY = Gdx.graphics.getHeight() / 2;
                score = 0;
                coinXs.clear();
                coinYs.clear();
                coinRectangle.clear();
                coinCount = 0;
                bombRectangle.clear();
                bombXs.clear();
                bombYs.clear();
                bombCount = 0;
            }

        }

        if (gameState == 2){
            batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2,  manY);
        }else{
            batch.draw(man[manState],Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2,  manY);
        }
        manRectangle = new com.badlogic.gdx.math.Rectangle(Gdx.graphics.getWidth() / 2- man[manState].getWidth(),manY,man[manState].getWidth(),man[manState].getHeight());

        for (int i = 0;i<coinRectangle.size();i++){
            if (Intersector.overlaps(manRectangle,coinRectangle.get(i))){
                score++;
                coinRectangle.remove(i);
                coinXs.remove(i);
                coinYs.remove(i);
                break;

            }
        }
        for (int i = 0;i<bombRectangle.size();i++){
            if (Intersector.overlaps(manRectangle,bombRectangle.get(i))){
                gameState = 2;



            }
        }



        font.draw(batch,String.valueOf(score),100,200);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();

    }

}
