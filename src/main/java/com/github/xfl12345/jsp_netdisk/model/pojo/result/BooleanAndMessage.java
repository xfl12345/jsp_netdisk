package com.github.xfl12345.jsp_netdisk.model.pojo.result;

import com.github.xfl12345.jsp_netdisk.appconst.api.result.FileApiResult;

public class BooleanAndMessage {
    public boolean success;
    public String message;

    public BooleanAndMessage(){}

    public BooleanAndMessage(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public BooleanAndMessage(BooleanAndMessage booleanAndMessage) {
        setBooleanAndMessage(booleanAndMessage);
    }

    public void setBooleanAndMessage(BooleanAndMessage booleanAndMessage){
        this.message = booleanAndMessage.message;
        this.success = booleanAndMessage.success;
    }

    public BooleanAndMessage(FileApiResult fileApiResult){
        setFileApiResult(fileApiResult);
    }

    public void setFileApiResult(FileApiResult fileApiResult){
        this.success = fileApiResult.equals(FileApiResult.SUCCEED);
        this.message = fileApiResult.getName();
    }
}
