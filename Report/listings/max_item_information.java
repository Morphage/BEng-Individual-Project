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