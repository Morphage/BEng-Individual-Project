public class IRTModule {
    private void computePosteriorDistribution(double[] knowledgeDistribution, 
	                                          Item item, int u_i) {
        for (int i = 0; i < knowledgeDistribution.length; i++) {
            knowledgeDistribution[i]
                    = numerator(i, item, knowledgeDistribution, u_i)
                    / denominator(item, knowledgeDistribution, u_i);
        }
    }

    private double numerator(int theta, Item item, 
	                         double[] knowledgeDistribution, int u_i) {
        if (u_i == 1) {
            return probabilityCorrectAnswer(theta, item)
                   * knowledgeDistribution[theta];
        } else {
            return (1 - probabilityCorrectAnswer(theta, item))
			       * knowledgeDistribution[theta];
        }
    }

    private double denominator(Item item, double[] knowledgeDistribution,
	                           int u_i) {
        double denominator = 0;

        for (int i = 0; i < knowledgeDistribution.length; i++) {
            denominator += numerator(i, item, knowledgeDistribution, u_i);
        }

        return denominator;
    }

    private double probabilityCorrectAnswer(int theta, Item item) {
        double a = item.getA();
        double b = item.getB();
        double c = item.getC();

        double probability = (1 - c) / (1 + Math.exp(-1.7 * a * (theta - b)));
        probability = c + probability;

        return probability;
    }

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
}
