package com.atilsamancioglu.survivorbirdstarter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee;
	Texture bee2;
	Texture bee3;
	Float birdX = 0f;
	Float birdY = 0f;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.28f;
	float enemyVelocity = 10;
	Random random;
	int score = 0;
	int scoredEnemy = 0;
	BitmapFont font;
	BitmapFont font2;

	Circle birdCircle;

	ShapeRenderer shapeRenderer;

	int numberOfEnemies = 4;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyOffSet = new float[numberOfEnemies];
	float[] enemyOffSet2 = new float[numberOfEnemies];
	float[] enemyOffSet3 = new float[numberOfEnemies];
	float distance = 0;

	Circle[] enemyCircles;
	Circle[] enemyCircles2;
	Circle[] enemyCircles3;

	@Override
	public void create() {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee = new Texture("bee.png");
		bee2 = new Texture("bee.png");
		bee3 = new Texture("bee.png");

		distance = (float) Gdx.graphics.getWidth() / 2;
		random = new Random();

		birdX = (float) (Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2);
		birdY = (float) Gdx.graphics.getHeight() / 3;

		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(6);

		font2 = new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);

		for (int i = 0; i < numberOfEnemies; i++) {
			enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyX[i] = Gdx.graphics.getWidth() - bee.getWidth() / 2 + i * distance;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (enemyX[scoredEnemy] < birdX) {
				score++;
				if (scoredEnemy < numberOfEnemies - 1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = -7;
			}

			for (int i = 0; i < numberOfEnemies; i++) {

				if (enemyX[i] < -bee.getWidth()) {
					enemyX[i] = enemyX[i] + numberOfEnemies * distance;

					enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
				} else {
					enemyX[i] = enemyX[i] - enemyVelocity;
				}

				batch.draw(bee, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
				batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);

				float radius = Gdx.graphics.getWidth() / 35f; // Daha küçük çarpışma alanı
				enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 20, radius);
				enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 20, radius);
				enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 20, radius);
			}

			if (birdY > 0 && birdY < 1080) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				gameState = 2;
			}

		} else if (gameState == 0) {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			font2.draw(batch, "Game Over! Tap To Play Again!", 100, Gdx.graphics.getHeight() / 2);

			if (Gdx.input.justTouched()) {
				gameState = 1;
				birdY = (float) Gdx.graphics.getHeight() / 3;

				for (int i = 0; i < numberOfEnemies; i++) {
					enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyX[i] = Gdx.graphics.getWidth() - bee.getWidth() / 2 + i * distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoredEnemy = 0;
				score = 0;
			}
		}

		batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 10);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		// Kuşun çarpışma alanını görsele uygun olarak daha ortalanmış ve küçültülmüş hale getir
		float birdRadius = Gdx.graphics.getWidth() / 35f;
		birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 20, birdRadius);

		for (int i = 0; i < numberOfEnemies; i++) {
			if (Intersector.overlaps(birdCircle, enemyCircles[i]) ||
					Intersector.overlaps(birdCircle, enemyCircles2[i]) ||
					Intersector.overlaps(birdCircle, enemyCircles3[i])) {
				System.out.println("Collision detected 'Çarpışma algılandı'");
				gameState = 2;
			}
		}
	}

	@Override
	public void dispose() {
	}
}
