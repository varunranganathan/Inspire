package com.varun.inspire;

/**
 * Created by vvvro on 1/1/2017.
 */

public class InspirePost {
    private int postID;
    private String postUserName;
    private String postUserEmail;
    private String postDescription;
    private String postDate;
    private int postType;
    private String postImageURL;
    public InspirePost(){

    }
    public InspirePost(int tPostID, String tPostUserName, String tPostUserEmail, String tPostDescription, String tPostDate){
        postID = tPostID;
        postUserName = tPostUserName;
        postUserEmail = tPostUserEmail;
        postDescription = tPostDescription;
        postDate = tPostDate;
        postImageURL = null;
        postType = 1;
    }
    public InspirePost(int tPostID, String tPostUserName, String tPostUserEmail, String tPostDescription, String tPostDate, String tPostImageURL){
        postID = tPostID;
        postUserName = tPostUserName;
        postUserEmail = tPostUserEmail;
        postDescription = tPostDescription;
        postDate = tPostDate;
        postImageURL = tPostImageURL;
        postType = 2;
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
    public String getPostImageURL(){
        return postImageURL;
    }
    public int getPostType(){
        return postType;
    }
}
