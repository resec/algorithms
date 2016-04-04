package org.resec.algorithms;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

// an immutable data type
public class WordNet {

    private final ST<String, Bag<Integer>> nouns = new ST<>();
    private final ST<Integer, String> synsets = new ST<>();
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException("Input synsets or hypernyms is null");
        }

        if (synsets.isEmpty() || hypernyms.isEmpty()) {
            throw new IllegalArgumentException("Input synsets or hypernyms is empty");
        }

        Digraph digraph = this.parseSynset(synsets);

        this.parseHypernym(hypernyms, digraph);

        sap = new SAP(digraph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsetsFile = "testing/synsets.txt";
        String hypernymsFile = "testing/hypernyms300K.txt";

        WordNet wordNet = new WordNet(synsetsFile, hypernymsFile);
        System.out.println(wordNet.nouns.size());
        //assert wordNet.digraph.E() == 84505;
        //assert wordNet.digraph.V() == 82192;
        //assert wordNet.synsets.size() == 82192;
        //assert wordNet.nouns.size() == 119188;

    }

    private Digraph parseSynset(String synsetsPath) {
        In synsetsIn = new In(synsetsPath);

        while (synsetsIn.hasNextLine()) {
            String[] line = synsetsIn.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String synset = line[1];
            this.synsets.put(id, synset);

            for (String noun : synset.split(" ")) {
                if (nouns.contains(noun)) {
                    nouns.get(noun).add(id);
                } else {
                    Bag<Integer> ids = new Bag<>();
                    ids.add(id);
                    nouns.put(noun, ids);
                }
            }
        }

        synsetsIn.close();

        return new Digraph(this.synsets.size());
    }

    private void parseHypernym(String hypernymsPath, Digraph digraph) {
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
        return this.sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateInputNouns(nounA, nounB);
        return synsets.get(this.sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
    }

    private void validateInputNouns(String... nouns) {
        for (String noun : nouns) {
            if (noun == null) {
                throw new NullPointerException("At least one input noun is null");
            }
            if (noun.isEmpty()) {
                throw new IllegalArgumentException("At least one input noun is empty");
            }
            if (!isNoun(noun)) {
                throw new IllegalArgumentException("At least one input noun is not a noun in this wordnet");
            }
        }
    }
}