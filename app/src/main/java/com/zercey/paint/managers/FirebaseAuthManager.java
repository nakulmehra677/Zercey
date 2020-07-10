package com.zercey.paint.managers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthManager {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    public FirebaseAuthManager() {
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }

    public boolean userExist() {
        return currentUser != null;
    }

    public String getUId() {
        if (userExist())
            return currentUser.getUid();
        return "";
    }

    public String contact() {
        return currentUser.getPhoneNumber();
    }

    public void logout() {
        auth.signOut();
    }
}
