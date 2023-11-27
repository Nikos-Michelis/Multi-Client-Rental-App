package com.nick.server.communication;

import com.nick.server.domain.Member;

import java.io.Serializable;

public class Response implements Serializable {
    private Boolean status;
    private String message;
    private Member user;
    private String role;

    public Response() {
    }

    public Response(Boolean status, String message, Member user, String role) {
        this.status = status;
        this.message = message;
        this.user = user;
        this.role = role;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Member getUser() {
        return user;
    }

    public void setUser(Member user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}