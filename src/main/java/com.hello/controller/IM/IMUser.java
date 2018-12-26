package com.hello.controller.IM;

public class IMUser {
    int Id;
    String username;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    @Override
    public String toString() {
        return "IMUser{" +
                "Id=" + Id +
                ", username='" + username + '\'' +
                '}';
    }
}
