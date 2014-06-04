/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.server.utils;

import java.util.ArrayList;
import jscape.database.ExerciseBankTable;
import jscape.irt.Item;

/**
 *
 * @author achantreau
 */
public class IRTTesting {

    private static double[][] itemParameters
            = {{1.1, 5, 0.25},
            {1, 5, 0.25},
            {1.2, 6, 0.25},
            {1.1, 6, 0.25},
            {1.1, 7, 0.25},
            {1.5, 7, 0.25},
            {1.2, 7, 0.25},
            {1.2, 7, 0.25},
            {1.2, 8, 0.25},
            {1.2, 9, 0.25},
            {1.2, 9, 0.25},
            {1.2, 9, 0.25},
            {1.2, 8, 0.25},
            {1.2, 7, 0.25},
            {1.2, 7, 0.25},
            {1.2, 6, 0.25},
            {1.2, 7, 0.25},
            {1.2, 7, 0.25},           
            };

    private static int[] responseVector
            = {1,
               1, 
               1,
               1,
               1,
               1,
               1,
               1,
               1,
               0,
               0,
               0,
               0,
               1,
               1,
               1,
               1,
               1};

    private static double[] knowledgeDistribution;

    private static final int K = 11;

    public static void main(String[] args) {
        IRTTesting irt = new IRTTesting();/*
        irt.initKnowledgeDistribution();

        irt.printKnowledgeDistribution();
        System.out.println("");
        for (int i=0; i < K; i++) {
            System.out.println("ANSWERING ITEM " + i);
            irt.computePosteriorDistribution(i);
            int abilityEstimate = irt.findAbilityEstimate();
            System.out.println("New ability estimate = " + abilityEstimate);
            irt.printItemInformation(abilityEstimate);
            System.out.println("");
            System.out.println("");
        }
        System.out.println("");
        irt.printKnowledgeDistribution();
        System.out.println(""); */
        //System.out.println(Arrays.toString(KnowledgeDistributionTable.getDistribution("ac6609", "Arrays")));
        
        ArrayList<Item> itemList = ExerciseBankTable.getIRTItems("ac6609", "Binary Trees");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
        
    }

    public void initKnowledgeDistribution() {
        knowledgeDistribution = new double[11];

        for (int i = 0; i < 11; i++) {
            knowledgeDistribution[i] = 1.0 / 11.0;
        }
    }

    public void computePosteriorDistribution(int itemIndex) {
        for (int i = 0; i < K; i++) {
            knowledgeDistribution[i] = numerator(i, itemIndex) / denominator(i, itemIndex);
        }
    }

    public double numerator(int theta, int itemIndex) {
        if (responseVector[itemIndex] == 1) {
            return probabilityCorrect(theta, itemParameters[itemIndex][0],
                    itemParameters[itemIndex][1], itemParameters[itemIndex][2])
                    * knowledgeDistribution[theta];
        } else {
            return (1 - probabilityCorrect(theta, itemParameters[itemIndex][0],
                    itemParameters[itemIndex][1], itemParameters[itemIndex][2]))
                    * knowledgeDistribution[theta];
        }
    }

    public double denominator(int theta, int itemIndex) {
        double denominator = 0;

        for (int i = 0; i < K; i++) {
            denominator += numerator(i, itemIndex);
        }

        return denominator;
    }

    public double probabilityCorrect(int theta, double a, double b, double c) {
        double probability = (1 - c) / (1 + Math.exp(-1.7 * a * (theta - b)));
        probability = c + probability;

        return probability;
    }
    
    public int findAbilityEstimate() {
        int abilityEstimate = 0;
        double max = -10;
        
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            if (knowledgeDistribution[i] > max) {
                max = knowledgeDistribution[i];
                abilityEstimate = i;
            }
        }
        return abilityEstimate;
    }
    
    public int maxItemInformation(int thetaEstimate) {
        double maxInformation = -10;
        int itemIndex = 0;
        
        for (int i = 0; i < itemParameters.length; i++) {
            if (itemInformation(thetaEstimate, i) > maxInformation) {
                maxInformation = itemInformation(thetaEstimate, i);
                itemIndex = i;
            }
        }
        
        return itemIndex;
    }
    
    public double itemInformation(int thetaEstimate, int itemIndex) {
        double information = 0;
        
        double aSquared = Math.pow(itemParameters[itemIndex][0], 2);
        double p = probabilityCorrect(thetaEstimate, itemParameters[itemIndex][0],
                    itemParameters[itemIndex][1], itemParameters[itemIndex][2]);
        double q = 1 - p;
        
        information = aSquared * (q / p);
        
        double numerator = p - itemParameters[itemIndex][2];
        double denominator = 1 - itemParameters[itemIndex][2];
        
        information = information * Math.pow(numerator / denominator, 2);
        
        return information;
    }

    private void printKnowledgeDistribution() {
        for (int i = 0; i < K; i++) {
            System.out.println("Level " + i + "; p(X) = " + knowledgeDistribution[i]);
        }
    }
    
    private void printItemInformation(int thetaEstimate) {
        for (int i = 0; i < itemParameters.length; i++) {
            System.out.println("Item " + i + " information = " + itemInformation(thetaEstimate, i));
        }
    }

}
