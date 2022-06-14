import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdArrayIO;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdArrayIO;

class BaseballElimination {
    private int numTeams;
    private int numTeamsExclusive; // not count the considering team
    private int numGames;
    private int numGamesExclusive; // not count the games of considering team
    private int winsOfExclusiveTeam; // number of wins of the considering team

    private SeparateChainingHashST<String, Integer> teamNames;
    private int[] wins; // number of wins of team i
    private int[] losses; // nummber of losses of team i
    private int[] remaining; // nummber of remaining games of team i
    private int[][] gamesLeft; // number of game left to play against team j of team i

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

        this.numGames = factorial(this.numTeams) / (factorial(2) * factorial(this.numTeams - 2));
        this.numGamesExclusive = factorial(this.numTeamsExclusive)
                / (factorial(2) * factorial(this.numTeamsExclusive - 2));
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

        int teamIndex = this.teamNames.get(team);
        return this.wins[teamIndex];
    } // number of wins for given team

    public int losses(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        int teamIndex = this.teamNames.get(team);
        return this.losses[teamIndex];
    } // number of losses for given team

    public int remaining(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        int teamIndex = this.teamNames.get(team);
        return this.remaining[teamIndex];
    } // number of remaining games for given team

    public int against(String team1, String team2) {
        if ((team1 == null) || (team2 == null))
            throw new IllegalArgumentException("team name cannot be null");

        int team1Index = this.teamNames.get(team1);
        int team2Index = this.teamNames.get(team2);

        return this.gamesLeft[team1Index][team2Index];
    } // number of remaining games between team1 and team2

    private int factorial(int number) {
        int fact = 1;
        for (int i = 1; i <= number; i++) {
            fact *= i;
        }
        return fact;
    }

    private int[] computeWinsExclusive(String exclusiveTeam) {
        if (exclusiveTeam == null)
            throw new IllegalArgumentException("exclusiveTeam name cannot be null");

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);
        StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);
        this.winsOfExclusiveTeam = this.wins[exclusiveTeamIndex];

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

        int exclusiveTeamIndex = this.teamNames.get(exclusiveTeam);
        StdOut.println("exclusiveTeamIndex: " + exclusiveTeamIndex);

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

    public boolean isEliminated(String team) {
        if (team == null)
            throw new IllegalArgumentException("team name cannot be null");

        int exclusiveTeamIndex = this.teamNames.get(team);

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
        StdOut.println("this.numGames: " + this.numGames);
        StdOut.println("this.numGamesExclusive: " + this.numGamesExclusive);
        StdOut.println("this.numTeams: " + this.numTeams);
        StdOut.println("this.numTeamsExclusive " + this.numTeamsExclusive);

        int numVertices = 1 + this.numGamesExclusive + this.numTeamsExclusive + 1;
        FlowNetwork flowNetwork = new FlowNetwork(numVertices);

        StdOut.println("flowNetwork: ");
        System.out.println(flowNetwork.toString());
        return false;
    }// is given team eliminated?

    // public Iterable<String> certificateOfElimination(String team) // subset R of
    // teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        // division.printHashTableKeys(division.teamNames);

        StdOut.println("wins: ");
        StdArrayIO.print(division.wins);
        StdOut.println("losses: ");
        StdArrayIO.print(division.losses);
        StdOut.println("remaining: ");
        StdArrayIO.print(division.remaining);
        StdOut.println("gamesLeft: ");
        StdArrayIO.print(division.gamesLeft);

        // System.out.println("numTeams: " + division.numTeams);
        // System.out.println("numGames: " + division.numGames);

        // String chosenTeamName = "Philadelphia";
        // String chosenTeamName = "Detroit";
        String chosenTeamName = "Toronto";
        // for (String teamName : division.teamNames.keys()) {
        // chosenTeamName = teamName;
        // break;
        // }
        // System.out.println("choosenTeamName: " + chosenTeamName);

        // System.out.println(division.isEliminated(chosenTeamName));
        StdOut.println("winsExclusive: ");
        StdArrayIO.print(division.computeWinsExclusive(chosenTeamName));
        StdOut.println("remainingExclusive: ");
        StdArrayIO.print(division.computeRemainingExclusive(chosenTeamName));
        StdOut.println("gamesLeftExclusive: ");
        StdArrayIO.print(division.computeGamesLeftExclusive(chosenTeamName));

        // for (String team : division.teams()) {
        // if (division.isEliminated(team)) {
        // StdOut.print(team + " is eliminated by the subset R = { ");
        // for (String t : division.certificateOfElimination(team)) {
        // StdOut.print(t + " ");
        // }
        // StdOut.println("}");
        // } else {
        // StdOut.println(team + " is not eliminated");
        // }
        // }
    }
}

// java BaseBallElimination.java baseball/teams4.txt