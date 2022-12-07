import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class WordNet {

    private final Digraph G;
    private final ST<String, ArrayList<Integer>> nouns;
    private final ArrayList<String> synsets;

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

    private void readSynsetFile(String synsetFile) {
        ///Reads Synset file and fills the synset list and the noun symbol table
        ///Synsets are added directly as is without spliting to the synset list
        ///The synset is split into its component words. Each word is a key that is
        // associated with multiple ids.

        In in = new In(synsetFile);

        String synset;
        ArrayList<Integer> idList;
        int id;
        String[] temp;

        for (String line : in.readAllLines()) {
            temp = line.split(",");
            id = Integer.parseInt(temp[0]);
            synset = temp[1];
            this.synsets.add(synset);

            for (String word : synset.split(" ")) {
                if (this.nouns.contains(word))
                    this.nouns.get(word).add(id);
                else {
                    idList = new ArrayList<>();
                    idList.add(id);
                    this.nouns.put(word, idList);
                }
            }
        }
    }

    private void readHyperNymFile(String hypernymFile) {
        ///Uses hypernym file to define edges between vertices
        In in = new In(hypernymFile);

        int v, w;
        String[] temp;
        for (String line : in.readAllLines()) {
            temp = line.split(",");
            v = Integer.parseInt(temp[0]);
            for (int i = 1; i < temp.length; i++) {
                w = Integer.parseInt(temp[i]);
                this.G.addEdge(v, w);
            }
        }
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

        this.nouns = new ST<>();
        this.synsets = new ArrayList<String>();

        readSynsetFile(synsets);

        this.G = new Digraph(this.synsets.size());

        readHyperNymFile(hypernyms);

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
        return this.nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();
        SAP sap = new SAP(G);
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB))
            throw new IllegalArgumentException();

        SAP sap = new SAP(G);
        int vertix = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        if (vertix == -1)
            return "-1";
        return synsets.get(vertix);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");

        StdOut.println(wn.sap("fluid", "liquid"));
    }
}
