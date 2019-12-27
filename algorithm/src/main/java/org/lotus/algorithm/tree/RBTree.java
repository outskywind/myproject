package org.lotus.algorithm.tree;

import com.sun.xml.internal.org.jvnet.mimepull.CleanUpExecutorFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    static  class CompareUtil{
         static <T> int compare( T key1,T key2){
             if (key1 instanceof String){
                 return ((String)key1).compareTo((String)key2);
             }else{
                 Integer v1 = (Integer)key1;
                 Integer v2 = (Integer)key2;
                 return  v1 -  v2;
             }
         }
    }


    Node root = null;

    public <T> void insert(T key){
        Node<T> node = new Node<>();
        node.key=key;
        node.isBlack=false;
        //从根节点开始遍历插入
        if(root==null) {
            root=node;
            node.isBlack=true;
            return ;
        }
        Node ip = root;
        //小的左子树，>=的右子树
        while(ip!=null){
            if (CompareUtil.compare(node.key,ip.key)<0){
                if (ip.left==null) {
                    //
                    ip.left = node;
                    node.parent = ip;
                    break;
                }
                ip = ip.left;
            }
            else{
                if (ip.right==null){
                    ip.right = node;
                    node.parent = ip;
                    break;
                }
                ip=ip.right;
            }
        }
        //pp 父节点
        fixup_insert(node);
    }

    /**
     *  插入的是红节点 需要检查 是否违反性质 红的孩子都是黑
     * @param node
     * @param <T>
     */
    private <T> void fixup_insert(Node<T> node) {
        //如果父节点是黑的，就没有违反性质
        if (node.parent.isBlack) return;
        //
        Node p = node.parent;
        while (!p.isBlack){
            Node uncle = null;
            if (p == p.parent.left) {
                uncle = p.parent.right;
            }else{
                uncle = p.parent.left;
            }
            //1 如果叔节点也是红的，变色 ，上移
            if (uncle!=null && !uncle.isBlack){
                p.parent.isBlack=false;
                p.isBlack=true;
                uncle.isBlack=true;
                p =p.parent.parent;
            }
            //如果叔节点是黑的，就要旋转
            else{
                //插入的是右节点，左旋一下
                if(p == p.parent.left){
                    if (node == p.right){
                        left_rotate(p);
                    }
                    //移动p指针
                    p = p.parent;
                    p.isBlack=false;
                    node.isBlack=true;
                    right_rotate(p);
                } else if(p == p.parent.right){
                    if(node == p.left){
                        right_rotate(p);
                    }
                    p = p.parent;
                    p.isBlack=false;
                    node.isBlack=true;
                    left_rotate(p);
                }
                //此时
                p=node;
            }
        }
        //root强制黑色
        root.isBlack = true;
    }

    /**
     *  以这个节点原点，将右轴左旋
     * @param p
     */
    private void left_rotate(Node p) {
        if(p.parent==null){
            root = p.right;
        }
        else if (p.parent.left==p){
            p.parent.left = p.right;
        }else{
            p.parent.right = p.right;
        }
        p.right.parent = p.parent;
        p.parent = p.right;
        Node pr = p.right;
        p.right = p.right.left;
        pr.left = p;
    }

    /**
     *  左轴右旋,与右轴左旋 对称的
     * @param p
     */
    private void right_rotate(Node p) {
        if(p.parent==null){
            root = p.left;
        }
        else if (p.parent.left==p){
            p.parent.left = p.left;
        }else{
            p.parent.right = p.left;
        }
        p.left.parent = p.parent;
        p.parent = p.left;
        Node pr = p.left;
        p.left = p.left.right;
        pr.right = p;
    }



    public void  printTree(){
        List<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            print(queue);
        }

    }

    private void print(List<Node> queue){
        //广度优先,队头出队
        int size = queue.size();
        for(int i=0;i<size;i++){
            Node node = queue.remove(0);
            System.out.print(node.key+" ");
            if (node.left!=null){
                queue.add(node.left);
            }
            if (node.right!=null){
                queue.add(node.right);
            }
            if(!node.key.equals("|")){
                Node split = new Node();
                split.key="|";
                queue.add(split);
            }

        }
        System.out.println();
    }

}
