package model;

import javax.annotation.Nullable;

public class User {
    private String UID;
    private String firstName;
    private String lastName;
    private String email;
    private String nickname;

    public User(String UID, String firstName, String lastName, String email, @Nullable String nickname) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.UID = UID;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUID() {
        return UID;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
