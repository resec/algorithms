package org.resec.algorithms;

import edu.princeton.cs.algs4.In;

public class Outcast {

    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new NullPointerException("Input wordnet is null");
        }

        this.wordNet = wordnet;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            System.out.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new NullPointerException("Input nouns is null");
        }
        if (nouns.length == 0) {
            throw new IllegalArgumentException("Input nouns is empty");
        }

        int maxD = Integer.MIN_VALUE;
        String outcast = null;
        for (String nounA : nouns) {
            int d = Integer.MIN_VALUE;
            for (String nounB : nouns) {
                d += wordNet.distance(nounA, nounB);
            }
            if (maxD < d) {
                maxD = d;
                outcast = nounA;
            }
        }

        return outcast;
    }
}