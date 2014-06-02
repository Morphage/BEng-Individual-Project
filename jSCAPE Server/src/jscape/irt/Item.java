/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.irt;

/**
 *
 * @author achantreau
 */
public class Item {
    
    private int    itemID;
    private double a;
    private double b;
    private double c;
    
    public Item(int itemID, double a, double b, double c) {
        this.itemID = itemID;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getItemID() {
        return itemID;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
    
    @Override
    public String toString() {
        return "Item " + itemID + ", a=" + a + ", b=" + b + ", c=" + c;
    }
}
