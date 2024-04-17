package com.psaainsankamil.vitone.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 24/05/18.
 */

public class InputRegisterValidation {
    private Context context;

    public InputRegisterValidation(Context context){
        this.context = context;
    }

    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isInputEditTextChar(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.matches(" ")) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    public boolean isInputEditTextSmall(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length() < 8) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextBig(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length() > 20) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextCheck(final TextInputEditText textInputEditText1, final TextInputEditText textInputEditText2,
                                        final TextInputLayout textInputLayout1, final TextInputLayout textInputLayout2) {

        final String value1 = textInputEditText1.getText().toString().trim();
        final String value2 = textInputEditText2.getText().toString().trim();

        class CheckMember extends AsyncTask<Void, Void, Object> {

            boolean check;

            @Override
            protected Object doInBackground(Void...v){
                HashMap<String,String> params = new HashMap<>();
                params.put("username", value1);
                params.put("email", value2);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"check_member", params);
                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(!status){
                        System.out.println("masuk = "+obj.getString("type"));
                        if(obj.getString("type").equals("username")){
                            textInputLayout1.setError("Username sudah ada!");
                            hideKeyboardFrom(textInputEditText1);
                        }else{
                            textInputLayout2.setError("Email sudah ada!");
                            hideKeyboardFrom(textInputEditText2);
                        }
                        return false;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(Object result){
                check = (boolean) result;
            }

        }

        CheckMember cm = new CheckMember();
        cm.execute();

        if(!cm.check){
            return false;
        }else{
            return true;
        }
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
