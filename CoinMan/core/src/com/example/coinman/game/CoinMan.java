package com.example.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture dizzy;
	int manstate=0;
	int delay=0;
	float accDuetoGravity=0.6f;
	float fallVelocity=0;
	int manY=0;
	int coinDelay=0;
	int bombDelay=0;
	Texture coins;
	Texture bombs;
	Random random;

	BitmapFont font;
	int gamestate=0;
	int score=0;
	int speedCoin=4;
	int speedBomb=10;
	int speedDelay=0;

	ArrayList<Rectangle> coinRectangle=new ArrayList<>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<>();
	Rectangle manRectangle;

	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> bombXs=new ArrayList<>();
	ArrayList<Integer> bombYs=new ArrayList<>();

	public void makeCoins(){

		float heightofCoins=random.nextFloat()*Gdx.graphics.getHeight()- coins.getHeight()/2;
		if (heightofCoins>=(Gdx.graphics.getHeight()/4)) {
			coinYs.add((int) heightofCoins);
		}
		else {
			coinYs.add((int)Gdx.graphics.getHeight()/4);
		}
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makebombs(){
		float heightofBombs=random.nextFloat()*Gdx.graphics.getHeight()- bombs.getHeight()/2;
		if (heightofBombs>=(Gdx.graphics.getHeight()/4)) {
			bombYs.add((int) heightofBombs);
		}
		else {
			bombYs.add((int)Gdx.graphics.getHeight()/4);
		}
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		dizzy=new Texture("dizzy-1.png");
		coins=new Texture("coin.png");
		bombs=new Texture("bomb.png");

		manY=Gdx.graphics.getHeight() / 4;
		random=new Random();

		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().setScale(10);

	}

	@Override
	public void render () {
         batch.begin();
         batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

         if(gamestate==0) { //game active

			 if (coinDelay < 100) {
				 coinDelay++;
			 } else {
				 coinDelay = 0;
				 makeCoins();
			 }

			 if (speedDelay<2000){
			 	speedDelay++;
			 }else {
			 	speedDelay=0;
			 	speedCoin++;
			 }

			 coinRectangle.clear();
			 for (int i = 0; i < coinXs.size(); i++) {
				 coinXs.set(i, coinXs.get(i) - speedCoin);
				 batch.draw(coins, coinXs.get(i), coinYs.get(i));
				 coinRectangle.add(new Rectangle(coinXs.get(i), coinYs.get(i), coins.getWidth(), coins.getHeight()));
			 }

			 if (bombDelay < 250) {
				 bombDelay++;
			 } else {
				 bombDelay = 0;
				 makebombs();
			 }

			 if (speedDelay<2000){
				 speedDelay++;
			 }else {
			 	speedDelay=0;
				 speedBomb++;
			 }

			 bombRectangle.clear();
			 for (int i = 0; i < bombXs.size(); i++) {
				 bombXs.set(i, bombXs.get(i) - speedBomb);
				 batch.draw(bombs, bombXs.get(i), bombYs.get(i));
				 bombRectangle.add(new Rectangle(bombXs.get(i), bombYs.get(i), bombs.getWidth(), bombs.getHeight()));
			 }

			 if (Gdx.input.justTouched()) {
				 fallVelocity = -10;
			 }
			 if (delay < 9) {
				 delay++;
			 } else {
				 delay = 0;
				 if (manstate == 0) {
					 manstate++;
				 } else {
					 manstate = 0;
				 }
			 }

			 fallVelocity += accDuetoGravity;
			 manY -= fallVelocity;
			 if (manY <= Gdx.graphics.getHeight() / 4) {
				 manY = Gdx.graphics.getHeight() / 4;
			 }

			 batch.draw(man[manstate], Gdx.graphics.getWidth() / 13, manY);
			 manRectangle = new Rectangle(Gdx.graphics.getWidth() / 13, manY, man[manstate].getWidth(), man[manstate].getHeight());

			 for (int i = 0; i < coinRectangle.size(); i++) {
				 if (Intersector.overlaps(manRectangle, coinRectangle.get(i))) {
					 coinRectangle.remove(i);
					 coinXs.remove(i);
					 coinYs.remove(i);
					 score++;
				 }
			 }

			 for (int i = 0; i < bombRectangle.size(); i++) {
				 if (Intersector.overlaps(manRectangle, bombRectangle.get(i))) {
					 gamestate=2;
				 }
			 }
		 }else if (gamestate==1){
			 batch.draw(man[manstate], Gdx.graphics.getWidth() / 13, manY);
         	if (Gdx.input.justTouched()){
         		score=0;
         		gamestate=0;
			}
		 }else {
			 batch.draw(dizzy,Gdx.graphics.getWidth() / 13,manY);
			 if (Gdx.input.justTouched()){
			 	gamestate=1;
			 	bombXs.clear();
			 	bombYs.clear();
			 	bombRectangle.clear();
			 	coinYs.clear();
			 	coinXs.clear();
			 	coinRectangle.clear();
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
