package classifiers;

import robot.Body;
import robot.Head;
import robot.ItemInHand;
import robot.Robot;

import java.util.Arrays;
import java.util.List;

public class Classification {
    private final List<Robot> friendBots;
    private final List<Robot> enemyBots;

    public Classification(List<Robot> friendBots, List<Robot> enemyBots) {
        this.friendBots = friendBots;
        this.enemyBots = enemyBots;
    }

    public static void main(String[] args) {
        List<Robot> friendBots = Arrays.asList(
                new Robot(true, true, Body.CIRCLE, Head.TRIANGLE, ItemInHand.EMPTY),
                new Robot(true, false, Body.TRIANGLE, Head.CIRCLE, ItemInHand.FLOWER),
                new Robot(true, true, Body.CIRCLE, Head.CIRCLE, ItemInHand.EMPTY)
        );

        List<Robot> enemyBots = Arrays.asList(
                new Robot(false, true, Body.SQUARE, Head.TRIANGLE, ItemInHand.BALL),
                new Robot(false, true, Body.SQUARE, Head.CIRCLE, ItemInHand.SWORD),
                new Robot(true, true, Body.SQUARE, Head.SQUARE, ItemInHand.EMPTY),
                new Robot(false, true, Body.CIRCLE, Head.CIRCLE, ItemInHand.SWORD)
        );

        Classification classifier = new Classification(friendBots, enemyBots);
        Robot robotToClassify = new Robot(true, true, Body.SQUARE, Head.TRIANGLE, ItemInHand.FLOWER);

        System.out.println("Klasifikace: " + classifier.classify(robotToClassify));

    }

    public String classify(Robot robot) {
        String classification = covering(robot);

        if ("Přítel".equals(classification)) {
            return "Přítel (covering)";
        } else if ("Nepřítel".equals(classification)) {
            return "Nepřítel (covering)";
        } else {
            double friendProbability = calculateProbability(robot, friendBots);
            double enemyProbability = calculateProbability(robot, enemyBots);

            if (friendProbability > enemyProbability) {
                return "Přítel (probability)";
            } else {
                return "Nepřítel (probability)";
            }
        }
    }

    private String covering(Robot robot) {
        for (Robot friendRobot : friendBots) {
            if (friendRobot.equals(robot)) {
                return "Přítel";
            }
        }
        for (Robot friendRobot : enemyBots) {
            if (friendRobot.equals(robot)) {
                return "Nepřítel";
            }
        }

        return null;
    }

    private double calculateProbability(Robot robot, List<Robot> robots) {
        double probability = 1.0;

        probability *= calculateAttributeProbability(robot.isSmiling(), getSmilingProbability(robots));
        probability *= calculateAttributeProbability(robot.hasTie(), getTieProbability(robots));
        probability *= calculateBodyProbability(robot, robots);
        probability *= calculateHeadProbability(robot, robots);
        probability *= calculateItemProbability(robot, robots);

        return probability;
    }

    private double calculateAttributeProbability(boolean value, double probability) {
        if (value) {
            return probability;
        } else {
            return 1 - probability;
        }
    }

    private double getSmilingProbability(List<Robot> robots) {
        int smilingCount = 0;
        for (Robot robot : robots) {
            if (robot.isSmiling()) {
                smilingCount++;
            }
        }
        return (double) smilingCount / robots.size();
    }

    private double getTieProbability(List<Robot> robots) {
        int tieCount = 0;
        for (Robot robot : robots) {
            if (robot.hasTie()) {
                tieCount++;
            }
        }
        return (double) tieCount / robots.size();
    }

    private double calculateBodyProbability(Robot robot, List<Robot> robots) {
        int bodyCount = 0;
        Body targetBody = robot.body();

        for (Robot robotInList : robots) {
            if (robotInList.body().equals(targetBody)) {
                bodyCount++;
            }
        }

        return (double) bodyCount / robots.size();
    }

    private double calculateHeadProbability(Robot robot, List<Robot> robots) {
        int headCount = 0;
        Head targetHead = robot.head();

        for (Robot robotInList : robots) {
            if (robotInList.head().equals(targetHead)) {
                headCount++;
            }
        }

        return (double) headCount / robots.size();
    }

    private double calculateItemProbability(Robot robot, List<Robot> robots) {
        int itemCount = 0;
        ItemInHand item = robot.itemInHand();

        for (Robot robotInList : robots) {
            if (robotInList.itemInHand().equals(item)) {
                itemCount++;
            }
        }

        return (double) itemCount / robots.size();
    }
}