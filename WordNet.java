import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WordNet {

    private Digraph G;
    private ST<String, Integer> synsetLookup;
    String[] synsets;

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

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        //Handling corner cases
        //If the one or both arguements are null
        //then the program will throw an illegal arguement exception
        if (synsets == null)
            throw new IllegalArgumentException();
        if (hypernyms == null)
            throw new IllegalArgumentException();

        In synsetFile = new In(synsets);
        In hypernymFile = new In(hypernyms);
        this.synsetLookup = new ST<String, Integer>();
        this.synsets = synsetFile.readAllLines();

        String s;
        for (int i = 0; i < this.synsets.length; i++) {
            s = Arrays.stream(this.synsets[i].split(",")).toArray()[1].toString();
            synsetLookup.put(s, i);
            i++;
        }

        G = new Digraph(this.synsets.length);
        int v, w;
        String[] temp;
        for (String line : hypernymFile.readAllLines()) {
            temp = line.split(",");
            v = Integer.parseInt(temp[0]);
            for (int i = 1; i < temp.length; i++) {
                w = Integer.parseInt(temp[i]);
                G.addEdge(v, w);
            }
        }

        if (!isRooted())
            throw new IllegalArgumentException();

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.synsetLookup.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        return this.synsetLookup.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();
        SAP sap = new SAP(G);
        return sap.length(synsetLookup.get(nounA), synsetLookup.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();

        SAP sap = new SAP(G);
        int vertix = sap.ancestor(synsetLookup.get(nounA), synsetLookup.get(nounB));
        return synsets[vertix];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println(wn.distance("water", "waterfront"));
    }
}
