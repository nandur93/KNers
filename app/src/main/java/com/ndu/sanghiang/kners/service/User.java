package com.ndu.sanghiang.kners.service;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String nik;
    public String email;
    public String dept;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String nik, String email, String dept) {
        this.name = name;
        this.nik = nik;
        this.email = email;
        this.dept = dept;
    }
}
