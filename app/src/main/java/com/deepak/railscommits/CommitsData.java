package com.deepak.railscommits;

/**
 * Created by Sharma on 6/2/2016.
 */
public class CommitsData {
    private String commitNumber, userName,   message;
    private boolean isLiked;
    public CommitsData(String commitNumber,String userName, String message,boolean isLiked){
        this.commitNumber = commitNumber;
        this.userName = userName;
        this.message = message;
        this.isLiked = isLiked;
    }

    public String getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(String commitNumber) {
        this.commitNumber = commitNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
