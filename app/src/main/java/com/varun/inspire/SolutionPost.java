package com.varun.inspire;

/**
 * Created by vvvro on 1/1/2017.
 */

public class SolutionPost {
    private int postID;
    private String postUserName;
    private String postUserEmail;
    private String postDescription;
    private String postDate;
    public SolutionPost(){

    }
    public SolutionPost(int tPostID, String tPostUserName, String tPostUserEmail, String tPostDescription, String tPostDate){
        postID = tPostID;
        postUserName = tPostUserName;
        postUserEmail = tPostUserEmail;
        postDescription = tPostDescription;
        postDate = tPostDate;
    }
    public int getPostID(){
        return postID;
    }
    public String getPostUserName(){
        return postUserName;
    }
    public String getPostUserEmail(){
        return getPostUserEmail();
    }
    public String getPostDescription(){
        return postDescription;
    }
    public String getPostDate(){
        return postDate;
    }
}
