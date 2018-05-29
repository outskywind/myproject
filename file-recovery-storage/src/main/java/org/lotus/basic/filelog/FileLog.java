package org.lotus.basic.filelog;


/**
 * Created by quanchengyun on 2018/2/9.
 */
public interface FileLog<T> {

     void  putRecord(T record);

     T  takeRecord();

     void recoverRecord(RecoverInterface<T> recoverInterface);


}
