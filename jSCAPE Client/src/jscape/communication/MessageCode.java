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
    GET_EXERCISE(3),
    ANSWER_EXERCISE(4),
    LOGIN(5),
    GET_DATE_LIST(6),
    GET_MONTHLY_PROGRESS(7),
    GET_TOTAL_PER_DAY(8),
    GET_YEARLY_PROGRESS(9),
    GET_TOTAL_PER_YEAR(10);

    private final int value;

    private MessageCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
