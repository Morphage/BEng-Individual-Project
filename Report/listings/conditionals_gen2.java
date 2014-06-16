String randomVar = ...
String solutionValue = ...
String difficulty = ...

ArrayList<String> choicesList = ....
Collections.shuffle(choicesList);

xmlExercise += "</value>\n"
    + "    </display>\n"
    + "    <display>\n"
    + "        <view>Multiple Choice</view>\n"
    + "        <value>What is the final value of variable " 
    + randomVar + "?</value>\n"
    + "        <choice0>" + choicesList.get(0) + "</choice0>\n"
    + "        <choice1>" + choicesList.get(1) + "</choice1>\n"
    + "        <choice2>" + choicesList.get(2) + "</choice2>\n"
    + "        <choice3>" + choicesList.get(3) + "</choice3>\n"
    + "        <solution>" + solutionValue + "</solution>\n"
    + "    </display>\n"
    + "    <display>\n"
    + "        <difficulty>" + difficulty + "</difficulty>\n"
    + "    </display>\n"
    + "</exercise>";