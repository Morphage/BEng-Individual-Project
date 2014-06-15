ArrayList<String> traversalOrders = new ArrayList<>();
traversalOrders.add(bst.inOrderTraversal());
traversalOrders.add(bst.preOrderTraversal());
traversalOrders.add(bst.postOrderTraversal());
traversalOrders.add(bst.levelOrderTraversal());

int rand = random.nextInt(4);
String traversalOrder = "post"/"pre"/"in"/"level" + " order";
String solutionTraversalOrder = traversalOrders.get(rand);
Collections.shuffle(traversalOrders);

xmlExercise += "</value>\n"
    + "    </display>\n"
    + "    <display>\n"
    + "        <view>Multiple Choice</view>\n"
    + "        <value>What should be printed if the binary tree is"
    + " traversed using the " + traversalOrder + " algorithm?</value>\n"
    + "        <choice0>" + traversalOrders.get(0) + "</choice0>\n"
    + "        <choice1>" + traversalOrders.get(1) + "</choice1>\n"
    + "        <choice2>" + traversalOrders.get(2) + "</choice2>\n"
    + "        <choice3>" + traversalOrders.get(3) + "</choice3>\n"
    + "        <solution>" + solutionTraversalOrder + "</solution>\n"
    + "    </display>\n"
    + "    <display>\n"
    + "        <difficulty>" + difficulty + "</difficulty>\n"
    + "    </display>\n"
    + "</exercise>";