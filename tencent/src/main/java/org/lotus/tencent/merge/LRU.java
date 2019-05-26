package org.lotus.tencent.merge;

import java.util.HashMap;
import java.util.Map;

public class LRU {


    KV  cache = null;
    KV head=null;
    KV tail=null;
    Map<Integer,KV>  keyMapping = new HashMap<Integer,KV>();

    class KV {
        int  key;
        int value;
        KV pre;
        KV next;
    }


    //映射 key 索引下标， 移动key 到最后一个下标
    public KV get(int key){
        //KV find = cache[];
        KV find  = keyMapping.get(key);
        if(find ==null){
            return null;
        }
        //修改指针
        find.pre.next = find.next;
        tail.next = find;
        find.next = null;
        return find;
    }

    public void put(int key,int value){
        //加入Map
        //keyMapping.put();
    }





}
