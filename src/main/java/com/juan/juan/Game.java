package com.juan.juan;

import static com.juan.juan.GALib.crossOver;
import static com.juan.juan.GALib.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class Game {

    public static final int PIPE_DELAY = 100;

    private Boolean paused;

    private int pauseDelay;
    private int restartDelay;
    private int pipeDelay;

    private ArrayList<Bird> birds;

    private ArrayList<Pipe> pipes;
    private Keyboard keyboard;

    public int score;
    public Boolean gameover;
    public Boolean started;

    public Game() {

        keyboard = Keyboard.getInstance();

        birds = new ArrayList<Bird>();
        birds.add(new Bird());
        birds.add(new Bird());

        restart();
    }

    public void restart() {

        //pick the two betters
        Collections.sort(birds);

        Bird padre = birds.get(0);
        Bird madre = birds.get(1);

        this.score = (int) padre.distance;

        System.out.println("PADRE: " + padre.distance);
        System.out.println("MADRE: " + madre.distance);

        ArrayList<Bird> newBirds = new ArrayList<>();

        for (int j = 0; j < 10; j++) {

            // System.out.println("CROSSOVER");
            Bird bird = crossOver(padre, madre);

            //System.out.println("Murattin");
            Mutate(bird);

            newBirds.add(bird);
        }

        newBirds.add(madre);
        newBirds.add(padre);

        birds = newBirds;

        System.out.println(birds.size());

        //restart();
        paused = false;
        started = true;
        gameover = false;

        // score = 0;
        pauseDelay = 0;
        restartDelay = 0;
        pipeDelay = 0;

        for (Bird bird : birds) {
            bird.reset();

        }
        pipes = new ArrayList<Pipe>();
    }

    public void update() {
        watchForStart();

        if (!started) {
            return;
        }

//        watchForPause();
//        watchForReset();
        if (gameover == true) {
            restart();

        }

        if (paused) {
            return;
        }

        Pipe pipe1 = new Pipe("wii");
        Pipe pipe2 = new Pipe("wii2");

        // menos infinito
        pipe1.x = 99999;
        pipe2.x = 99999;

        for (Pipe pipe : pipes) {
                                        // menos el ancho de un pipe
            if (pipe.x - birds.get(0).x > - 66) {
                if (pipe.x - birds.get(0).x < pipe1.x - birds.get(0).x) {
                    pipe1 = pipe;
                } else if (pipe.x - birds.get(0).x < pipe2.x - birds.get(0).x) {
                    pipe2 = pipe;
                }
            }
        }

        if (pipe1.x == 99999) {
            pipe1.x = 400;
            pipe2.x = 400;

        }

        for (Bird bird : birds) {
            if (bird.dead == false) {
                //bird.setInputValue(0, bird.y - pipe1.y + 50, 0);
                bird.setInputValue(bird.y, pipe1.y + 50 , 0);
                bird.update();

            }
        }

        if (gameover) {
            return;
        }

        movePipes();
        checkForCollisions();
    }

    public ArrayList<Render> getRenders() {
        ArrayList<Render> renders = new ArrayList<Render>();
        renders.add(new Render(0, 0, "lib/background.png"));
        for (Pipe pipe : pipes) {
            renders.add(pipe.getRender());
        }
        renders.add(new Render(0, 0, "lib/foreground.png"));
        for (Bird bird : birds) {
            renders.add(bird.getRender());
        }

        return renders;
    }

    private void watchForStart() {
        if (!started && keyboard.isDown(KeyEvent.VK_SPACE)) {
            started = true;
        }
    }

    private void watchForPause() {
        if (pauseDelay > 0) {
            pauseDelay--;
        }

        if (keyboard.isDown(KeyEvent.VK_P) && pauseDelay <= 0) {
            paused = !paused;
            pauseDelay = 10;
        }
    }

    private void watchForReset() {
        if (restartDelay > 0) {
            restartDelay--;
        }

        if (keyboard.isDown(KeyEvent.VK_R) && restartDelay <= 0) {
            restart();
            restartDelay = 10;
            return;
        }
    }

    private void movePipes() {
        pipeDelay--;

        if (pipeDelay < 0) {
            pipeDelay = PIPE_DELAY;
            Pipe northPipe = null;
            Pipe southPipe = null;

            // Look for pipes off the screen
            for (Pipe pipe : pipes) {
                if (pipe.x - pipe.width < 0) {
                    if (northPipe == null) {
                        northPipe = pipe;
                    } else if (southPipe == null) {
                        southPipe = pipe;
                        break;
                    }
                }
            }

            if (northPipe == null) {
                Pipe pipe = new Pipe("north");
                pipes.add(pipe);
                northPipe = pipe;
            } else {
                northPipe.reset();
            }

            if (southPipe == null) {
                Pipe pipe = new Pipe("south");
                pipes.add(pipe);
                southPipe = pipe;
            } else {
                southPipe.reset();
            }

            northPipe.y = southPipe.y + southPipe.height + 175;
        }

        for (Pipe pipe : pipes) {
            pipe.update();
        }
    }

    private void checkForCollisions() {

        int alive = 0;
        for (Pipe pipe : pipes) {
            for (Bird bird : birds) {
                if (bird.dead == false) {
                    alive++;

                    if (pipe.collides(bird.x, bird.y, bird.width, bird.height)) {
                        bird.dead = true;
                        bird.y = -1000;
                    } else if (pipe.x == bird.x && pipe.orientation.equalsIgnoreCase("south")) {
                        score++;
                    }
                }

            }

        }

        for (Bird bird : birds) {
            if (bird.dead == false) {

                // Ground + Bird collision
                if (bird.y + bird.height > App.HEIGHT - 80 || bird.y + bird.height <= 0) {
                    bird.dead = true;

                    bird.y = App.HEIGHT - 80 - bird.height;
                    bird.y = -1000;

                }
            }

        }

        if (alive == 0) {
            gameover = true;
        }
    }
}
