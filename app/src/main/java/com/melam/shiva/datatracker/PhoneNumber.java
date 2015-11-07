package com.melam.shiva.datatracker;

import android.telephony.TelephonyManager;
import android.content.Context;

/**
 * Created by Shiva on 5/21/2015.
 */
public class PhoneNumber {

    String phonenumber="";

    public PhoneNumber(Context context){

        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        phonenumber = telephonyManager.getLine1Number();
        phonenumber = phonenumber.substring(phonenumber.length() - 10);
    }

    public String getPhonenumber(){

        return this.phonenumber;
    }
}
