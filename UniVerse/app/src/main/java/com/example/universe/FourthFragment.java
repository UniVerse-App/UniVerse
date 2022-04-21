package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import java.io.*;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.auth.FirebaseAuth;

public class FourthFragment extends PreferenceFragmentCompat {
    Context thisContext;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        thisContext = getContext();

        findPreference("updatePass").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {



//                let user = firebase.auth().currentUser;
//                let newPassword = getASecureRandomPassword();
//
//                user.updatePassword(newPassword).then(() => {
//                        // Update successful.
//                }, (error) => {
//                    // An error happened.
//                });
                return true;
            }
        });

        findPreference("editInfo").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent(thisContext, RegisterStudentInfo.class);
                startActivity(intent);
                return true;
            }
        });

        findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(thisContext, LoginUser.class);
                startActivity(intent);
                return true;
            }
        });
    }
}
