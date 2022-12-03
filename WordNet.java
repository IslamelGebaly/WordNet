import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class WordNet {

    // constructor takes the name of the two input files
    private Digraph G;
    private ST<String, Integer> nouns;
    private ArrayList<String> synsets;

    private boolean isRooted() {
        boolean rootFound = false;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0) {
                if (rootFound)
                    return false;
                else
                    rootFound = true;
            }
        }
        return rootFound;
    }

    private String getSynset(int id) {
        return this.synsets.get(id);
    }

    public WordNet(String synsets, String hypernyms) {

        if (synsets == null)
            throw new IllegalArgumentException();
        if (hypernyms == null)
            throw new IllegalArgumentException();

        In synsetFile = new In(synsets);
        In hypernymFile = new In(hypernyms);
        this.nouns = new ST<String, Integer>();
        this.synsets = new ArrayList<String>();
        String s;

        int i = 0;
        for (String line : synsetFile.readAllLines()) {
            s = Arrays.stream(line.split(",")).toArray()[1].toString();
            this.synsets.add(s);
            for (String word : s.split(" "))
                this.nouns.put(word, i);
            i++;
        }

        G = new Digraph(i);
        int v, w;
        String[] temp;
        for (String line : hypernymFile.readAllLines()) {
            temp = line.split(",");
            v = Integer.parseInt(temp[0]);
            for (i = 1; i < temp.length; i++) {
                w = Integer.parseInt(temp[i]);
                G.addEdge(v, w);
            }
        }

        if (!isRooted())
            throw new IllegalArgumentException();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return this.nouns.get(word) == null ? false : true;
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

        StdOut.println(wn.getSynset(83));
    }
}
