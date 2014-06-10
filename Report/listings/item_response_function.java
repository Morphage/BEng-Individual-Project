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