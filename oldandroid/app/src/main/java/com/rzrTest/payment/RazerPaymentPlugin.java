package com.rzrTest.payment;

import android.util.Log;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "RazerPaymentStatus")
public class RazerPaymentPlugin extends Plugin {
@PluginMethod
  public void onCreate(PluginCall call){
  Log.e("99999999999999999", "Working");
  }
}
