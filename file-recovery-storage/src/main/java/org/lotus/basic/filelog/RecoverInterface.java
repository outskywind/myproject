package org.lotus.basic.filelog;

/**
 * Created by quanchengyun on 2018/2/26.
 */
public interface RecoverInterface<T> {

    /**
     * 返回恢复重试的结果，true表示成功 ，false表示失败
     * @param object
     * @return
     */
     boolean recover(T object);
}
