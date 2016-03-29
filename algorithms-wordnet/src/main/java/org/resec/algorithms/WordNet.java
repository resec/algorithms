package org.resec.algorithms;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

// an immutable data type
public class WordNet {

    private final Bag<String> synsets = new Bag<>();
    private final SET<String> nouns = new SET<>();
    private final Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException("Input synsets or hypernyms is null");
        }

        this.parseSynset(synsets);

        digraph = new Digraph(this.synsets.size());

        this.parseHypernym(hypernyms);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsetsFile = "testing/synsets.txt";
        String hypernymsFile = "testing/hypernyms300K.txt";

        WordNet wordNet = new WordNet(synsetsFile, hypernymsFile);
        System.out.println(wordNet.digraph.E());
        System.out.println(wordNet.digraph.V());
        System.out.println(wordNet.synsets.size());
        System.out.println(wordNet.nouns.size());
        //assert wordNet.digraph.E() == 84505;
        //assert wordNet.digraph.V() == 82192;
        //assert wordNet.synsets.size() == 82192;
        //assert wordNet.nouns.size() == 119188;

    }

    private void parseSynset(String synsetsPath) {
        In synsetsIn = new In(synsetsPath);

        while (synsetsIn.hasNextLine()) {
            String synset = synsetsIn.readLine().split(",")[1];
            synsets.add(synset);

            for (String noun : synset.split(" ")) {
                nouns.add(noun);
            }
        }

        synsetsIn.close();
    }

    private void parseHypernym(String hypernymsPath) {
        In hypernymsIn = new In(hypernymsPath);

        while (hypernymsIn.hasNextLine()) {
            String[] nums = hypernymsIn.readLine().split(",");
            int to = Integer.valueOf(nums[0]);

            for (int i = 1; i < nums.length; i++) {
                digraph.addEdge(Integer.valueOf(nums[i]), to);
            }
        }

        hypernymsIn.close();
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("Input word is null");
        }

        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateInputNouns(nounA, nounB);

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateInputNouns(nounA, nounB);

        return null;
    }

    private void validateInputNouns(String... nouns) {
        for (String noun : nouns) {
            if (noun == null) {
                throw new NullPointerException("At least one input noun is null");
            }
            if (!isNoun(noun)) {
                throw new IllegalArgumentException("At least one input noun is not a noun in this wordnet");
            }
        }
    }
}