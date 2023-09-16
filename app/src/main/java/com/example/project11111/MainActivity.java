package com.example.project11111;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    private Handler handler = new Handler();
    private boolean isValidationSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputLayout = findViewById(R.id.TextInputLayout);
        textInputEditText = findViewById(R.id.InputPassword);

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String passwordInput = charSequence.toString();

                // Define regex patterns for validation
                String lengthPattern = ".{8,}";
                String uppercasePattern = ".*[A-Z].*";
                String numericPattern = ".*\\d.*";
                String specialCharPattern = ".*[!@#$%*].*";

                // Initialize boolean flags for each validation criteria
                boolean hasLength = passwordInput.matches(lengthPattern);
                boolean hasUppercase = passwordInput.matches(uppercasePattern);
                boolean hasNumeric = passwordInput.matches(numericPattern);
                boolean hasSpecialChar = passwordInput.matches(specialCharPattern);

                // Construct the validation message with colored requirements
                String validationMessage =
                        (hasLength ? "<font color='#00FF00'>- At least 8 characters</font><br>" : "- At least 8 characters<br>") +
                                (hasUppercase ? "<font color='#00FF00'>- Minimum one uppercase letter</font><br>" : "- Minimum one uppercase letter<br>") +
                                (hasNumeric ? "<font color='#00FF00'>- Minimum one number</font><br>" : "- Minimum one number<br>") +
                                (hasSpecialChar ? "<font color='#00FF00'>- Minimum one special character (e.g., !@#$%*)</font>" : "- Minimum one special character (e.g., !@#$%*)<br>");

                // Update the text with HTML formatting for color
                textInputLayout.setHelperText(Html.fromHtml(validationMessage, Html.FROM_HTML_MODE_COMPACT));

                // Check if all criteria are met to clear the error message
                if (hasLength && hasUppercase && hasNumeric && hasSpecialChar) {
                    textInputLayout.setError("");
                    isValidationSuccessful = true;
                    // Delay displaying success message for a few seconds
                    handler.postDelayed(successMessageRunnable, 500); // Delay for 2 seconds
                } else {
                   // textInputLayout.setError("Please meet the password requirements.");
                    isValidationSuccessful = false;
                    // Remove any pending success message callbacks
                    handler.removeCallbacks(successMessageRunnable);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private Runnable successMessageRunnable = new Runnable() {
        @Override
        public void run() {
            if (isValidationSuccessful) {
                textInputLayout.setHelperText("Your password is correct");
                // Clear the message after a few seconds
              //  handler.postDelayed(clearMessageRunnable, 500); // Delay for 2 seconds
            }
        }
    };

    private Runnable clearMessageRunnable = new Runnable() {
        @Override
        public void run() {
            textInputLayout.setHelperText("");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacks(successMessageRunnable);
        handler.removeCallbacks(clearMessageRunnable);
    }
}
