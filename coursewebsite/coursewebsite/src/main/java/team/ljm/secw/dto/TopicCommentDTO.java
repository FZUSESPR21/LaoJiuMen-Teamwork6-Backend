package team.ljm.secw.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TopicCommentDTO {

    private int id;
    private int topicId;
    private String account;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releasedAt;

    private String name;

    @Override
    public String toString() {
        return "TopicCommentDTO{" +
                "id=" + id +
                ", topicId=" + topicId +
                ", account='" + account + '\'' +
                ", content='" + content + '\'' +
                ", releasedAt=" + releasedAt +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(Date releasedAt) {
        this.releasedAt = releasedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
