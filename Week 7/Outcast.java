import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNetDataStructure;

    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("wordnet must not be null");
        }

        this.wordNetDataStructure = wordnet;
    }
    // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        String outcastNoun = "";
        int outcastMaxDistance = -1;

        for (int i = 0; i < nouns.length; i++) {
            int cumulativeDistance = 0;

            for (int j = 0; j < nouns.length; j++) {
                if (i == j) {
                    continue;
                }
                cumulativeDistance += this.wordNetDataStructure.distance(nouns[i], nouns[j]);
            }

            if (cumulativeDistance > outcastMaxDistance) {
                outcastMaxDistance = cumulativeDistance;
                outcastNoun = nouns[i];
            }
        }
        return outcastNoun;
    } // given an array of WordNet nouns, return an outcast

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

// java Outcast.java wordnet/synsets.txt wordnet/hypernyms.txt
// wordnet/outcast5.txt wordnet/outcast8.txt wordnet/outcast11.txt