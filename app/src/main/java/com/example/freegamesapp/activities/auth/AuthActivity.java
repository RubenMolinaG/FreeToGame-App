package com.example.freegamesapp.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freegamesapp.MainActivity;
import com.example.freegamesapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 1221;

    public static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();
        userSessionControl();
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [UI ELEMENTS]
    public void init() {
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FIREBASE LOGIN/REGISTER]
    public void login(View view) {
        String strEmail = etEmail.getText().toString();
        String strPassword = etPassword.getText().toString();

        if (!strEmail.isEmpty() && !strPassword.isEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(AuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please, fill the data.", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        String strEmail = etEmail.getText().toString();
        String strPassword = etPassword.getText().toString();

        if (!strEmail.isEmpty() && !strPassword.isEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(AuthActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(AuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please, fill the data.", Toast.LENGTH_SHORT).show();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [SIGN IN WITH GOOGLE]
    public void signInWithGoogle(View view) {

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("797058917152-q5ru2uhat0p6o8udhqk37tqtb7k2dabr.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(this, "Sign in Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(AuthActivity.this, "Firebase Error while Login.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [RECOVER PASSWORD]
    public void recoverPasswordActivity(View view) {
        startActivity(new Intent(this, RecoverPasswordActivity.class));
    }

    // ------------------------------------------------------------------------------------------------------------------------------- [FIREBASE USER SESSION CONTROL]
    private void userSessionControl() {
        if (firebaseUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}

/**
 *
 *
 * <Button
 *             android:id="@+id/btn_google"
 *             android:layout_width="match_parent"
 *             android:layout_height="wrap_content"
 *             android:text="Sign in with Google"
 *             android:textColor="#7E7E7E"
 *             android:background="@drawable/btn_login_form"
 *             android:drawableLeft="@mipmap/google_icon"
 *             android:onClick="signInWithGoogle"
 *             tools:ignore="OnClick"
 *             />
 *
 */