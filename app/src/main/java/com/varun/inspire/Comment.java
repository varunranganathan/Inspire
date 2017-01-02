package com.varun.inspire;

/**
 * Created by vvvro on 1/2/2017.
 */

public class Comment {
    private int postID;
    private int cID;
    private String postUserName;
    private String postUserEmail;
    private String postDescription;
    private String postDate;
    public Comment(){

    }
    public Comment(int tPostID, int tCID, String tPostUserName, String tPostUserEmail, String tPostDescription, String tPostDate){
        postID = tPostID;
        cID = tCID;
        postUserName = tPostUserName;
        postUserEmail = tPostUserEmail;
        postDescription = tPostDescription;
        postDate = tPostDate;
    }
    public int getPostID(){
        return postID;
    }
    public int getCommentID(){
        return cID;
    }
    public String getPostUserName(){
        return postUserName;
    }
    public String getPostUserEmail(){
        return postUserEmail;
    }
    public String getPostDescription(){
        return postDescription;
    }
    public String getPostDate(){
        return postDate;
    }
}
