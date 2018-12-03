package org.lotus.tencent.forum;

import java.util.Date;

/**
 *
 * 回帖对象
 * Created by quanchengyun on 2018/11/30.
 */
public class Comment {
    //id
    String commentId;
    //用户id
    String userId;
    //本条回帖内容
    String content;
    //针对本回帖的回帖
    Comment child;
    //本回帖回复的对象
    Comment parent;
    //本帖子所属的主题对象
    Topic tpoic;
    //回帖时间
    Date time;


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Comment getChild() {
        return child;
    }

    public void setChild(Comment child) {
        this.child = child;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Topic getTpoic() {
        return tpoic;
    }

    public void setTpoic(Topic tpoic) {
        this.tpoic = tpoic;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
