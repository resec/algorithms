package org.resec.algorithms;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Collections;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validateNullInput(G);
        digraph = G;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String file = "testing/digraph2.txt";
        In in = new In(file);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int length = sap.length(1, 3);
        int ancestor = sap.ancestor(1, 3);
        System.out.println(length);
        System.out.println(ancestor);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateNullInput(v, w);
        validateNonExistedVertex(v, w);

        Result result = findSAP(Collections.singletonList(v), Collections.singletonList(w));

        if (result != null) {
            return result.length;
        } else {
            return -1;
        }
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateNullInput(v, w);
        validateNonExistedVertex(v, w);

        Result result = findSAP(Collections.singletonList(v), Collections.singletonList(w));

        if (result != null) {
            return result.ancestor;
        } else {
            return -1;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateNullInput(v, w);
        validateNonExistedVertex(v);
        validateNonExistedVertex(w);

        Result result = findSAP(v, w);

        if (result != null) {
            return result.length;
        } else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateNullInput(v, w);
        validateNonExistedVertex(v);
        validateNonExistedVertex(w);

        Result result = findSAP(v, w);

        if (result != null) {
            return result.ancestor;
        } else {
            return -1;
        }
    }

    private void validateNullInput(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new NullPointerException("At least one input is null");
            }
        }
    }

    private void validateNonExistedVertex(int... vertexes) {
        for (int v : vertexes) {
            if (v < 0 || v > digraph.V() - 1) {
                throw new IndexOutOfBoundsException(String.format("Not existed vertex %d in the diagraph", v));
            }
        }
    }

    private void validateNonExistedVertex(Iterable<Integer> vertexes) {
        for (int v : vertexes) {
            if (v < 0 || v > digraph.V() - 1) {
                throw new IndexOutOfBoundsException(String.format("Not existed vertex %d in the diagraph", v));
            }
        }
    }

    private Result findSAP(Iterable<Integer> v, Iterable<Integer> w) {
        // consider finding the common ancestor by starting two bfs from the two
        // sources on the diagraph until they meet each other
        int[] vmarked = new int[this.digraph.V()];
        int[] wmarked = new int[this.digraph.V()];
        for (int i = 0; i < this.digraph.V(); i++) {
            vmarked[i] = -1;
            wmarked[i] = -1;
        }

        Queue<Integer> vq = new Queue<>();
        Queue<Integer> wq = new Queue<>();

        for (int s : v) {
            vmarked[s] = 0;
            vq.enqueue(s);
        }
        for (int s : w) {
            wmarked[s] = 0;
            wq.enqueue(s);
        }

        while (!vq.isEmpty() || !wq.isEmpty()) {
            // start a new round of expanding for v, w
            expand(vmarked, vq);
            expand(wmarked, wq);

            // check if they meet each other
            int length = Integer.MAX_VALUE;
            int ancestor = Integer.MAX_VALUE;
            for (int i = 0; i < this.digraph.V(); i++) {
                if (vmarked[i] != -1 && wmarked[i] != -1) {
                    if (length > vmarked[i] + wmarked[i]) {
                        length = vmarked[i] + wmarked[i];
                        ancestor = i;
                    }
                }
            }

            if (length != Integer.MAX_VALUE) {
                // found the nearest common ancestor
                return new Result(length, ancestor);
            }
        }

        return null;
    }

    private void expand(int[] marked, Queue<Integer> q) {
        // clean up current nodes in the queue
        int witer = q.size();
        while (witer > 0) {
            int from = q.dequeue();
            for (Integer to : digraph.adj(from)) {
                if (marked[to] == -1) {
                    marked[to] = marked[from] + 1;
                    q.enqueue(to);
                }
            }
            witer--;
        }
    }

    private class Result {

        final int length;
        final int ancestor;

        Result(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }
    }
}

