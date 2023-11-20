package com.rzrTest.payment;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;
import com.rzrTest.payment.RazerPaymentPlugin;


public class MainActivity extends BridgeActivity {

  @Override
  public void  onCreate(Bundle savedInstanceState) {
    registerPlugin(RazerPaymentPlugin.class);
    super.onCreate(savedInstanceState);
  }


}
