package com.example.isaksayyed.messenger;

public class Users {

    String name;
    String status;

    public Users(){

    }

    public Users(String name, String status) {
        this.name = name;
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
