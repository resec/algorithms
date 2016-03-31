package org.resec.algorithms;

import edu.princeton.cs.algs4.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validateNullInput(G);
        digraph = G;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String file = "testing/digraph1.txt";
        In in = new In(file);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        int[] vs = new int[]{1, 2, 3, 4, 5, 11, 12, 7};

        List<Integer>[] ivs = new List[vs.length];
        int i = 0;
        for (int v : vs) {
            ivs[i] = Collections.singletonList(v);
            i++;
        }

        Output output = sap.findSAP(ivs);

        System.out.println(output);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateNullInput(v, w);
        validateNonExistedVertex(v, w);

        Output result = findSAP(Collections.singletonList(v), Collections.singletonList(w));

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

        Output result = findSAP(Collections.singletonList(v), Collections.singletonList(w));

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

        Output result = findSAP(v, w);

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

        Output result = findSAP(v, w);

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

    private Output findSAP(Iterable<Integer>... vs) {
        // consider finding the common ancestor by starting one bfs from each
        // source on the diagraph until they meet each other
        Bag<ST<Integer, Integer>> markeds = new Bag<>();
        Bag<Queue<Integer>> qs = new Bag<>();

        for (Iterable<Integer> v : vs) {
            ST<Integer, Integer> marked = new ST<>();
            Queue<Integer> q = new Queue<>();
            for (int s : v) {
                marked.put(s, 0);
                q.enqueue(s);
            }

            markeds.add(marked);
            qs.add(q);
        }

        boolean goon = true;
        while (goon) {
            // start a new round of expanding for all vs
            Iterator<ST<Integer, Integer>> imarked = markeds.iterator();
            Iterator<Queue<Integer>> iq = qs.iterator();

            while (imarked.hasNext()) {
                ST<Integer, Integer> marked = imarked.next();
                Queue<Integer> q = iq.next();
                // clean up current nodes in the queue
                int witer = q.size();
                while (witer > 0) {
                    int from = q.dequeue();
                    for (Integer to : digraph.adj(from)) {
                        if (marked.get(to) == null) {
                            marked.put(to, marked.get(from) + 1);
                            q.enqueue(to);
                        }
                    }
                    witer--;
                }
            }

            // check if they meet each other
            int length = Integer.MAX_VALUE;
            int ancestor = Integer.MAX_VALUE;
            for (int i = 0; i < this.digraph.V(); i++) {
                boolean found = true;
                int sum = 0;
                for (ST<Integer, Integer> marked : markeds) {
                    Integer value = marked.get(i);
                    if (value == null) {
                        found = false;
                        break;
                    }
                    sum += value;
                }

                if (found && length > sum) {
                    length = sum;
                    ancestor = i;
                }
            }

            if (length != Integer.MAX_VALUE) {
                // found the nearest common ancestor
                return new Output(length, ancestor);
            }

            goon = false;
            for (Queue<Integer> q : qs) {
                if (!q.isEmpty()) {
                    goon = true;
                    break;
                }
            }
        }

        return null;
    }

    private class Output {

        final int length;
        final int ancestor;

        Output(int length, int ancestor) {
            this.length = length;
            this.ancestor = ancestor;
        }

        @Override
        public String toString() {
            return "length: " + length + ", ancestor: " + ancestor;
        }
    }
}

