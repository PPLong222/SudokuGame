package com.example.sudoku;

import org.litepal.crud.DataSupport;

public class RecordBitmap extends DataSupport {
    /**
        originurl: the url of the recycleview image
        answerurl: the url of the answer recycleview image
        data:      completion date
        level:     level of this Soduku
        completedata: time you complete the Soduku
     */
    private String originurl;
    private String answerurl;
    private String date;
    private String completedate;
    private int level;

    public String getOriginurl() {
        return originurl;
    }

    public void setOriginurl(String originurl) {
        this.originurl = originurl;
    }

    public String getAnswerurl() {
        return answerurl;
    }

    public void setAnswerurl(String answerurl) {
        this.answerurl = answerurl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompletedate() {
        return completedate;
    }

    public void setCompletedate(String completedate) {
        this.completedate = completedate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
