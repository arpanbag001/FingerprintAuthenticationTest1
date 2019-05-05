package com.innovationredefined.fingerprintauthenticationtest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        Reprint.initialize(this);

        if (Reprint.isHardwarePresent()) {
            if (Reprint.hasFingerprintRegistered()) {
                textView.setText("Authenticating");
                Reprint.authenticate(new AuthenticationListener() {
                    @Override
                    public void onSuccess(int moduleTag) {
                        textView.setText("Authenticated");
                        Toast.makeText(getApplicationContext(), "Authenticated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(AuthenticationFailureReason failureReason, boolean fatal, CharSequence errorMessage, int moduleTag, int errorCode) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                textView.setText("Fingerprint not registered");
                Toast.makeText(getApplicationContext(), "Fingerprint not registered!\nPlease go to settings to register fingerprint", Toast.LENGTH_LONG).show();
            }
        } else {
            textView.setText("Fingerprint not supported");
            Toast.makeText(getApplicationContext(), "Fingerprint not supported", Toast.LENGTH_LONG).show();
        }

    }
}
