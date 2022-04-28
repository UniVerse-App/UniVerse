package com.example.universe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FourthFragment extends PreferenceFragmentCompat {
    Context thisContext;
    int duration = Toast.LENGTH_SHORT;
    String toastText;
    String newPass;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        thisContext = getContext();
        findPreference("updatePass").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent(thisContext, ForgotPassword.class);
                startActivity(intent);
                return false;
            }
        });

        findPreference("updateInt").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent(thisContext, InterestsSelection.class);
                startActivity(intent);
                return true;
            }
        });

        findPreference("editInfo").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent(thisContext, EditStudentInfo.class);
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

        findPreference("NotiEnable").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // for Android 8 and above
                intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());

                startActivity(intent);

                return true;
            }
        });
    }
}
