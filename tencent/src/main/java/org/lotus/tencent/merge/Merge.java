package org.lotus.tencent.merge;

import java.util.Random;

/**
 * Created by quanchengyun on 2018/12/3.
 */
public class Merge {

    public static class Node {
        public int data;
        public Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * 时间c为比较一次和修改一次指针的时间
     * T = c(N1+N2) 无占用额外空间
     * @param head1
     * @param head2
     * @return
     */
    public static Node merge(Node head1 ,Node head2){
        if(head1==null){
            return head2==null?null: head2;
        }
        if(head2==null){
            return head1==null?null: head1;
        }
        Node head = null;
        if(head1.data<=head2.data){
            head = head1;
            head1 = head1.next;
        }else{
            head = head2;
            head2 = head2.next;
        }
        Node _head = head;
        while(head1!=null || head2!=null){
            if(head1==null){
                while(head2.data==head.data && head2!=null){
                    head2=head2.next;
                }
                head.next=head2;
                break;
            }
            if(head2==null){
                while(head1.data==head.data && head1!=null){
                    head1=head1.next;
                }
                head.next=head1;
                break;
            }
            if(head1.data<=head2.data){
                if(head1.data>head.data){
                    head.next=head1;
                    head = head.next;
                }
                head1 =head1.next;
            }
            else{
                if(head2.data>head.data){
                    head.next=head2;
                    head = head.next;
                }
                head2 = head2.next;
            }
        }
        return _head;
    }


    public static void main(String[] args){
        test1();
        test2();
    }



    static void test1(){
        Random r = new Random();
        Node head1 = new Node(r.nextInt(7),null);
        Node head2 = new Node(r.nextInt(9),null);
        Node _head1 = head1;
        Node _head2 = head2;
        for(int i=0;i<5-1;i++){
            _head1.next = new Node(_head1.data+r.nextInt(101),null);
            _head1 = _head1.next;
            _head2.next = new Node(_head2.data+r.nextInt(101),null);
            _head2 = _head2.next;
        }
        Node $head1 = head1;
        while($head1!=null){
            System.out.print($head1.data);
            System.out.print(" ");
            if ($head1.next==null)break;
            $head1 = $head1.next;
        }
        System.out.print("\n");
        Node $head2 = head2;
        while($head2!=null){
            System.out.print($head2.data);
            System.out.print(" ");
            if ($head2.next==null)break;
            assert $head2.data<$head2.next.data;
            $head2 = $head2.next;
        }
        System.out.print("\n");
        //
        Node merged = merge(head1,head2);
        Node _merged = merged;
        while(_merged!=null){
            System.out.print(_merged.data);
            System.out.print(" ");
            if (_merged.next==null)break;
            assert _merged.data<_merged.next.data;
            _merged = _merged.next;
        }
        System.out.print("\n");
    }


    //
    static void test2(){

        Node head1 = new Node(3,null);
        Node head2 = new Node(1,new Node(3,new Node(4,null)));
        Node _head1 = head1;
        Node _head2 = head2;
        //
        Node merged = merge(head1,head2);
        Node _merged = merged;
        while(_merged!=null){
            for(int i=0;i<100;i++){
                if(_merged==null)break;
                System.out.print(_merged.data);
                System.out.print(" ");
                assert _merged.data<_merged.next.data;
                _merged = _merged.next;
            }
            System.out.print("\n");
        }
    }


}
