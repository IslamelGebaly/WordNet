import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.Arrays;

public class WordNet {

    // constructor takes the name of the two input files
    private Digraph G;
    private ST<String, Integer> synsets;

    private boolean isRooted() {
        boolean rootFound = false;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0 && !rootFound) {
                rootFound = true;
            } else if (rootFound) {
                return false;
            }
        }
        return rootFound;
    }

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null)
            throw new IllegalArgumentException();
        if (hypernyms == null)
            throw new IllegalArgumentException();

        In synsetFile = new In(synsets);
        In hypernymFile = new In(hypernyms);
        this.synsets = new ST<String, Integer>();
        String s;
        int i = 0;
        for (String line : synsetFile.readAllLines()) {
            s = Arrays.stream(line.split(",")).toArray()[1].toString();
            for (String word : s.split(" "))
                this.synsets.put(word, i);
            i++;
        }

        G = new Digraph(i);
        int v, w;
        String[] temp;
        for (String line : hypernymFile.readAllLines()) {
            temp = line.split(",");
            v = Integer.parseInt(temp[0]);
            for (i = 0; i < temp.length; i++) {
                w = Integer.parseInt(temp[i]);
                G.addEdge(v, w);
            }
        }

        if (!isRooted())
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.synsets.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return synsets.get(word) == null ? false : true;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();

        return "";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        wn.sap(null, "ns");
    }
}
