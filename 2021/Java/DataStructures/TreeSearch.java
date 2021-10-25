package Searches;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class MonteCarloTreeSearch {
    public class Node {
        Node parent;
        ArrayList <Node> childNodes;
        boolean isPlayersTurn; // True if it is the player's turn.
        boolean playerWon; // True if the player won; false if the opponent won.
        int score;
        int visitCount;

        public Node() {}

        public Node(Node parent, boolean isPlayersTurn) {
            this.parent = parent;
            childNodes = new ArrayList<>();
            this.isPlayersTurn = isPlayersTurn;
            playerWon = false;
            score = 0;
            visitCount = 0;
        }
    }

    static final int WIN_SCORE = 10;
    static final int TIME_LIMIT = 500; // Time the algorithm will be running for (in milliseconds).

    public static void main(String[] args) {
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();

        mcts.monteCarloTreeSearch(mcts.new Node(null, true));
    }
  
    public Node monteCarloTreeSearch(Node rootNode) {
        Node winnerNode;
        double timeLimit;

        // Expand the root node.
        addChildNodes(rootNode, 10);

        timeLimit = System.currentTimeMillis() + TIME_LIMIT;

        // Explore the tree until the time limit is reached.
        while (System.currentTimeMillis() < timeLimit) {
            Node promisingNode;

            // Get a promising node using UCT.
            promisingNode = getPromisingNode(rootNode);

            // Expand the promising node.
            if (promisingNode.childNodes.size() == 0) {
                addChildNodes(promisingNode, 10);
            }

            simulateRandomPlay(promisingNode);
        }

        winnerNode = getWinnerNode(rootNode);
        printScores(rootNode);
        System.out.format("\nThe optimal node is: %02d\n", rootNode.childNodes.indexOf(winnerNode) + 1);

        return winnerNode;
    }

    public void addChildNodes(Node node, int childCount) {
        for (int i = 0; i < childCount; i++) {
            node.childNodes.add(new Node(node, !node.isPlayersTurn));
        }
    }

    public Node getPromisingNode(Node rootNode) {
        Node promisingNode = rootNode;

        // Iterate until a node that hasn't been expanded is found.
        while (promisingNode.childNodes.size() != 0) {
            double uctIndex = Double.MIN_VALUE;
            int nodeIndex = 0;
          
            for (int i = 0; i < promisingNode.childNodes.size(); i++) {
                Node childNode = promisingNode.childNodes.get(i);
                double uctTemp;

                if (childNode.visitCount == 0) {
                    nodeIndex = i;
                    break;
                }
                uctTemp = ((double) childNode.score / childNode.visitCount)
                            + 1.41 * Math.sqrt(Math.log(promisingNode.visitCount) / (double) childNode.visitCount);

                if (uctTemp > uctIndex) {
                    uctIndex = uctTemp;
                    nodeIndex = i;
                }
            }

            promisingNode = promisingNode.childNodes.get(nodeIndex);
        }

        return promisingNode;
    }
    public void simulateRandomPlay(Node promisingNode) {
        Random rand = new Random();
        Node tempNode = promisingNode;
        boolean isPlayerWinner;
        promisingNode.playerWon = (rand.nextInt(6) == 0);

        isPlayerWinner = promisingNode.playerWon;

        while (tempNode != null) {
            tempNode.visitCount++;

            if ((tempNode.isPlayersTurn && isPlayerWinner) ||
                (!tempNode.isPlayersTurn && !isPlayerWinner)) {
                tempNode.score += WIN_SCORE;
            }

            tempNode = tempNode.parent;
        }
    }

    public Node getWinnerNode(Node rootNode) {
        return Collections.max(rootNode.childNodes, Comparator.comparing(c -> c.score));
    }

    public void printScores(Node rootNode) {
        System.out.println("N.\tScore\t\tVisits");
        
        for (int i = 0; i < rootNode.childNodes.size(); i++) {
            System.out.println(String.format("%02d\t%d\t\t%d", i + 1,
             rootNode.childNodes.get(i).score, rootNode.childNodes.get(i).visitCount));
        }
    }
}
