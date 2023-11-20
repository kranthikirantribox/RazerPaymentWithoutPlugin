package com.rzrTest.payment;

import static android.content.ContentValues.TAG;
import static my.com.softspace.ssmpossdk.transaction.MPOSTransaction.TransactionEvents.TransactionResult.TransactionFailed;
import static my.com.softspace.ssmpossdk.transaction.MPOSTransaction.TransactionEvents.TransactionResult.TransactionSuccessful;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import my.com.softspace.ssmpossdk.Environment;
import my.com.softspace.ssmpossdk.SSMPOSSDK;
import my.com.softspace.ssmpossdk.SSMPOSSDKConfiguration;
import my.com.softspace.ssmpossdk.transaction.MPOSTransaction;
import my.com.softspace.ssmpossdk.transaction.MPOSTransactionOutcome;
import my.com.softspace.ssmpossdk.transaction.MPOSTransactionParams;


@CapacitorPlugin(name = "RazerPaymentStatus")
public class RazerPaymentPlugin extends Plugin {
  RazerPaymentActivity razerPaymentActivity = new RazerPaymentActivity();

  @PluginMethod
  public void loadLogs(PluginCall call) {

   String value = String.valueOf(razerPaymentActivity.readLog());
    JSObject ret = new JSObject();
    ret.put("value", value);
    call.resolve(ret);
  }
  @PluginMethod
  public void starttranstion(PluginCall call) {
    String value = call.getString("value");
    try {
      JSONArray jsonArray = new JSONArray(value);
      JSONObject jsonObject = jsonArray.getJSONObject(0);

      String amount = jsonObject.getString("amount");
      Context context = getContext();
      razerPaymentActivity.btnStartTrxOnClick(context,amount);


    } catch (JSONException e) {
      e.printStackTrace();
      // Handle JSON parsing error
      call.reject("Error parsing JSON");
    }


  }



  @PluginMethod
  public void refreshtoken(PluginCall call) {
    Context context = getContext();
    razerPaymentActivity.refreshToken(context);

  }
  @PluginMethod
  public void onCreate(PluginCall call){
     Context context = getContext();
    razerPaymentActivity.initFasstapMPOSSDK(context);
  }


}
