/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.irt;

import java.util.ArrayList;
import java.util.Random;
import jscape.database.ExerciseBankTable;
import jscape.database.KnowledgeDistributionTable;

/**
 *
 * @author achantreau
 */
public class IRTModule {

    private static Random rand = new Random();

    public static void main(String[] args) {
        IRTModule irt = new IRTModule();
        int thetaEstimate = 5;

        double[] knowledgeDistribution = KnowledgeDistributionTable.getDistribution("ac6609", "Binary Trees");

        ArrayList<Item> itemList = ExerciseBankTable.getItems("ac6609", "Binary Trees");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
        
        System.out.println("");
        System.out.println("");
        
        int[] responseVector = {1,1,0,1,1,0,1,0,0,1,1,1,1};

        int i = 0;
        while (itemList.size() > 7) {
            int itemIndex = irt.maxItemInformation(thetaEstimate, itemList);
            System.out.println("ItemID with max information=" + itemList.get(itemIndex).getItemID()
             + "....item difficulty = " + itemList.get(itemIndex).getB());

            if (responseVector[i] == 0) {
                System.out.println("Answering item incorrectly...");
            } else {
                System.out.println("Answering item correctly...");
            }

            irt.computePosteriorDistribution(knowledgeDistribution, itemList.get(itemIndex), responseVector[i]);
            irt.printKnowledgeDistribution(knowledgeDistribution);
            thetaEstimate = irt.abilityEstimate(knowledgeDistribution);
            System.out.println("After answering item, theta estimate is now " + thetaEstimate);
            itemList.remove(itemIndex);
            System.out.println("");
            System.out.println("");
            i++;
        }
    }

    private void computePosteriorDistribution(double[] knowledgeDistribution, Item item, int u_i) {
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            knowledgeDistribution[i]
                    = numerator(i, item, knowledgeDistribution, u_i) / denominator(item, knowledgeDistribution, u_i);
        }
    }

    private double numerator(int theta, Item item, double[] knowledgeDistribution, int u_i) {
        if (u_i == 1) {
            return probabilityCorrectAnswer(theta, item) * knowledgeDistribution[theta];
        } else {
            return (1 - probabilityCorrectAnswer(theta, item)) * knowledgeDistribution[theta];
        }
    }

    private double denominator(Item item, double[] knowledgeDistribution, int u_i) {
        double denominator = 0;

        for (int i = 0; i < knowledgeDistribution.length; i++) {
            denominator += numerator(i, item, knowledgeDistribution, u_i);
        }

        return denominator;
    }

    /**
     * Calculates P(correct|theta), i.e. the probability of a correct answer
     * given an ability level of theta.
     */
    private double probabilityCorrectAnswer(int theta, Item item) {
        double a = item.getA();
        double b = item.getB();
        double c = item.getC();

        double probability = (1 - c) / (1 + Math.exp(-1.7 * a * (theta - b)));
        probability = c + probability;

        return probability;
    }

    /**
     * Returns the ID of the item with the maximum information for the
     * provisional proficiency level estimated given by thetaEstimate.
     */
    private int maxItemInformation(int thetaEstimate, ArrayList<Item> itemList) {
        double maxInformation = -10;
        int itemID = 0;

        for (int i = 0; i < itemList.size(); i++) {
            double information = itemInformation(thetaEstimate, itemList.get(i));

            if (information > maxInformation) {
                maxInformation = information;
                itemID = i;
            }
        }

        return itemID;
    }

    /**
     * Calculates the item information for the provisional proficiency level
     * estimated until that moment, i.e. thetaEstimate.
     */
    private double itemInformation(int thetaEstimate, Item item) {
        double information;

        double a = item.getA();
        double c = item.getC();

        double aSquared = Math.pow(a, 2);
        double p = probabilityCorrectAnswer(thetaEstimate, item);
        double q = 1 - p;

        information = aSquared * (q / p);

        double numerator = p - c;
        double denominator = 1 - c;

        information = information * Math.pow(numerator / denominator, 2);

        return information;
    }

    /**
     * Finds the current estimated theta level by finding the maximum
     * probability in the knowledge distribution.
     */
    private int abilityEstimate(double[] knowledgeDistribution) {
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

    /**
     * Print out knowledge distribution.
     */
    private void printKnowledgeDistribution(double[] knowledgeDistribution) {
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            System.out.println("Level " + i + "; p(X) = " + knowledgeDistribution[i]);
        }
    }
}
