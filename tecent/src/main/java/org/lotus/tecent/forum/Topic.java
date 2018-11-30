package org.lotus.tecent.forum;

import java.util.Date;

/**
 *
 * 发布帖子主题对象
 * Created by quanchengyun on 2018/11/30.
 */
public class Topic {
    //帖子主题id
    String id;
    //发帖主题作者
    User user;
    //发帖主题内容
    String content;
    //发帖时间
    Date time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
