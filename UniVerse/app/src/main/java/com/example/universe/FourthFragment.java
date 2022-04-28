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

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        thisContext = getContext();

        findPreference("updatePass").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(thisContext);
                final String newPass = sp.getString("updatePass","");

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(thisContext, "Password successfully updated!", duration);
                            toast.show();
                        }
                        else {
                            Toast toast = Toast.makeText(thisContext, "Password update Failed.",duration);
                            toast.show();
                        }
                    }
                });
                return true;
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
    }
}
