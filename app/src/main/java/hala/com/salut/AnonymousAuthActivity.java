package hala.com.salut;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-08-20
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity to demonstrate anonymous login and account linking (with an email/password account).
 */
public class AnonymousAuthActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "AnonymousAuth";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_auth);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


        // Click listeners
        findViewById(R.id.button_anonymous_sign_in).setOnClickListener(this);
        findViewById(R.id.button_anonymous_sign_out).setOnClickListener(this);

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    private void signInAnonymously() {
        showProgressDialog();
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END signin_anonymously]
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }



    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        TextView idView = (TextView) findViewById(R.id.anonymous_status_id);
        TextView emailView = (TextView) findViewById(R.id.anonymous_status_email);
        boolean isSignedIn = (user != null);

        // Status text
        if (isSignedIn) {
            idView.setText(getString(R.string.id_fmt, user.getUid()));
            emailView.setText(getString(R.string.email_fmt, user.getEmail()));
        } else {
            idView.setText(R.string.signed_out);
            emailView.setText(null);
        }

        // Button visibility
        findViewById(R.id.button_anonymous_sign_in).setEnabled(!isSignedIn);
        findViewById(R.id.button_anonymous_sign_out).setEnabled(isSignedIn);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_anonymous_sign_in) {
            signInAnonymously();
        } else if (i == R.id.button_anonymous_sign_out) {
            signOut();
        }
    }
}