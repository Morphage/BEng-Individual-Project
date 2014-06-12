private double probabilityCorrectAnswer(int theta, Item item) {
    double a = item.getA();
    double b = item.getB();
    double c = item.getC();

    double probability = (1 - c) / (1 + Math.exp(-1.7 * a * (theta - b)));
    probability = c + probability;

    return probability;
}