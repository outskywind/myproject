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
        Node head = head1.data<=head2.data? head1:head2;
        if(head1.data<=head2.data){
            head1 = head1.next;
        }else{
            head2 = head2.next;
        }
        Node _head = head;
        while(head1!=null || head2!=null){
            if(head1==null){
                head.next=head2;
                break;
            }
            if(head2==null){
                head.next=head1;
                break;
            }
            //all not tail
            if(head1.data<=head2.data){
                head.next  = head1;
                head1 =head1.next;
            }
            else{
                head.next = head2;
                head2 = head2.next;
            }
            head = head.next;
        }
        return _head;
    }


    public static void main(String[] args){
        Random r = new Random();
        Node head1 = new Node(r.nextInt(7),null);
        Node head2 = new Node(r.nextInt(9),null);
        Node _head1 = head1;
        Node _head2 = head2;
        for(int i=0;i<10000-1;i++){
            _head1.next = new Node(_head1.data+r.nextInt(101),null);
            _head1 = _head1.next;
            _head2.next = new Node(_head2.data+r.nextInt(101),null);
            _head2 = _head2.next;
        }
        //
        Node merged = merge(head1,head2);
        Node _merged = merged;
        while(_merged.next!=null){
            for(int i=0;i<100;i++){
                System.out.print(_merged.data);
                System.out.print(" ");
                if (_merged.next==null)break;
                _merged = _merged.next;
            }
            System.out.print("\n");
        }

    }


}
