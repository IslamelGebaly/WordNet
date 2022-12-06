import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    private int max(int[] distances) {
        int max = distances[0];
        int index = 0;
        for (int i = 0; i < distances.length; i++) {
            if (max < distances[i]) {
                max = distances[i];
                index = i;
            }
        }
        return index;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int distances[] = new int[nouns.length];
        int d;
        for (int i = 0; i < nouns.length; i++) {
            d = 0;
            for (int j = 0; j < nouns.length; j++) {
                d += wordnet.distance(nouns[i], nouns[j]);
            }

            distances[i] = d;
        }

        return nouns[max(distances)];
    }

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
