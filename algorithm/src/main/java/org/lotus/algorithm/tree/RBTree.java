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
        FIXUP_INSERT(node);
    }

    /**
     *  插入的是红节点 需要检查 是否违反性质 红的孩子都是黑
     * @param node
     * @param <T>
     */
    private <T> void FIXUP_INSERT(Node<T> node) {
        //如果父节点是黑的，就没有违反性质
        if (node.parent.isBlack) return;
        //
        while (node.parent!=null && !node.parent.isBlack){
            Node p = node.parent;
            Node uncle = null;
            if (p == p.parent.left) {
                uncle = p.parent.right;
            }else{
                uncle = p.parent.left;
            }
            //1 如果叔节点也是红的，变色 ，上移,本轮无旋转
            if (uncle!=null && !uncle.isBlack){
                p.parent.isBlack=false;
                p.isBlack=true;
                uncle.isBlack=true;
                //上移2层
                node=node.parent.parent;
                //旋转node一次
            }
            //如果叔节点是黑的，判断旋转1次
            else{
                //旋转准备下一次的旋转处理
                if (node == node.parent.left && node.parent==node.parent.parent.right){
                    node=node.parent;
                    right_rotate(node);
                }else if(node == node.parent.right && node.parent==node.parent.parent.left){
                    node=node.parent;
                    left_rotate(node);
                }
                else{
                    //最终的，变色，旋转1次。
                    node.parent.parent.isBlack=false;
                    node.parent.isBlack=true;
                    if (node.parent==node.parent.parent.left){
                        right_rotate(node.parent.parent);
                    }else if(node.parent==node.parent.parent.right){
                        left_rotate(node.parent.parent);
                    }
                }
            }
        }
        //root强制黑色
        root.isBlack = true;
    }

    //删除操作
    public <T> void remove(T key){
        Node ip = root;
        while(ip!=null){
            int cmp = CompareUtil.compare(key,ip.key);
            if (cmp<0){
                ip = ip.left;
            }
            else if(cmp==0){
                break;
            }else{
                ip = ip.right;
            }
        }
        if (ip==null)return;

        //1.如果 ip 无孩子, 或只有一个孩子
        if (ip.left==null || ip.right==null) {
            Node  v = ip.left==null?ip.right:ip.left;
            //ip 为根节点
            if (ip.parent==null){
                root = v;
            }else{
                //ip 非根节点
                if (ip==ip.parent.left){
                    ip.parent.left = v;
                }else{
                    ip.parent.right = v;
                }
                if (v!=null){
                    v.parent = ip.parent;
                }
            }
            return ;
        }

        //2. 2个孩子
       Node y =  find_min(ip);
       boolean original_isBlack = y.isBlack;
       //
        Node x = y.right;
        Node yp = y.parent;

        if (y==yp.left){
            yp.left = x;
        }else{
            yp.right =x;
        }
        if (x!=null) {
            x.parent = yp;
        }

        y.parent = ip.parent;
        //如果ip根节点
        if (ip.parent==null){
            root = y;
        }else{
            if (ip.parent.left==ip){
                ip.parent.left = y;
            }else {
                ip.parent.right = y;
            }
        }

        y.left = ip.left;
        y.right = ip.right;
        if (ip.left!=null){
            ip.left.parent=y;
        }
        if (ip.right!=null){
            ip.right.parent=y;
        }
        y.isBlack  = ip.isBlack;

        //如果y原本是黑节点，就要调整
        if (original_isBlack){
            FIXUP_REMOVE(x);
        }

    }

    /**
     * x 继承了y的黑色，为了维护红黑性质不变
     * @param x
     */
    private void FIXUP_REMOVE(Node x) {
        //非根，黑节点
        // x 如果是红，就退出循环，直接将x变黑即可
        while(x.isBlack && x.parent!=null){
            Node  b = getBorther(x);
            //x 没有兄弟节点。则x直接上移
            if(b==null){
                x = x.parent;
            }
            //case1   brother 为红 ，将 x.parent 旋转 ， x.parent 变红
            // b 变黑  b 更新节点指向
            else if(!b.isBlack){
                //左旋
                if(b==x.parent.right){
                    left_rotate(x.parent);
                }else {
                    //右旋
                    right_rotate(x.parent);
                }
                b.isBlack = true;
                x.parent.isBlack=false;
            }
            else{
                //2. brother 为黑 ， brother.right 为红


            }
            
        }
        //x继承黑色
        x.isBlack=true;
    }

    private Node getBorther(Node x) {
        if (x==x.parent.left)return x.parent.right;
        else return  x.parent.left;
    }

    private Node find_min(Node ip) {
        Node p = ip;
        Node c = ip.left==null?ip.right:ip.left;
        while (c!=null){
            p = c;
            c = c.left==null?c.right:c.left;
        }
        return p;
    }


    /**
     *  以这个节点原点，将右轴左旋
     * @param node
     */
    private void left_rotate(Node node) {
        //Node p = node.parent;
        if(node.parent==null){
            root = node.right;
        }
        else if (node.parent.left==node){
            node.parent.left = node.right;
        }else{
            node.parent.right = node.right;
        }
        node.right.parent = node.parent;
        node.parent = node.right;
        Node pr = node.right;
        node.right = node.right.left;
        pr.left = node;
    }

    /**
     *  左轴右旋,与右轴左旋 对称的
     * @param node
     */
    private void right_rotate(Node node) {

        if(node.parent==null){
            root = node.left;
        }
        //1.修改parent的对应指针
        else if (node.parent.left==node){
            node.parent.left = node.left;
        }else{
            node.parent.right = node.left;
        }
        node.left.parent = node.parent;
        //修改本节点parent指针
        node.parent = node.left;
        //修改左右子指针
        Node pr = node.left;
        node.left = node.left.right;
        pr.right = node;
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
            System.out.print(node.key+"("+(node.isBlack?"B":"R")+")"+" ");
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
