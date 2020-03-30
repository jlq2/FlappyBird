package com.juan.juan;

import io.jenetics.BitGene;
import io.jenetics.Genotype;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Bird implements Comparable<Bird> {

    public int x;
    public int y;
    public int width;
    public int height;

    public boolean dead;

    public double yvel;
    public double gravity;

    private int jumpDelay;
    private double rotation;

    private Image image;
    private Keyboard keyboard;
    public double pesos[];
    private double entrada[];

    public double distance;

    public static Integer fitnes(int distance) {
        return distance;
    }

    public Bird() {
        x = 100;
        y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        pesos = new double[4];
        entrada = new double[4];

        entrada[0] = 1; // BIAS

        Random rn = new Random(System.currentTimeMillis());

        for (int i = 0; i < 4; i++) {
            pesos[i] = 0; //rn.nextDouble();
            System.out.println("peso[" + i + "]: " + pesos[i]);
        }

        keyboard = Keyboard.getInstance();

        distance = 0;

    }

    public void reset() {
        x = 100;
        y = 150;
        yvel = 0;
        width = 45;
        height = 32;
        gravity = 0.5;
        jumpDelay = 0;
        rotation = 0.0;
        dead = false;
        distance = 0;

    }

    public boolean calculate() {

        float sum = 0;
        for (int i = 0; i < 4; i++) {
            System.out.println("Calc peso[" + i + "]: " + pesos[i]);

            sum += pesos[i] * entrada[i];

        }
        System.out.println("sum: " + sum);

        return sum >= 0.5 ? true : false;

    }

    public void setInputValue(float distanceX, float distaceY, float vel) {
        double normalized = (distanceX - 0) / (400 - 0);
        double normalized2 = (distaceY - 0) / (400 - 0);
        double normalized3 = (vel - 0) / (400 - 0);

        entrada[0] = 1;
        entrada[1] = normalized;
        entrada[2] = normalized2;
        entrada[3] = normalized3;

    }

    public void update() {
        yvel += gravity;
        distance++;

        if (jumpDelay > 0) {
            jumpDelay--;
        }

        if (!dead && calculate()) {
            yvel = -10;
            jumpDelay = 10;
        }

        y += (int) yvel;
    }

    public Render getRender() {
        Render r = new Render();
        r.x = x;
        r.y = y;

        if (image == null) {
            image = Util.loadImage("lib/bird.png");
        }
        r.image = image;

        rotation = (90 * (yvel + 20) / 20) - 90;
        rotation = rotation * Math.PI / 180;

        if (rotation > Math.PI / 2) {
            rotation = Math.PI / 2;
        }

        r.transform = new AffineTransform();
        r.transform.translate(x + width / 2, y + height / 2);
        r.transform.rotate(rotation);
        r.transform.translate(-width / 2, -height / 2);

        return r;
    }

    @Override
    public int compareTo(Bird o) {
        return Double.compare(o.distance, distance);
    }

}
