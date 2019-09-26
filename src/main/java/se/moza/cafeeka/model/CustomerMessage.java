package se.moza.cafeeka.model;

import se.moza.cafeeka.model.audit.DateAudit;

public class CustomerMessage extends DateAudit {

    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;

    public CustomerMessage() { }

    public CustomerMessage(String name, String email, String phone, String subject, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return '\n' +
                "CustomerMessage:" + '\n' +
                " name: " + name + '\n' +
                " email: " + email + '\n' +
                " phone: " + phone + '\n' +
                " subject: " + subject + '\n' +
                " message: " + message + '\n';
    }
}
