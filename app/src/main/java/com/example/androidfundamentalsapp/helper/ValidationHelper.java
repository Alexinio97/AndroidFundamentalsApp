package com.example.androidfundamentalsapp.helper;

import com.google.android.material.textfield.TextInputLayout;

public class ValidationHelper {
    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


    public void isEmptyError(TextInputLayout inputLayout,String errorMessage)
    {
        if(inputLayout.getEditText().getText().toString().isEmpty())
        {
            inputLayout.setError(errorMessage);
            isValid = false;
        }
    }

    public void setMinMaxError(TextInputLayout inputLayout,int min, int max)
    {
        int inputLength = inputLayout.getEditText().getText().toString().length();
        // isEmptyError method shall set is valid if the String is empty
        if(inputLength == 0)
            return;
        if(inputLength < min)
        {
            inputLayout.setError("Too few characters, minimum is " + min + ".");
            isValid = false;
        }
        else if(inputLength > max)
        {
            inputLayout.setError("Too many characters, maximum is "+ max + ".");
            isValid = false;
        }
    }

    public void clearError(TextInputLayout inputLayout)
    {
        inputLayout.setError("");
    }
}
