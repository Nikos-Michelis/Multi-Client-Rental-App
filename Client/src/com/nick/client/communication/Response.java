package com.nick.client.communication;

import com.nick.client.domain.Member;
import java.io.Serializable;

public class Response implements Serializable {
    private Boolean status;
    private String message;
    private String role;
    private Member user;

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

    public void setStatus(Boolean status) {
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