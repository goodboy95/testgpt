package com.seekerhut.model.mysqlModel;

import java.io.Serializable;

/**
 * emotion_pic
 * @author 
 */
public class EmotionPic implements Serializable {
    private Integer emotionId;

    private String emotionToken;

    private String emotionPath;

    private static final long serialVersionUID = 1L;

    public Integer getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(Integer emotionId) {
        this.emotionId = emotionId;
    }

    public String getEmotionToken() {
        return emotionToken;
    }

    public void setEmotionToken(String emotionToken) {
        this.emotionToken = emotionToken;
    }

    public String getEmotionPath() {
        return emotionPath;
    }

    public void setEmotionPath(String emotionPath) {
        this.emotionPath = emotionPath;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EmotionPic other = (EmotionPic) that;
        return (this.getEmotionId() == null ? other.getEmotionId() == null : this.getEmotionId().equals(other.getEmotionId()))
            && (this.getEmotionToken() == null ? other.getEmotionToken() == null : this.getEmotionToken().equals(other.getEmotionToken()))
            && (this.getEmotionPath() == null ? other.getEmotionPath() == null : this.getEmotionPath().equals(other.getEmotionPath()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEmotionId() == null) ? 0 : getEmotionId().hashCode());
        result = prime * result + ((getEmotionToken() == null) ? 0 : getEmotionToken().hashCode());
        result = prime * result + ((getEmotionPath() == null) ? 0 : getEmotionPath().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", emotionId=").append(emotionId);
        sb.append(", emotionToken=").append(emotionToken);
        sb.append(", emotionPath=").append(emotionPath);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}