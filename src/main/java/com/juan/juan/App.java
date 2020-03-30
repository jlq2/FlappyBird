package com.juan.juan;

import io.jenetics.DoubleChromosome;
import io.jenetics.DoubleGene;
import io.jenetics.Genotype;
import io.jenetics.Phenotype;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;
import io.jenetics.util.ISeq;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import javax.swing.*;

public class App {

    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    public static void main(String[] args) {

        JFrame frame = new JFrame("FLAPIA Bird");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        Keyboard keyboard = Keyboard.getInstance();
        frame.addKeyListener(keyboard);

        GamePanel panel = new GamePanel();
        frame.add(panel);

        
        
        Game game = panel.getGame();


    }
}
