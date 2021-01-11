package model;

import javax.annotation.Nullable;

public class User {
    private String UID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public User(String UID, String firstName, String lastName, String email, @Nullable String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.UID = UID;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
