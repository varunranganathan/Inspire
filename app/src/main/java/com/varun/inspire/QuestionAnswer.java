package com.varun.inspire;

/**
 * Created by vvvro on 1/2/2017.
 */

public class QuestionAnswer {
    private String question;
    private String answer;

    public QuestionAnswer(String tQuestion, String tAnswer){
        question = tQuestion;
        answer = tAnswer;
    }

    public String getQuestion(){
        return question;
    }

    public String getAnswer(){
        return answer;
    }
}
