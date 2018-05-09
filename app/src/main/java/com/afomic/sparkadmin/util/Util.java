package com.afomic.sparkadmin.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.afomic.sparkadmin.LoginActivity;

import java.util.Random;

/**
 * Created by afomic on 11/22/17.
 *
 */

public class Util {
    public static boolean isEmpty(EditText edt){
        return TextUtils.isEmpty(getString(edt));
    }
    public  static String getString(EditText edt){
        return edt.getText().toString();
    }
    public static void makeToast(Context ctx,String message){
        Toast.makeText(ctx,message,Toast.LENGTH_SHORT).show();
    }
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
