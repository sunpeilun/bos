package com.czxy.bos.domain.map;

public class BMapJsonResult {
    private Integer status;
    private String message;
    private MyResult[] result;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyResult[] getResult() {
        return result;
    }

    public void setResult(MyResult[] result) {
        this.result = result;
    }
}
