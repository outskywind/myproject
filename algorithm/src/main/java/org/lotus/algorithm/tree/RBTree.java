package org.lotus.algorithm.tree;

/**
 * 红黑搜索树
 *
 * 参见  算法导论
 * 红黑树：
 * 树与子树 的递归 关系
 *
 * 性质约束
 * 根为黑，叶子节点为黑 ， 任一节点X，到它所有叶子节点的所有最短路径的黑高相同
 * 红节点的子节点全黑
 *
 * --> 得出推论:
 * 1插入节点应该是红色，否则插入后会违背黑高相同的约束
 * 2节点到所有叶子路径高度，最高是最矮的最多2倍
 *
 * 假设路径边界证明
 */
public class RBTree {

    static class  Node<T>{
        T key;
        Node left;
        Node right;
        Node parent;
        //黑 或 红
        boolean isBlack=false;
    }

    static  class CompareUtil<T>{


         int compare( T key1,T key2){
             if (key1 instanceof String){
                 return ((String)key1).compareTo((String)key2);
             }
         }
    }


    Node root = null;

    public int insert(Node<T> node){
        node.isBlack=false;
        //从根节点开始遍历插入
        if(root==null) {
            root=node;
            return 0;
        }
        Node p = root;
        if (node.key)

    }




}
