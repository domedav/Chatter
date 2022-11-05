package com.domedav.chatter.ui.login;
import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.domedav.DataManager;
import com.domedav.chatter.R;
import com.domedav.chatter.databinding.ActivityLoginBinding;
import com.domedav.chatter.ui.login.credentials.CredentialValidator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private boolean CanUndo = false;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;

    private boolean passwordError = true;
    private boolean usernameError = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);    //toggle if theres info which shouldnt be shared (eg: visible password)
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginButton = binding.login;
        passwordEditText = binding.password;
        usernameEditText = binding.username;

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if(CanUndo)
                {
                    CanUndo = false;
                    setContentView(binding.getRoot());
                    CredentialValidator.GoodForProceed_Username = false;
                    CredentialValidator.GoodForProceed_Password = false;
                    usernameEditText.setText(DataManager.GetData_String(getApplicationContext(), "Login", "usernameEditText_restore_data"));
                    ValidateUsername(binding.userContainer, DataManager.GetData_String(getApplicationContext(), "Login", "usernameEditText_restore_data"));
                    return;
                }
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        usernameEditText.setText(DataManager.GetData_String(this, "Login", "usernameEditText_restore_data"));
        ValidateUsername(binding.userContainer, DataManager.GetData_String(this, "Login", "usernameEditText_restore_data"));
        InitEvents();

        //anim bg
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.loginmain).getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(6000);
        animationDrawable.start();
    }

    private void InitEvents(){

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                ValidateUsername(binding.userContainer, s.toString());

                ColorStateList csl = ContextCompat.getColorStateList(getApplicationContext(), usernameError ? R.color.outlining : R.color.error);
                binding.userContainer.setStartIconTintList(csl);
                binding.userContainer.setCounterTextColor(csl);
                binding.userContainer.setEndIconTintList(csl);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                CredentialValidator.GoodForProceed_Password = CredentialValidator.ValidatePasswordLength(s.toString().length());
                binding.passContainer.setError(CredentialValidator.GoodForProceed_Password ? null : getString(R.string.invalid_password));
                passwordError = CredentialValidator.GoodForProceed_Password;

                ColorStateList csl = ContextCompat.getColorStateList(getApplicationContext(), passwordError ? R.color.outlining : R.color.error);
                binding.passContainer.setStartIconTintList(csl);
                binding.passContainer.setCounterTextColor(csl);
                binding.passContainer.setEndIconTintList(csl);

                UpdateLoginButton();
            }
        });

        usernameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            ColorStateList csl = ContextCompat.getColorStateList(getApplicationContext(), usernameError ? (hasFocus ? R.color.outlining : R.color.outlining_semitransparent) : R.color.error);
            binding.userContainer.setStartIconTintList(csl);
            binding.userContainer.setCounterTextColor(csl);
            binding.userContainer.setEndIconTintList(csl);
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            ColorStateList csl = ContextCompat.getColorStateList(getApplicationContext(), passwordError ? (hasFocus ? R.color.outlining : R.color.outlining_semitransparent) : R.color.error);
            binding.passContainer.setStartIconTintList(csl);
            binding.passContainer.setCounterTextColor(csl);
            binding.passContainer.setEndIconTintList(csl);
            binding.passContainer.setHintTextColor(csl);
        });

        loginButton.setOnClickListener(v -> {
            //login
            setContentView(R.layout.activity_login_email);
            ((TextView)findViewById(R.id.usernamedisplay)).setText(getString(R.string.username_display, usernameEditText.getText().toString()));
            CanUndo = true;
            DataManager.SetData(this, "Login", "usernameEditText_restore_data", usernameEditText.getText().toString());
        });
    }

    private void UpdateLoginButton(){
        binding.login.setEnabled(CredentialValidator.GoodForProceed_Password && CredentialValidator.GoodForProceed_Username);
    }

    private void ValidateUsername(TextInputLayout usernameEditText, String username){
        CredentialValidator.LoginUsernameErrorType errorType = CredentialValidator.ValidateUsername(username);
        switch (errorType)
        {
            case username_short:
                usernameEditText.setError(getString(R.string.invalid_username_tooshort));
                CredentialValidator.GoodForProceed_Username = false;
                usernameError = false;
                break;
            case username_long:
                usernameEditText.setError(getString(R.string.invalid_username_toolong));
                CredentialValidator.GoodForProceed_Username = false;
                usernameError = false;
                break;
            case username_invalid:
                usernameEditText.setError(getString(R.string.invalid_username_nonascii));
                CredentialValidator.GoodForProceed_Username = false;
                usernameError = false;
                break;
            case ignore:
                usernameEditText.setError(null);
                CredentialValidator.GoodForProceed_Username = true;
                usernameError = true;
                break;
        }
        UpdateLoginButton();
    }
}