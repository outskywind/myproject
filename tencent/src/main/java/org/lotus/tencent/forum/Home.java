package org.lotus.tencent.forum;

import java.util.List;

/**
 * 首页对象
 * Created by quanchengyun on 2018/11/30.
 */
public class Home {
    //所属用户
    User user;
    //用户的最近10条直接回复
    List<Comment> latestReply;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getLatestReply() {
        return latestReply;
    }

    public void setLatestReply(List<Comment> latestReply) {
        this.latestReply = latestReply;
    }
}
