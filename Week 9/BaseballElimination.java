import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdArrayIO;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class BaseballElimination {
    private int numTeams;
    private int numTeamsExclusive; // not count the considering team
    private int numGames;
    private int numGamesExclusive; // not count the games of considering team
    private int winsOfExclusiveTeam; // number of wins of the considering team
    private int remainingOfExclusiveTeam; // number of remaining games of the considering team
    private FordFulkerson maxFlowMinCutAlgorithm;
    private FlowNetwork flowNetwork;
    private int numVertices;
    private int sourceVertex;
    private int terminalVertex;
    private int firstGameVertex;
    private int firstTeamVertex;

    private SeparateChainingHashST<String, Integer> teamNames;
    private int[] wins; // number of wins of team i
    private int[] losses; // nummber of losses of team i
    private int[] remaining; // nummber of remaining games of team i
    private int[][] gamesLeft; // number of game left to play against team j of team i

    private SeparateChainingHashST<String, Integer> teamNamesExclusive;
    private int[] winsExclusive;
    private int[] remainingExclusive;
    private int[][] gamesLeftExclusive;

    public BaseballElimination(String filename) {
        // Exeption
        if (filename == null)
            throw new IllegalArgumentException("filename cannot be null");

        // initiate and populate intances variables
        In inputFile = new In(filename);
        this.numTeams = inputFile.readInt(); //
        this.numTeamsExclusive = this.numTeams - 1;

        teamNames = new SeparateChainingHashST<String, Integer>();
        wins = new int[this.numTeams];
        losses = new int[this.numTeams];
        remaining = new int[this.numTeams];
        gamesLeft = new int[this.numTeams][this.numTeams];

        // for (int teamIndex = 0; teamIndex < this.numTeams; teamIndex++) {
        // gamesLeft[teamIndex][teamIndex] = -1;
        // }

        for (int teamIndex = 0; teamIndex < this.numTeams; teamIndex++) {
            String teamName = inputFile.readString();
            teamNames.put(teamName, teamIndex);
            wins[teamIndex] = inputFile.readInt();
            losses[teamIndex] = inputFile.readInt();
            remaining[teamIndex] = inputFile.readInt();
            for (int otherTeamIndex = 0; otherTeamIndex < this.numTeams; otherTeamIndex++) {
                gamesLeft[teamIndex][otherTeamIndex] = inputFile.readInt();
            }
        }

        this.numGames = (this.numTeams - 1) * (this.numTeams) / 2;
        this.numGamesExclusive = (this.numTeamsExclusive - 1) * (this.numTeamsExclusive) / 2;
    } // create a baseball division from given filename in format specified below

    private void printHashTableKeys(SeparateChainingHashST<String, Integer> hashTable) {
        for (String key : hashTable.keys()) {
            System.out.print(key + " ");
        }
        System.out.println(" ");
    }

    public int numberOfTeams() {
        return this.numTeams;
    } // number of teams

    public Iterable<String> teams() {
        return teamNames.keys();
    } // all teams

    public int wins(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int teamIndex = this.teamNames.get(team);
        return this.wins[teamIndex];
    } // number of wins for given team

    public int losses(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int teamIndex = this.teamNames.get(team);
        return this.losses[teamIndex];
    } // number of losses for given team

    public int remaining(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int teamIndex = this.teamNames.get(team);
        return this.remaining[teamIndex];
    } // number of remaining games for given team

    public int against(String team1, String team2) {
        if ((team1 == null) || (team2 == null))
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team1) || !this.teamNames.contains(team2)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int team1Index = this.teamNames.get(team1);
        int team2Index = this.teamNames.get(team2);

        return this.gamesLeft[team1Index][team2Index];
    } // number of remaining games between team1 and team2

    private SeparateChainingHashST<String, Integer> computeTeamNamesExclusive(String exclusiveTeam) {
        if (exclusiveTeam == null)
            throw new IllegalArgumentException("exclusiveTeam name cannot be null");

        if (!this.teamNames.contains(exclusiveTeam)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);
        this.teamNamesExclusive = new SeparateChainingHashST<String, Integer>();
        // Deep copy hash symbol table
        for (String eachTeamName : this.teamNames.keys()) {
            int eachTeamIndex = this.teamNames.get(eachTeamName);
            // System.out.println("(eachTeamName, eachTeamIndex): (" + eachTeamName + ", " +
            // eachTeamIndex + ")");

            if (eachTeamName.equals(exclusiveTeam)) {
                continue;
            } else if (eachTeamIndex > exclusiveTeamIndex) {
                this.teamNamesExclusive.put(eachTeamName, eachTeamIndex - 1);
            } else {
                this.teamNamesExclusive.put(eachTeamName, eachTeamIndex);
            }
        }

        // for (String eachTeamNameExclusive : this.teamNamesExclusive.keys()) {
        // int eachTeamIndexExclusive =
        // this.teamNamesExclusive.get(eachTeamNameExclusive);
        // // System.out.println(
        // // "(eachTeamNameExclusive, eachTeamIndexExclusive): (" +
        // eachTeamNameExclusive
        // // + ", "
        // // + eachTeamIndexExclusive + ")");
        // }

        return this.teamNamesExclusive;
    }

    private int[] computeWinsExclusive(String exclusiveTeam) {
        if (exclusiveTeam == null)
            throw new IllegalArgumentException("exclusiveTeam name cannot be null");

        if (!this.teamNames.contains(exclusiveTeam)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);
        // StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);

        this.winsExclusive = new int[this.numTeamsExclusive];

        int outIndex = 0;
        for (int inIndex = 0; inIndex < this.numTeams; inIndex++) {
            if (inIndex < exclusiveTeamIndex) {
                outIndex = inIndex;
            } else if (inIndex == exclusiveTeamIndex) {
                continue;
            } else {
                outIndex = inIndex - 1;
            }

            this.winsExclusive[outIndex] = this.wins[inIndex];
        }
        return this.winsExclusive;
    }

    private int[] computeRemainingExclusive(String exclusiveTeam) {
        if (exclusiveTeam == null)
            throw new IllegalArgumentException("exclusiveTeam name cannot be null");

        if (!this.teamNames.contains(exclusiveTeam)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);
        // StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);

        this.remainingExclusive = new int[this.numTeamsExclusive];

        int outIndex = 0;
        for (int inIndex = 0; inIndex < this.numTeams; inIndex++) {
            if (inIndex < exclusiveTeamIndex) {
                outIndex = inIndex;
            } else if (inIndex == exclusiveTeamIndex) {
                continue;
            } else {
                outIndex = inIndex - 1;
            }

            this.remainingExclusive[outIndex] = this.remaining[inIndex];
        }
        return this.remainingExclusive;
    }

    private int[][] computeGamesLeftExclusive(String exclusiveTeam) {
        if (exclusiveTeam == null)
            throw new IllegalArgumentException("exclusiveTeam name cannot be null");

        if (!this.teamNames.contains(exclusiveTeam)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);

        this.gamesLeftExclusive = new int[this.numTeamsExclusive][this.numTeamsExclusive];

        // int inclusiveRow = 0;
        // int inclusiveCol = 0;

        int exclusiveRow = 0;
        int exclusiveCol = 0;

        // StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);

        for (int inclusiveRow = 0; inclusiveRow < this.numTeams; inclusiveRow++) {
            // exclusiveRow++;
            for (int inclusiveCol = 0; inclusiveCol < this.numTeams; inclusiveCol++) {
                // exclusiveCol++;
                if (inclusiveRow < exclusiveTeamIndex) {
                    exclusiveRow = inclusiveRow;
                } else if (inclusiveRow == exclusiveTeamIndex) {
                    continue;
                } else {
                    exclusiveRow = inclusiveRow - 1;
                }

                if (inclusiveCol < exclusiveTeamIndex) {
                    exclusiveCol = inclusiveCol;
                } else if (inclusiveCol == exclusiveTeamIndex) {
                    continue;
                } else {
                    exclusiveCol = inclusiveCol - 1;
                }

                // StdOut.println("inclusive(Row, Col): (" + inclusiveRow + ", "
                // + inclusiveCol + ") ,exclusive(Row, Col): (" + exclusiveRow + ", " +
                // exclusiveCol + ") ");
                this.gamesLeftExclusive[exclusiveRow][exclusiveCol] = this.gamesLeft[inclusiveRow][inclusiveCol];
            }
        }
        return this.gamesLeftExclusive;
    }

    private int calculateTotalNumberOfGamesLeft(int[][] gamesLeftMatrix) {
        int accumulatedSum = 0;
        for (int row = 0; row < gamesLeftMatrix.length; row++) {
            for (int col = row + 1; col < gamesLeftMatrix[0].length; col++) {
                accumulatedSum += gamesLeftMatrix[row][col];
            }
        }
        return accumulatedSum;
    }

    public boolean isEliminated(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        this.winsOfExclusiveTeam = -1; // number of wins of the considering team
        this.remainingOfExclusiveTeam = -1; // number of remaining games of the
        // considering
        // // team
        this.maxFlowMinCutAlgorithm = null; //
        this.flowNetwork = null; //
        // this.teamNamesExclusive = null; //
        this.winsExclusive = null; //
        this.remainingExclusive = null; //
        this.gamesLeftExclusive = null; //

        int exclusiveTeamIndex = this.teamNames.get(team);
        this.winsOfExclusiveTeam = this.wins[exclusiveTeamIndex];
        this.remainingOfExclusiveTeam = this.remaining[exclusiveTeamIndex];
        // StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);

        computeWinsExclusive(team);
        // computeRemainingExclusive(team);
        computeGamesLeftExclusive(team);

        // Case 1: Explicit elimination
        for (int otherTeamIndex = 0; otherTeamIndex < this.numTeams; otherTeamIndex++) {
            if (otherTeamIndex == exclusiveTeamIndex) {
                continue;
            }

            if (wins[exclusiveTeamIndex] + remaining[exclusiveTeamIndex] < wins[otherTeamIndex]) {
                return true;
            }
        }

        // Case 2: Implicit elimination: Note: loop thourgh all vertices, exclude one
        // vertex at a time
        // StdOut.println("this.numGames: " + this.numGames);
        // StdOut.println("this.numGamesExclusive: " + this.numGamesExclusive);
        // StdOut.println("this.numTeams: " + this.numTeams);
        // StdOut.println("this.numTeamsExclusive " + this.numTeamsExclusive);

        this.numVertices = 1 + this.numGamesExclusive + this.numTeamsExclusive + 1;
        this.sourceVertex = 0;
        this.terminalVertex = numVertices - 1;
        this.flowNetwork = new FlowNetwork(numVertices);

        // StdOut.println("Element at (" + 0 + ", " + 1 + "): " +
        // this.gamesLeftExclusive[0][1]);
        // StdOut.println("Element at (" + 1 + ", " + 2 + "): " +
        // this.gamesLeftExclusive[1][2]);
        // StdOut.println("Element at (" + 2 + ", " + 3 + "): " +
        // this.gamesLeftExclusive[2][3]);
        // StdOut.println("numVerties: " + numVertices);
        // StdOut.println("numGamesExclusive: " + this.numGamesExclusive);
        // StdOut.println("numTeamsExclusive: " + this.numTeamsExclusive);

        // Add edge from source vertex to game vertices
        this.firstGameVertex = sourceVertex + 1; // first game vertex start at 1
        for (int rowTeam = 0, gameVertexCounter = firstGameVertex; rowTeam < this.numTeamsExclusive; rowTeam++) {
            for (int colTeam = rowTeam + 1; colTeam < this.numTeamsExclusive; colTeam++, gameVertexCounter++) {
                int rowColGamesLeft = this.gamesLeftExclusive[rowTeam][colTeam];
                // StdOut.println(
                // "rowTeam: " + rowTeam + " ,colTeam: " + colTeam + " ,gameVertexCounter: " +
                // gameVertexCounter);
                FlowEdge sourceToGamesEdge = new FlowEdge(sourceVertex, gameVertexCounter, rowColGamesLeft);
                flowNetwork.addEdge(sourceToGamesEdge);
            }
        }

        // Add edge from team vertices to terminal vertex
        this.firstTeamVertex = firstGameVertex + this.numGamesExclusive; // first team vertex is after last game vertex
        for (int indexTeam = 0,
                teamVertexCounter = firstTeamVertex; indexTeam < this.numTeamsExclusive; indexTeam++, teamVertexCounter++) {
            int indexTeamWin = this.winsExclusive[indexTeam];
            FlowEdge teamToTerminalEdge = new FlowEdge(teamVertexCounter, terminalVertex,
                    this.winsOfExclusiveTeam + this.remainingOfExclusiveTeam - indexTeamWin);
            flowNetwork.addEdge(teamToTerminalEdge);
        }

        // Add edge from game vertices to team vertices
        for (int rowTeam = 0,
                gameVertexCounter = firstGameVertex; rowTeam < this.numTeamsExclusive; rowTeam++) {
            for (int colTeam = rowTeam + 1; colTeam < this.numTeamsExclusive; colTeam++, gameVertexCounter++) {
                int rowTeamVertex = firstTeamVertex + rowTeam;
                int colTeamVertex = firstTeamVertex + colTeam;

                // StdOut.println("gameVertexCounter: " + gameVertexCounter);
                FlowEdge gameToRowTeamEdge = new FlowEdge(gameVertexCounter, rowTeamVertex,
                        Double.POSITIVE_INFINITY);
                FlowEdge gameToColTeamEdge = new FlowEdge(gameVertexCounter, colTeamVertex,
                        Double.POSITIVE_INFINITY);

                flowNetwork.addEdge(gameToRowTeamEdge);
                flowNetwork.addEdge(gameToColTeamEdge);
            }
        }

        // Max Flow - Min Cut algorithm
        this.maxFlowMinCutAlgorithm = new FordFulkerson(flowNetwork, sourceVertex, terminalVertex);
        double maxFlowValue = maxFlowMinCutAlgorithm.value();
        // StdOut.println("maxFlowValue: " + maxFlowValue);
        // Calculate number of games left
        int totalGamesLeftExclusive = calculateTotalNumberOfGamesLeft(this.gamesLeftExclusive);
        // StdOut.println("totalGamesLeftExclusive: " + totalGamesLeftExclusive);

        // StdOut.println("flowNetwork: ");
        // System.out.println(flowNetwork.toString());

        // // Empty temporary variables
        // this.winsExclusive = null;
        // this.remainingExclusive = null;
        // this.gamesLeftExclusive = null;

        // Elimination decision
        if (maxFlowValue < totalGamesLeftExclusive) {
            return true; // The given team is eliminated
        } else if (maxFlowValue == totalGamesLeftExclusive) {
            return false; // // The given team is not eliminated
        } else {
            throw new IllegalStateException("maxFlowValue can not be greater than totalGamesLeftExclusive");
        }
    }// is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        if (!this.teamNames.contains(team)) {
            throw new IllegalArgumentException("team name not found in hash table");
        }

        computeTeamNamesExclusive(team);
        // StdOut.println(this.maxFlowMinCutAlgorithm);
        if (isEliminated(team) == false) {// If team is not eliminated
            return null;
        } else if (this.maxFlowMinCutAlgorithm == null) { // if trivial eliminated? */
            // StdOut.println("Trivial Elimination: ");
            Queue<String> inCutTeamsQueue = new Queue<String>();
            for (String eachTeamNameExclusive : teamNamesExclusive.keys()) {
                int eachTeamIndexExclusive = teamNamesExclusive.get(eachTeamNameExclusive);
                // StdOut.println(this.wins[teamNames.get(team)]);
                // StdOut.println(this.remaining[teamNames.get(team)]);
                // StdOut.println(this.winsExclusive[eachTeamIndexExclusive]);
                if (this.wins[teamNames.get(team)]
                        + this.remaining[teamNames.get(team)] < this.winsExclusive[eachTeamIndexExclusive]) {
                    inCutTeamsQueue.enqueue(eachTeamNameExclusive);
                }
            }
            return inCutTeamsQueue;
        } else { // If team is nontrivial eliminated
            // StdOut.println("Non-trivial elimination: ");
            Queue<String> inCutTeamsQueue = new Queue<String>();

            // printHashTableKeys(teamNames);
            // printHashTableKeys(teamNamesExclusive);
            // StdOut.print(flowNetwork.toString());

            // StdOut.println("flowNetwork: ");
            // StdOut.println(this.flowNetwork.toString());

            // StdOut.println("Marked of FolkFullkerson: ");
            // StdArrayIO.print(this.maxFlowMinCutAlgorithm.getMarked());

            for (String eachTeamNameExclusive : teamNamesExclusive.keys()) {
                int eachTeamIndexExclusive = teamNamesExclusive.get(eachTeamNameExclusive);
                int eachTeamVertexExclusive = eachTeamIndexExclusive + this.firstTeamVertex;
                if (this.maxFlowMinCutAlgorithm.inCut(eachTeamVertexExclusive)) {
                    // StdOut.println("Team inCut: " + eachTeamIndexExclusive);
                    // StdOut.println("Index in mincut: " + eachTeamIndexExclusive + ", " +
                    // eachTeamNameExclusive);
                    inCutTeamsQueue.enqueue(eachTeamNameExclusive);
                } else {
                    // StdOut.println("Index: " + eachTeamIndexExclusive + ", " +
                    // eachTeamNameExclusive);
                }

            }

            // System.exit(-1);
            return inCutTeamsQueue; // ???
        }
    } // subset R of
      // teams that eliminates given team; null if not eliminated

    private boolean isCertificationTrue() {
        return false;
    }

    // public static void main(String[] args) {
    // BaseballElimination division = new BaseballElimination(args[0]);

    // // division.printHashTableKeys(division.teamNames);

    // // StdOut.println("wins: ");
    // // StdArrayIO.print(division.wins);
    // // // StdOut.println("losses: ");
    // // // StdArrayIO.print(division.losses);
    // // StdOut.println("remaining: ");
    // // StdArrayIO.print(division.remaining);
    // // StdOut.println("gamesLeft: ");
    // // StdArrayIO.print(division.gamesLeft);

    // // System.out.println("numTeams: " + division.numTeams);
    // // System.out.println("numGames: " + division.numGames);

    // // String chosenTeamName = "Philadelphia"; // teams4.txt
    // // String chosenTeamName = "Ghaddafi"; // teams4a.txt
    // // String chosenTeamName = "Detroit"; // teams5.txt
    // String chosenTeamName = "Philadelphia"; // teams5c.txt
    // // String chosenTeamName = "Ireland"; // teams7.txt
    // // String chosenTeamName = "Team1"; // teams24.txt
    // // String chosenTeamName = "Team13"; // teams24.txt
    // // String chosenTeamName = "Team25"; // teams32.txt
    // // String chosenTeamName = "Team50"; // teams54.txt
    // // for (String teamName : division.teamNames.keys()) {
    // // chosenTeamName = teamName;
    // // break;
    // // }
    // // System.out.println("choosenTeamName: " + chosenTeamName);

    // StdOut.println("Is the team " + chosenTeamName + " eliminated? -> " +
    // division.isEliminated(chosenTeamName));
    // // StdOut.println("winsExclusive: ");
    // // StdArrayIO.print(division.computeWinsExclusive(chosenTeamName));
    // // StdOut.println("remainingExclusive: ");
    // // StdArrayIO.print(division.computeRemainingExclusive(chosenTeamName));
    // // StdOut.println("gamesLeftExclusive: ");
    // // StdArrayIO.print(division.gamesLeftExclusive);
    // // StdArrayIO.print(division.computeGamesLeftExclusive(chosenTeamName));

    // // division.computeTeamNamesExclusive(chosenTeamName);

    // StdOut.println("Certificate of elimination: ");
    // for (String eachTeamName : division.certificateOfElimination(chosenTeamName))
    // {
    // StdOut.print(eachTeamName + " ");
    // }
    // StdOut.println("");
    // }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
                // StdOut.print(division.flowNetwork.toString());
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }

        StdOut.println("====================================================");
    }
}

// java BaseBallElimination.java baseball/teams5.txt