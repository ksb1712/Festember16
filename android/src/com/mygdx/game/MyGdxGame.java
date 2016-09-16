package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter implements InputProcessor{

    AndroidCode androidCode;

    SpriteBatch batch;


	Sprite background;
	Sprite background2;

	Texture bk1;
	Texture ship;
	Texture obstacle;
	Texture bonus;
	Texture heart;

	ShapeRenderer shapeRenderer;

	Sound danger;
	Music backgroundMusic;
	Sound starCollect;
	Sound defeat;

	Sprite player;
	float posX,posY;
	float pwidth,pheight;
	float velocity, velX,velY;
	float finalX,finalY;

	long score;
	int life;
	int bonusTime;
	int no_of_rocks;
	int displayTime;
	int elapsedTime;

	float bk1X,bk2X;


	ArrayList<Rocks> obstacles;
	Bonus bonusDetails;

	BitmapFont scoreDisp;

	TextButton pauseButton;
	TextButton.TextButtonStyle style;
	TextureRegion play;
	TextureRegion pause;


	boolean playerMoving;
	boolean dangerPlaying;
	boolean noDamage;
	boolean paused;
	boolean gameOver;

    public MyGdxGame(AndroidCode androidCode) {
        this.androidCode = androidCode;
    }

    @Override
	public void create () {
		batch = new SpriteBatch();

		bk1=new Texture("background.jpg");
		background=new Sprite(bk1);
		background2=new Sprite(bk1);
		background2.flip(true,false);
		bk1X=0;
		bk2X=Gdx.graphics.getWidth();


		obstacle=new Texture("asteroid.png");
		bonus=new Texture("mystery.png");
		heart=new Texture("life.png");

		danger=Gdx.audio.newSound(Gdx.files.internal("danger.mp3"));
		starCollect=Gdx.audio.newSound(Gdx.files.internal("starcollect.mp3"));
		backgroundMusic=Gdx.audio.newMusic(Gdx.files.internal("spacemusic.mp3"));
		defeat=Gdx.audio.newSound(Gdx.files.internal("defeat.mp3"));
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		shapeRenderer=new ShapeRenderer();


		scoreDisp=new BitmapFont(Gdx.files.internal("spacefont.fnt"),Gdx.files.internal("spacefont.png"),false);
		scoreDisp.setColor(com.badlogic.gdx.graphics.Color.YELLOW);


		no_of_rocks=0;
		score=1;
		life=200;
		ship=new Texture("player.png");
		player=new Sprite(ship);
		posX=100;posY=100;
		pwidth=100;pheight=100;
		finalX=300;finalY=300;

		velocity=200;velX=0;velY=0;

		playerMoving=false;
		dangerPlaying=false;
		noDamage=false;
		paused=false;
		gameOver=false;



		obstacles=new ArrayList<Rocks>();
		bonusDetails=new Bonus();
		bonusDetails.setBonusSprite(new Sprite(bonus));
		bonusTime=0;
		displayTime=0;
		elapsedTime=0;

		pause=new TextureRegion(new Texture(Gdx.files.internal("pause.png")));
		play=new TextureRegion(new Texture(Gdx.files.internal("play.png")));
		style=new TextButton.TextButtonStyle();
		style.up=new TextureRegionDrawable(pause);
		style.down=new TextureRegionDrawable(pause);
		style.font=scoreDisp;
		pauseButton=new TextButton("",style);
		pauseButton.setPosition(100, 100);
		pauseButton.setWidth(50);
		pauseButton.setHeight(50);

		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("mylog", "x = " + x + " y= " + y);

			}
		});




		Gdx.input.setInputProcessor(this);

	}

	@Override
	public void render () {

			Gdx.app.log("mylog",String.valueOf(Gdx.graphics.getFramesPerSecond()));

			batch.begin();

			batch.draw(background, bk1X, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			batch.draw(background2, bk2X, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(!paused&&!gameOver) {
			bk1X--;
			bk2X--;
			if (bk1X == -Gdx.graphics.getWidth()) {
				bk1X = Gdx.graphics.getWidth();
			}
			if (bk2X == -Gdx.graphics.getWidth()) {
				bk2X = Gdx.graphics.getWidth();
			}
		}

			batch.draw(player, posX - pwidth / 2, posY - pheight / 2, pwidth, pheight);
		if(life>0&& !paused) {
			for (int i = 0; i < no_of_rocks; i++) {
				batch.draw(obstacles.get(i).getRock(), obstacles.get(i).getPosX(), obstacles.get(i).getPosY(), obstacles.get(i).getSize(), obstacles.get(i).getSize());
				obstacles.get(i).setPosX(obstacles.get(i).getPosX() + obstacles.get(i).getVelocity());
				if (obstacles.get(i).getPosX() <= 0) {
					obstacles.get(i).setPosX(Gdx.graphics.getWidth());
					obstacles.get(i).setAppearances();
				}

				if (obstacles.get(i).getAppearances() == obstacles.get(i).getMax_appearance()) {
					obstacles.remove(i);
					no_of_rocks--;
				}

			}

			if (bonusDetails.isBonusSprite()) {
				batch.draw(bonusDetails.bonusSprite, bonusDetails.getPosX(), bonusDetails.getPosY(), bonusDetails.getSize(), bonusDetails.getSize());
				bonusDetails.setPosX(bonusDetails.getPosX() + bonusDetails.getVelocity());
				if (bonusDetails.getPosX() <= 0) {
					bonusDetails.setIsBonusSprite(false);
					bonusDetails.resetValues();
				}
				checkBonusCollected();
			}



			score++;

			setVelocity();
			stopCharacter();

		}
		else if(paused){
			//pause game
			scoreDisp.draw(batch,"paused",Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		}
		else{
			//game over
			batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
			scoreDisp.draw(batch,"game over",Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()*3/4);
			scoreDisp.draw(batch,"play again",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/2);
			scoreDisp.draw(batch,"share score",Gdx.graphics.getWidth()*3/4,Gdx.graphics.getHeight()/2);
			backgroundMusic.stop();

			gameOver=true;
		}

			scoreDisp.draw(batch, "score : " + score,10,Gdx.graphics.getHeight()-70);
			if(displayTime==150){
				scoreDisp.draw(batch,"score bonus!!!",Gdx.graphics.getWidth()/2-100,100);
				elapsedTime++;
				if(elapsedTime==displayTime){
					displayTime=0;
					elapsedTime=0;
				}
			}
			if(displayTime==100){
				scoreDisp.draw(batch,"life bonus!!!",Gdx.graphics.getWidth()/2-100,100);
				elapsedTime++;
				if(elapsedTime==displayTime){
					displayTime=0;
					elapsedTime=0;
				}
			}
			if(noDamage){
				scoreDisp.draw(batch,"your ship will not take any damage for sometime!!!",Gdx.graphics.getWidth()/2-150,100);
			}

			if(bonusDetails.isBonusCollected()){
				if(bonusDetails.isSpeedBoost()){
					scoreDisp.draw(batch,"speed is doubled!!!",Gdx.graphics.getWidth()/2-100,100);
				}
			}


			batch.draw(heart, 10, Gdx.graphics.getHeight() - 60, 50, 50);
		if(!gameOver){
			pauseButton.draw(batch, batch.getColor().a);
		}
			batch.end();



			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
			shapeRenderer.rect(70, Gdx.graphics.getHeight() - 50, (Gdx.graphics.getWidth() - 150) * life / 200, 30);

			shapeRenderer.end();




			if (score % 75 == 0) {
				no_of_rocks++;
				Rocks newRock = new Rocks();
				newRock.setRock(new Sprite(obstacle));
				obstacles.add(newRock);

			}
			if (score % 1500 == 0) {
				bonusDetails.setIsBonusSprite(true);
			}
		    if (!noDamage&&!paused) {
			    checkCollision();
			}



			if (bonusDetails.isBonusCollected()) {


				if (bonusDetails.isFullHealth()) {
					life = 200;
					displayTime=100;
					bonusDetails.setIsBonusCollected(false);
					bonusDetails.resetValues();
				} else if (bonusDetails.isScoreBoost()) {
					score += 300;
					displayTime=150;
					bonusDetails.setIsBonusCollected(false);
					bonusDetails.resetValues();
				} else if (bonusDetails.isNoDamage()) {
					noDamage = true;
					bonusTime++;
					if (bonusTime == bonusDetails.getFrames()) {
						noDamage = false;
						bonusDetails.setIsBonusCollected(false);
						bonusDetails.resetValues();
						bonusTime = 0;
					}
				} else {

					if (velocity != 400) {
						velocity = 400;

					}
					bonusTime++;
					if (bonusTime == bonusDetails.getFrames()) {
						velocity = 200;

						bonusDetails.setIsBonusCollected(false);
						bonusDetails.resetValues();
						bonusTime = 0;
					}
				}

			}




	}

	@Override
	public void dispose () {
		batch.dispose();
		ship.dispose();
		obstacle.dispose();
		heart.dispose();
		danger.dispose();
		bonus.dispose();
		shapeRenderer.dispose();
		backgroundMusic.dispose();
		starCollect.dispose();
		bk1.dispose();
		defeat.dispose();
		scoreDisp.dispose();


	}

	public void resetGame(){
		life=200;
		score=1;
		for(int i=(obstacles.size()-1);i>=0;i--){
            obstacles.remove(i);
        }
		no_of_rocks=0;
		gameOver=false;
		playerMoving=false;
		backgroundMusic.play();

	}

	public void setVelocity(){

		float distance= (float)Math.sqrt(Math.pow(finalX-posX,2)+Math.pow(finalY - posY, 2));

		if(playerMoving) {
			velX = (finalX - posX) * velocity / distance;
			velY = (finalY - posY) * velocity / distance;

			posX += velX * Gdx.graphics.getDeltaTime();
			posY += velY * Gdx.graphics.getDeltaTime();
		}

	}

	public void stopCharacter(){
		if(velX>=0){
			if(posX>=finalX){
				velX=0;
				velY=0;
				playerMoving=false;
			}
		}
		else{
			if(posX<=finalX){
				velX=0;
				velY=0;
				playerMoving=false;
			}
		}

		if(velY>=0){
			if(posY>=finalY) {
				velY = 0;
				velX = 0;
				playerMoving = false;
			}
		}
		else {
			if(posY<=finalY){
				velY=0;
				velX=0;
				playerMoving=false;
			}
		}
	}

	public void checkBonusCollected(){

		float playerCenterX=posX+pwidth/2;
		float playerCenterY=posY+pheight/2;
		float bonusCenterX=bonusDetails.getPosX()+bonusDetails.getSize()/2;
		float bonusCenterY=bonusDetails.getPosY()+bonusDetails.getSize()/2;
		float minX=(bonusDetails.getSize()+pwidth)/2;
		float minY=(bonusDetails.getSize()+pheight)/2;

		if((Math.abs(playerCenterX-bonusCenterX)<=minX)&&(Math.abs(playerCenterY - bonusCenterY)<=minY)){

			bonusDetails.setIsBonusSprite(false);
			bonusDetails.setIsBonusCollected(true);
			starCollect.play();

		}

	}


	public void checkCollision(){
		float rockcenterX,rockcenterY;
		float playercenterX,playercenterY;
		float minX,minY;
		boolean isColliding=false;

		for(int i=0;i<obstacles.size();i++){
			rockcenterX =obstacles.get(i).getPosX()+obstacles.get(i).getSize()/2;
			rockcenterY=obstacles.get(i).getPosY()+obstacles.get(i).getSize()/2;
			playercenterX=posX+pwidth/2;
			playercenterY=posY+pheight/2;
			minX=(obstacles.get(i).getSize()+pwidth)/2;
			minY=(obstacles.get(i).getSize()+pheight)/2;

			if((Math.abs(rockcenterX-playercenterX+50)<=minX)&&(Math.abs(rockcenterY-playercenterY+50)<=minY)) {

					batch.setColor(com.badlogic.gdx.graphics.Color.RED);
					if (!dangerPlaying) {
						danger.play();
						dangerPlaying = true;
					}
					isColliding = true;
					life--;


			}


		}
		if(!isColliding){
			batch.setColor(com.badlogic.gdx.graphics.Color.WHITE);
			danger.stop();
			dangerPlaying=false;
		}

	}


	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		finalX=screenX;
		finalY=Gdx.graphics.getHeight()-screenY;

		if(finalX>100&&finalX<150&&finalY>100&&finalY<150){
			if(!paused){
				paused=true;
				backgroundMusic.pause();
				style.up=new TextureRegionDrawable(play);
				style.down=new TextureRegionDrawable(play);

			}
			else{
				paused=false;
				backgroundMusic.play();
				style.up=new TextureRegionDrawable(pause);
				style.down=new TextureRegionDrawable(pause);
			}
		}
        if(gameOver){
            if(finalX<(Gdx.graphics.getWidth()/2)){
                resetGame();
            }
            else{
                // implicit intent
                androidCode.shareScore(score);
            }
        }

		playerMoving=true;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		finalX=screenX;
		finalY=Gdx.graphics.getHeight()-screenY;
		playerMoving=true;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		finalX=screenX;
		finalY=Gdx.graphics.getHeight()-screenY;
		playerMoving=true;
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void pause() {
		super.pause();
	}
}
