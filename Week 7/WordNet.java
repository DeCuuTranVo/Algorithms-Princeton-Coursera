import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BinarySearchST;

public class WordNet {
    private Digraph netDataStructure;
    private SeparateChainingHashST<String, Queue<Integer>> wordInToVertex;
    private BinarySearchST<Integer, String> synsetsDict;
    private SAP shortestPathSolver;
    private int numSynsets; // num of vertices
    private int numWords; // num of words in synsets

    // constructor takes the name of the two input files
    // The constructor should take time linearithmic (or better) in the input size.
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null))
            throw new IllegalArgumentException("synsets and hypernyms must not be null");
        // The input to the constructor does not correspond to a rooted DAG. ???

        this.synsetsDict = new BinarySearchST<Integer, String>();
        this.wordInToVertex = new SeparateChainingHashST<String, Queue<Integer>>();

        In inSynsets = new In(synsets);
        // int numVerticesSynsets = 0;

        while (!inSynsets.isEmpty()) {
            String rawLine = inSynsets.readLine();
            // The string look like this: 35126,diesel_oil diesel_fuel,a heavy mineral oil
            // used as fuel in diesel engines
            String[] rawLineElements = rawLine.split(",");

            // Extract key and value from each line; key: index of line; value: word array
            // separates by " " of that line
            int elementIndex = Integer.valueOf(rawLineElements[0]);
            this.synsetsDict.put(elementIndex, rawLineElements[1]);

            String[] elementWords = rawLineElements[1].split(" ");
            // Fill the wordInToVertex hash table with nouns
            for (int i = 0; i < elementWords.length; i++) {
                // if the elementInToVertex symbol table not contain the keys
                if (!wordInToVertex.contains(elementWords[i])) {
                    // Create a queue of integer relevant to a key string; add that <key, value>
                    // pait to the noun->vertexs of synsets hash map
                    Queue<Integer> integerValueQueue = new Queue<Integer>();
                    integerValueQueue.enqueue(elementIndex);
                    wordInToVertex.put(elementWords[i], integerValueQueue);

                } else {
                    // Create a queue
                    wordInToVertex.get(elementWords[i]).enqueue(elementIndex);
                }
            }
        }

        // // Data structure check for synsets symbol tables
        // System.out.println("Number of vertices synsets: " + this.wordDict.size());

        // for (Integer index : this.wordDict.keys()) {
        // System.out.print("Key: " + index);
        // System.out.print(" Value: ");
        // for (String word : this.wordDict.get(index)) {
        // System.out.print(word + " ");
        // }
        // System.out.println("");
        // if (index > 10) {
        // break;
        // }

        // // Data structure check for noun->queue<vertex> symbol tables
        // System.out.println("Number of words in the symbol tables: " +
        // this.wordInToVertex.size());

        // int reverseCounter = 10;
        // for (String keyInWordInToVertex : this.wordInToVertex.keys()) {
        // System.out.print("Key: " + keyInWordInToVertex);
        // System.out.print(" Value: ");
        // Queue<Integer> valueInWordInToVertex =
        // this.wordInToVertex.get(keyInWordInToVertex);
        // for (Integer valueElement : valueInWordInToVertex) {
        // System.out.print(valueElement + " ");
        // }
        // System.out.println("");

        // System.out.println("Counter: " + reverseCounter);
        // if (reverseCounter < 0) {
        // break;
        // } else {
        // reverseCounter--;
        // }
        // }

        this.numSynsets = this.synsetsDict.size();
        this.numWords = this.wordInToVertex.size();

        this.netDataStructure = new Digraph(this.numSynsets);

        In inHypernyms = new In(hypernyms);

        while (!inHypernyms.isEmpty()) {
            String rawLine = inHypernyms.readLine();
            String[] rawLineElements = rawLine.split(",");
            int elementIndex = Integer.parseInt(rawLineElements[0]);

            for (int i = 1; i < rawLineElements.length; i++) {
                // Add edge from elementIndex (source vertex) to rawLineElements[i] (destination
                // vertex)
                this.netDataStructure.addEdge(elementIndex, Integer.parseInt(rawLineElements[i]));
            }
        }

        // // // Data structure checks for synonyms Digraph
        // System.out.println("Number of Vertex: " + this.netDataStructure.V());
        // System.out.print("Number of Edge: " + this.netDataStructure.E());

        // for (int i = 0; i < this.netDataStructure.V(); i++) {
        // System.out.print(i + "->");
        // for (Integer j : this.netDataStructure.adj(i)) {
        // System.out.print(j + ", ");
        // }
        // System.out.println("");

        // if (i > 10) {
        // break;
        // }
        // }

        this.shortestPathSolver = new SAP(this.netDataStructure);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // All words
        return wordInToVertex.keys();
    }

    // is the word a WordNet noun?
    // The method isNoun() should run in time logarithmic (or better) in the number
    // of nouns.
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word must not be null");
        }
        // TODO
        return this.wordInToVertex.contains(word);
    }

    // distance between nounA and nounB (defined below)
    // The method distance() should run in time linear in the size of
    // the WordNet digraph.

    public int distance(String nounA, String nounB) {
        if ((!this.isNoun(nounA)) || (!this.isNoun(nounB))) {
            throw new IllegalArgumentException("nounA and nounB should be in the wordInToVertex keys");
        }
        Queue<Integer> verticesOfNounA = this.wordInToVertex.get(nounA);
        Queue<Integer> verticesOfNounB = this.wordInToVertex.get(nounB);

        int minDistanceBetweenTwoNouns = shortestPathSolver.length(verticesOfNounA, verticesOfNounB);
        return minDistanceBetweenTwoNouns;
    }

    // // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // // in a shortest ancestral path (defined below)
    // // The method sap() should run in time linear in the size of
    // the WordNet digraph.
    public String sap(String nounA, String nounB) {
        if ((!this.isNoun(nounA)) || (!this.isNoun(nounB))) {
            throw new IllegalArgumentException("nounA and nounB should be in the wordInToVertex keys");
        }
        Queue<Integer> verticesOfNounA = this.wordInToVertex.get(nounA);
        Queue<Integer> verticesOfNounB = this.wordInToVertex.get(nounB);

        int shortestCommonAncestor = shortestPathSolver.ancestor(verticesOfNounA, verticesOfNounB);
        String shortestCommonSynset = this.synsetsDict.get(shortestCommonAncestor);
        return shortestCommonSynset;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsetsInputFile = args[0];
        String hypernymsInputFile = args[1];

        WordNet myWordNet = new WordNet(synsetsInputFile, hypernymsInputFile);

        // Test nouns() method
        // int counter = 0;
        // for (String singleWord : myWordNet.nouns()) {
        // System.out.print(singleWord + " ");

        // if (counter > 10) {
        // break;
        // } else {
        // counter++;
        // }
        // }
        // System.out.println();

        // //Test isNoun() method
        System.out.println("Is the word \"Ninkharsag\" contained in the wordNet: " + myWordNet.isNoun("Ninkharsag"));
        System.out.println("Is the word \"Ninkharsah\" contained in the wordNet: " + myWordNet.isNoun("Ninkharsah"));

        // Test distance() method
        int testLengthSAPBetweenWords = myWordNet.distance("worm", "bird");
        System.out.println("Distance between worm and bird: " + testLengthSAPBetweenWords);

        testLengthSAPBetweenWords = myWordNet.distance("white_marlin", "mileage");
        System.out.println("Distance between white_marlin and mileage: " + testLengthSAPBetweenWords);

        // Test sap() method
        String testAncestorSAPBetweenWords = myWordNet.sap("individual", "edible_fruit");
        System.out.println(
                "Shortest common ancestor between individual and edible_fruit: " + testAncestorSAPBetweenWords);

    }
}