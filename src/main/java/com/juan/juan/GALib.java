/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.juan.juan;

import java.util.Random;

/**
 *
 * @author Uni
 */
public class GALib {
        static Random rn = new Random(0);

    public static void Mutate(Bird padre) {

        int mutateRatio = 20;

        if (mutateRatio < rn.nextInt(100)) {

            for (int i = 0; i < padre.pesos.length; i++) {

                padre.pesos[i] = rn.nextDouble();
               System.out.println("Mutando!!" + padre.pesos[i]);
            }

        }
    }

    //https://stackoverflow.com/questions/23970715/java-mixing-together-two-double-bitstrings-for-genetic-algorithm-crossover
    public static Bird crossOver(Bird padre, Bird madre) {

        Bird bird = new Bird();

        for (int i = 0; i < padre.pesos.length; i++) {

            long weightALong = Double.doubleToRawLongBits(padre.pesos[i]);
            long weightBLong = Double.doubleToRawLongBits(madre.pesos[i]);
            long mask = -1L; // all bits set to 1
            int crossOverPoint = rn.nextInt(Long.SIZE);

            long combined = 0;
            // treat special cases because of modulo Long.SIZE of second parameter of shifting operations
            if (crossOverPoint == 0) {
                combined = weightBLong;
            } else if (combined == Long.SIZE) {
                combined = weightALong;
            } else {
                combined = (weightALong & (mask << (Long.SIZE - crossOverPoint)))
                        | (weightBLong & (mask >>> crossOverPoint));
            }
            double newWeight = Double.longBitsToDouble(combined);

            bird.pesos[i] = newWeight;

        }

        return bird;
    }

}
