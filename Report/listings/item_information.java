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