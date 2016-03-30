package org.resec.algorithms;

import edu.princeton.cs.algs4.*;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validateNullInput(G);
        digraph = G;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateNullInput(v, w);

        // consider finding the common ancestor by starting two bfs from the two
        // sources on the diagraph until they meet each other
        int[] vmarked = new int[digraph.V()];
        int[] wmarked = new int[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            vmarked[i] = -1;
            wmarked[i] = -1;
        }

        Queue<Integer> vq = new Queue<>();
        Queue<Integer> wq = new Queue<>();
        vq.enqueue(v);
        wq.enqueue(w);

        while (!vq.isEmpty() || !wq.isEmpty()) {
            // start a new round of expanding for v
            expand(vmarked, vq);

            // start a new round of expanding for w
            expand(wmarked, wq);

            // check if they meet each other
            for (int i = 0; i < digraph.V(); i++) {
                if (vmarked[i] != -1 && wmarked[i] != -1) {
                    // found the common ancestor, also should be nearest one

                }
            }
        }


        return 0;
    }

    private void expand(int[] marked, Queue<Integer> q) {
        int witer = q.size();
        while (witer > 0) {
            int from = q.dequeue();
            for (Integer to : digraph.adj(from)) {
                if (marked[to] != -1) {
                    marked[to] = marked[from] + 1;
                    q.enqueue(to);
                }
            }
            witer--;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateNullInput(v, w);
        return 0;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateNullInput(v, w);

        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateNullInput(v, w);
        return 0;
    }

    private void validateNullInput(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new NullPointerException("At least one input is null");
            }
        }
    }
}