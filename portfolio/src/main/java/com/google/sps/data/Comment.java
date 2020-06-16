package com.google.sps.data;

import javax.servlet.http.HttpServletRequest;
import com.google.appengine.api.datastore.Entity;

/**
 * A class for storing a Comment.
 */
public class Comment {
    private String nickname;
    private String text;
    private long timestamp;
    private String imageSrc;
    private float happyScore;

    public Comment(long timestamp, String nickname, String text, String imageSrc, float happyScore) {
        this.timestamp = timestamp;
        this.nickname = nickname;
        this.text = text;
        this.imageSrc = imageSrc;
        this.happyScore = happyScore;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long time) {
        this.timestamp = time;
    }

    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setHappyScore(float happyScore) {
        this.happyScore = happyScore;
    }

    public float getHappyScore() {
        return happyScore;
    }

    /**
     * Creates a Comment from a Datastore entity.
    */
    public static Comment makeComment(Entity entity) {
        long timestamp = (long) entity.getProperty("timestamp");
        String nickname = (String) entity.getProperty("nickname");
        String text = (String) entity.getProperty("text");
        String imageSrc = (String) entity.getProperty("imageSrc");
        float happyScore = ((Double) entity.getProperty("happyScore")).floatValue();
        Comment c = new Comment(timestamp, nickname, text, imageSrc, happyScore);
        return c;
    }

    /*
     * Gets a the parameter's value from the request or a default value if the request 
     * does not contain the parameter.
     */
    private static String getParamOrDefault(HttpServletRequest request, String paramName, String revert) {
        final String paramValue = request.getParameter(paramName);
        if (paramValue == null) {
            return revert;
        }
        return paramValue;
    }
}