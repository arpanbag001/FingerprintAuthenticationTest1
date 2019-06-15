package com.innovationredefined.fingerprintauthenticationtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(R.id.rootView);
        textView = findViewById(R.id.text);

        Reprint.initialize(this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Reprint.cancelAuthentication();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        startListeningForFingerprint();
    }

    private void startListeningForFingerprint() {
        if (Reprint.isHardwarePresent()) {
            if (Reprint.hasFingerprintRegistered()) {
                textView.setText("Authenticating...");
                Snackbar.make(rootView, "Authenticating...", Snackbar.LENGTH_SHORT).show();
                Reprint.authenticate(new AuthenticationListener() {
                    @Override
                    public void onSuccess(int moduleTag) {
                        textView.setText("Authenticated!");
                        Snackbar.make(rootView, "Authenticated!", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int moduleTag, int errorCode) {
                        if (failureReason.equals(AuthenticationFailureReason.AUTHENTICATION_FAILED))
                            Snackbar.make(rootView, "Failed!", Snackbar.LENGTH_SHORT).show();
                        else if (failureReason.equals(AuthenticationFailureReason.LOCKED_OUT)) {
                            textView.setText("Too many wrong attempts!");
                            Snackbar.make(rootView, "Too many wrong attempts!\nPlease restart authentication!", Snackbar.LENGTH_LONG).show();
                        } else {
                            textView.setText(failureReason.name());
                            Snackbar.make(rootView, "Failed because: "+ failureReason.name(), Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                textView.setText("Fingerprint not registered");
                Snackbar.make(rootView, "No fingerprint is registered\nPress \"Go\" and click on \"Fingerprint\"", Snackbar.LENGTH_LONG)
                        .setAction("Go", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
                            }
                        })
                        .show();
            }
        } else {
            textView.setText("Fingerprint not supported!");
            Snackbar.make(rootView, "Fingerprint not supported!", Snackbar.LENGTH_SHORT).show();
        }
    }
}
