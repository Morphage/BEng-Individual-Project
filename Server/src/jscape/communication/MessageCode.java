/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jscape.communication;

/**
 *
 * @author achantreau
 */
public enum MessageCode {
    PROFILE_INFO(0),
    PERFORMANCE_STATS(1),
    EXERCISE_CATEGORIES(2),
    GET_EXERCISE(3);
    
    private final int value;
    private MessageCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
