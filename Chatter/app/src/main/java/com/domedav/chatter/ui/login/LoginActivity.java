package com.domedav.chatter.ui.login;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.domedav.DataManager;
import com.domedav.chatter.R;
import com.domedav.chatter.databinding.ActivityLoginBinding;
import com.domedav.chatter.login.credentials.CredentialValidator;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.splashscreen.SplashScreen;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private boolean CanUndo = false;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Handler mainScreenDelayAnim;
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
                    findViewById(R.id.fade).setBackground(getDrawable(R.drawable.animated_layoutswitch_fadeanim_in));
                    ((AnimatedVectorDrawable)findViewById(R.id.fade).getBackground()).start();
                    mainScreenDelayAnim.postDelayed(()->{
                        setContentView(binding.getRoot());
                        HandleBackButton();
                    },250);
                    return;
                }
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        usernameEditText.setText(DataManager.GetData_String(this, "Login", "usernameEditText_previous_data"));
        ValidateUsername(binding.userContainer, DataManager.GetData_String(this, "Login", "usernameEditText_previous_data"));
        InitEvents();

        //anim bg
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.loginmain).getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(6000);
        animationDrawable.start();

        mainScreenDelayAnim = new Handler();
        mainScreenDelayAnim.postDelayed(() -> {binding.fade.setBackground(getDrawable(R.drawable.animated_layoutswitch_fadeanim_out)); ((AnimatedVectorDrawable)binding.fade.getBackground()).start();}, 250);
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
                ValidatePassword(s.toString());
            }
        });

        loginButton.setOnClickListener(v -> {
            //login
            binding.fade.setBackground(getDrawable(R.drawable.animated_layoutswitch_fadeanim_in));
            ((AnimatedVectorDrawable)binding.fade.getBackground()).start();
            mainScreenDelayAnim.postDelayed(() ->
            {
                setContentView(R.layout.activity_login_email);
                findViewById(R.id.fade).setBackground(getDrawable(R.drawable.animated_layoutswitch_fadeanim_out));
                ((AnimatedVectorDrawable)findViewById(R.id.fade).getBackground()).start();
                ((TextView)findViewById(R.id.usernamedisplay)).setText(getString(R.string.username_almostthere, usernameEditText.getText().toString()));
                CanUndo = true;
                DataManager.SetData(this, "Login", "usernameEditText_previous_data", usernameEditText.getText().toString());

                TextInputLayout emailContainer = (TextInputLayout)findViewById(R.id.emailContainer);

                //anim email phone
                AnimationDrawable animationDrawable = (AnimationDrawable)(emailContainer.getStartIconDrawable());
                animationDrawable.setEnterFadeDuration(1100);
                animationDrawable.setExitFadeDuration(650);
                animationDrawable.start();
            },250);
        });
    }

    private void UpdateLoginButton(){
        binding.login.setEnabled(CredentialValidator.GoodForProceed_Password && CredentialValidator.GoodForProceed_Username);
    }

    private void ValidateUsername(TextInputLayout usernameEditText, String username){
        if(username.isEmpty()){return;}
        CredentialValidator.LoginUsernameErrorType errorType = CredentialValidator.ValidateUsername(username);
        switch (errorType)
        {
            case username_short:
                usernameEditText.setError(getString(R.string.invalid_username_tooshort));
                CredentialValidator.GoodForProceed_Username = false;
                break;
            case username_long:
                usernameEditText.setError(getString(R.string.invalid_username_toolong));
                CredentialValidator.GoodForProceed_Username = false;
                break;
            case username_invalid:
                usernameEditText.setError(getString(R.string.invalid_username_nonascii));
                CredentialValidator.GoodForProceed_Username = false;
                break;
            case ignore:
                usernameEditText.setError(null);
                CredentialValidator.GoodForProceed_Username = true;
                break;
        }
        UpdateLoginButton();
    }

    private void ValidatePassword(String s){
        CredentialValidator.GoodForProceed_Password = CredentialValidator.ValidatePasswordLength(s.length());
        binding.passContainer.setError(CredentialValidator.GoodForProceed_Password ? null : getString(R.string.invalid_password));

        UpdateLoginButton();
    }

    private void HandleBackButton(){
        binding.fade.setBackground(getDrawable(R.color.layouttransition_top));
        mainScreenDelayAnim.postDelayed(() -> {binding.fade.setBackground(getDrawable(R.drawable.animated_layoutswitch_fadeanim_out)); ((AnimatedVectorDrawable)binding.fade.getBackground()).start();}, 250);
        CredentialValidator.GoodForProceed_Username = false;
        CredentialValidator.GoodForProceed_Password = false;
        usernameEditText.setText(DataManager.GetData_String(getApplicationContext(), "Login", "usernameEditText_previous_data"));
        ValidateUsername(binding.userContainer, DataManager.GetData_String(getApplicationContext(), "Login", "usernameEditText_previous_data"));
        ValidatePassword(binding.password.toString());
        UpdateLoginButton();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("TAG", "onConfigurationChanged: " + newConfig.toString());
        HandleBackButton();
    }
}