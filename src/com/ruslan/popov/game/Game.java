package com.ruslan.popov.game;

import com.ruslan.popov.IO.Input;
import com.ruslan.popov.display.Display;
import com.ruslan.popov.game.level.Level;
import com.ruslan.popov.graphics.Sprite;
import com.ruslan.popov.graphics.SpriteSheet;
import com.ruslan.popov.graphics.TextureAtlas;
import com.ruslan.popov.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Game implements Runnable {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 3;
    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDLE_TIME = 1;

    private boolean running;

    private Thread gameThread;
    private Graphics2D graphics;
    private Input input;
    private TextureAtlas atlas;
    private Player player;

    private Level lvl;
    public static final String ATLAS_FILE_NAME = "texture_atlas.png";


    public Game() {
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.graphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        player = new Player(300, 300, atlas, 2, 3);
        lvl = new Level(atlas);
    }


    public synchronized void start() {

        // проверка на запуск
        if (running)
            return;


        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {

        if (!running)
            return;

        running = false;

        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cleanUp();

    }

    private void update() {

        player.update(input);

        lvl.update();

    }

    private void render() {

        Display.clear();
        lvl.render(graphics);

        player.render(graphics);
        lvl.renderGrass(graphics);

        Display.swapBuffers();

    }

    // после метода start запускается этот метод (основной поток)
    @Override
    public void run() {

        int fps = 0;
        int upd = 0;
        int updl = 0;


// кол-во времени
        long count = 0;
// кол-во update
        float delta = 0;
// начинаем считать время, что бы знать сколько проходит между предыдущей итерацией нашего лупа и нынешней
        long lastTime = Time.get();
        while (running) {
            // текущее время
            long now = Time.get();
            // проверка сколько времени прошло с прошлого раза (когда мы запускали этот луп)
            long elapsedTime = now - lastTime;
            // прошлое время равняем к данному, что бы на следущей итерации прошлое время держало время данного лупа
            lastTime = now;
// добавляем сколько время прошло
            count += elapsedTime;
// проверка на изменение
            boolean render = false;
            // прошлое время делим на то , которое должно пройти
            delta += (elapsedTime / UPDATE_INTERVAL);
            // если update больше 1
            while (delta > 1) {
                // делаем update
                update();
                upd++;
                delta--;
                // проверяем если луп выполняется не первый раз, то прибаляем 1, если первый раз то запускаем ещё раз
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }
            // если есть изменения то рисуем и считаем fps (сколько раз в секунду мы рисуем)
            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDLE_TIME); // если всё ок, то 1 секунда передышка для программы, для подтягивания информации
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // после 1 сек показываем информацию на сверху над игрой
            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || Fps: " + fps + " | Upd: " + upd + " | Updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }

        }
    }


    private void cleanUp() {
        Display.destroy();

    }


}
