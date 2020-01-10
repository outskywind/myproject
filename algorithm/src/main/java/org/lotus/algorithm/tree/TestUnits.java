package org.lotus.algorithm.tree;

import org.junit.Test;

public class TestUnits {


    @Test
    public void  testRBTree(){
        RBTree tree = new RBTree();

        tree.insert("c");
        tree.insert("b");
        tree.insert("a");
        tree.insert("f");
        tree.insert("d");
        tree.insert("e");
        tree.insert("z");
        tree.insert("y");
        tree.insert("x");
        tree.insert("w");

        tree.printTree();

        tree.remove("f");

        tree.printTree();

    }
}
