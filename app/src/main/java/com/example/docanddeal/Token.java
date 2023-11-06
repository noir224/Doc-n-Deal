package com.example.docanddeal;


public class Token {
    private String userID;
    private String Tokenn;

    public Token(String userID, String token) {
        this.userID = userID;
        this.Tokenn = token;
    }

    public Token() {
    }

    public String getUserID() {
        return userID;
    }

    public String getToken() {
        return Tokenn;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setToken(String token) {
        this.Tokenn = token;
    }
}
