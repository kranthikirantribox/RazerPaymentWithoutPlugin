package com.rzrTest.payment;

import static my.com.softspace.ssmpossdk.transaction.MPOSTransaction.TransactionEvents.TransactionResult.TransactionFailed;
import static my.com.softspace.ssmpossdk.transaction.MPOSTransaction.TransactionEvents.TransactionResult.TransactionSuccessful;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
  import android.content.DialogInterface;
  import android.content.Intent;
  import android.content.pm.PackageManager;
  import android.media.AudioManager;
  import android.media.MediaPlayer;
  import android.media.ToneGenerator;
  import android.net.Uri;
  import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
  import android.os.VibrationEffect;
  import android.os.Vibrator;
  import android.provider.Settings;
  import android.text.method.ScrollingMovementMethod;
  import android.util.Log;
  import android.view.View;
  import android.widget.Button;
  import android.widget.EditText;
  import android.widget.LinearLayout;
  import android.widget.TextView;
  import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
  import androidx.appcompat.app.AppCompatActivity;
  import androidx.core.content.ContextCompat;

  import com.visa.CheckmarkMode;
  import com.visa.CheckmarkTextOption;
  import com.visa.SensoryBrandingView;

  import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
  import java.util.Date;


import my.com.softspace.ssmpossdk.Environment;
  import my.com.softspace.ssmpossdk.SSMPOSSDK;
  import my.com.softspace.ssmpossdk.SSMPOSSDKConfiguration;
  import my.com.softspace.ssmpossdk.transaction.MPOSTransaction;
  import my.com.softspace.ssmpossdk.transaction.MPOSTransactionOutcome;
  import my.com.softspace.ssmpossdk.transaction.MPOSTransactionParams;

public class RazerPaymentActivity extends AppCompatActivity
{
  private final static String TAG = "FasstapSDKTester";

  private final static String CARD_TYPE_VISA = "0";
  private final static String CARD_TYPE_MASTERCARD = "1";
  private final static String CARD_TYPE_AMEX = "2";
  private final static String CARD_TYPE_JCB = "3";
  private final static String CARD_TYPE_DISCOVER = "23";

  private final static String TRX_STATUS_APPROVED = "100";
  private final static String TRX_STATUS_REVERSED = "101";
  private final static String TRX_STATUS_VOIDED = "102";
  private final static String TRX_STATUS_PENDING_SIGNATURE = "103";
  private final static String TRX_STATUS_SETTLED = "104";
  private final static String TRX_STATUS_PENDING_TC = "105";
  private final static String TRX_STATUS_REFUNDED = "106";

  private EditText edtAmtAuth;
  private EditText edtUserID;
  private EditText edtDeveloperID;
//  private EditText edtRefNo;
  public String edtRefNo;
  private EditText edtTrxID;
//  private TextView tvLogArea;
  private Button btnStartTrx;
  private Button btnClearLog;
  private Button btnVoidTrx;
  private Button btnSettlement;
  private Button btnRefreshToken;
  private Button btnGetTransactionStatus;
  private Button btnRefundTrx;
  private Button btnUploadSignature;
  private volatile boolean isTrxRunning = false;
  private LinearLayout layoutSensoryBranding;
  private SensoryBrandingView visaSensoryBrandingView;
  private VideoView sensoryBrandingVideoView;

  private MPOSTransactionOutcome _transactionOutcome;

  String log_data;

  ArrayList<String> stringList = new ArrayList<>();

  @SuppressLint("MissingInflatedId")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_razer_payment);
    edtAmtAuth = findViewById(R.id.edtAmtAuth);
    edtUserID = findViewById(R.id.edtUserID);
    edtDeveloperID = findViewById(R.id.edtDeveloperID);
//    edtRefNo = findViewById(R.id.edtRefNo);
    edtTrxID = findViewById(R.id.edtTrxID);
//    tvLogArea = findViewById(R.id.tvLogArea);
    btnStartTrx = findViewById(R.id.btnStartTrx);
    btnClearLog = findViewById(R.id.btnClearLog);
    btnVoidTrx = findViewById(R.id.btnVoidTrx);
    btnSettlement = findViewById(R.id.btnSettlement);
    btnRefreshToken = findViewById(R.id.btnRefreshToken);
    btnGetTransactionStatus = findViewById(R.id.btnGetTransactionStatus);
    btnRefundTrx = findViewById(R.id.btnRefundTrx);
    btnUploadSignature = findViewById(R.id.btnUploadSignature);
    layoutSensoryBranding = findViewById(R.id.activity_layoutSensoryBranding);
    visaSensoryBrandingView = findViewById(R.id.activity_visaSensoryBrandingView);
    sensoryBrandingVideoView = findViewById(R.id.activity_sensoryBrandingVideoView);

    initUI();
//    initFasstapMPOSSDK();
  }

  @Override
  protected void onResume() {
    super.onResume();
    edtAmtAuth.clearFocus();
  }

  private void writeLog(String msg) {
    this.runOnUiThread(() -> {
      Date now = new Date();
      Log.e("3333333333333333333", new SimpleDateFormat("MM-dd HH:mm:ss.SS").format(now) + "\n" + msg + "\n\n");
      stringList.add(msg);
//      tvLogArea.append(new SimpleDateFormat("MM-dd HH:mm:ss.SS").format(now) + "\n" + msg + "\n\n");
    });

  }

  private void clearLogs() {
    this.runOnUiThread(() -> {
//      tvLogArea.setText("");
//      tvLogArea.scrollTo(0,0);
    });
  }

  private void initUI()
  {


//    tvLogArea.setMovementMethod(new ScrollingMovementMethod());

    btnStartTrx.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {

      }
    });

    btnClearLog.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        clearLogs();
      }
    });

    btnVoidTrx.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        btnVoidTrxOnClick();
      }
    });

    btnSettlement.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        btnSettlementOnClick();
      }
    });

    btnRefreshToken.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btnRefreshTokenOnClick();
      }
    });

    btnGetTransactionStatus.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btnGetTransactionStatussOnClick();
      }
    });

    btnRefundTrx.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        btnRefundTrxOnClick();
      }
    });

    btnUploadSignature.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        uploadSignature();
      }
    });
  }

  public void btnStartTrxOnClick(Context context, String amount) {
    //trx running, means user have the intention to cancel trx
    if (isTrxRunning) {
      cancelTrx();
    }
    else {
      edtRefNo = "SS" + Calendar.getInstance().getTimeInMillis();
      startTrx(context, amount);
//      btnUploadSignature.setEnabled(false);
    }
  }

  private void cancelTrx() {
    toggleTransactionRunning(false);
    SSMPOSSDK.getInstance().getTransaction().abortTransaction();
    // clearLogs();
    writeLog("transaction successfully cancelled");
  }

  public void btnVoidTrxOnClick() {
    voidTransaction();
  }

  public void btnRefundTrxOnClick() {
    refundTransaction();
  }

  public void btnSettlementOnClick() {
    performSettlement();
  }

  public void btnRefreshTokenOnClick() {
//    refreshToken();
  }

  public void btnGetTransactionStatussOnClick() {
    getTransactionStatus();
  }

  private void startTrx(Context context, String amount) {

    if (!isNfcEnabled(context))
    {
      return;
    }

    toggleTransactionRunning(true);

    if (SSMPOSSDK.requestPermissionIfRequired((Activity) context, 10009))
    {
      new Thread() {
        @Override
        public void run() {
          startEMVProcessing(context,amount);
        }
      }.start();
    }
    else
    {
      toggleTransactionRunning(false);
    }
  }

  private void toggleTransactionRunning(boolean isRunning) {
    if (isRunning) {
      isTrxRunning = true;
//      btnStartTrx.setText("Cancel\nTransaction");
//      if (btnRefundTrx.isEnabled()) btnRefundTrx.setText("Cancel\nRefund Transaction");
    }
    else {
      isTrxRunning = false;
//      btnStartTrx.setText("Start\nTransaction");
//      btnRefundTrx.setText("Refund\nTransaction");
//      btnUploadSignature.setEnabled(false);
    }

  }

  public boolean isNfcEnabled(Context context) {
    NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);
    if (adapter != null) {
      if (adapter.isEnabled())
      {
        return true;
      }
      else
      {
        runOnUiThread(() -> new AlertDialog.Builder(context)
          .setMessage(R.string.ALERT_NFC_NOT_ENABLE)
          .setPositiveButton(R.string.ALERT_BTN_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
            }
          })
          .setNegativeButton(R.string.BTN_SETTINGS, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
              if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return;
              }
              intent = new Intent(Settings.ACTION_SETTINGS);
              if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
              } else {
                new AlertDialog.Builder(context)
                  .setMessage(R.string.ALERT_NOT_SUPPORTED_MSG)
                  .setCancelable(true)
                  .setPositiveButton(R.string.ALERT_BTN_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
                  .create()
                  .show();
              }
            }
          })
          .setCancelable(false)
          .create()
          .show()
        );
        return false;
      }
    }

    // NFC not supported
    btnStartTrx.setEnabled(false);
    return false;
  }

    public void initFasstapMPOSSDK(Context context) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        writeLog("Init...");
        SSMPOSSDKConfiguration config = SSMPOSSDKConfiguration.Builder.create()
          .setAttestationHost(BuildConfig.ATTESTATION_HOST)
          .setAttestationHostCertPinning(BuildConfig.ATTESTATION_CERT_PINNING)
          .setAttestationHostReadTimeout(10000L)
          .setAttestationRefreshInterval(300000L)
          .setAttestationStrictHttp(true)
          .setAttestationConnectionTimeout(30000L)
          .setLibGooglePlayProjNum("181214540201")
          .setLibAccessKey(BuildConfig.ACCESS_KEY)
          .setLibSecretKey(BuildConfig.SECRET_KEY)
          .setUniqueID("1AyG5OtZLFp0AjXwiZrP")
          .setDeveloperID("KgrDiYb7rfFZLcP")
          .setEnvironment(BuildConfig.FLAVOR_environment.equals("uat") ? Environment.UAT : Environment.PROD)
          .build();

        SSMPOSSDK.init(context, config);
            writeLog("SDK Version: " + SSMPOSSDK.getInstance().getSdkVersion());
            writeLog("COTS ID: " + SSMPOSSDK.getInstance().getCotsId());
        if (!SSMPOSSDK.hasRequiredPermission(context)) {
          SSMPOSSDK.requestPermissionIfRequired((Activity) context, 1000);
        }

      }
    });
  }

  public void refreshToken(Context context)
  {
    writeLog("refreshToken()");
    SSMPOSSDK.getInstance().getSSMPOSSDKConfiguration().uniqueID = "1AyG5OtZLFp0AjXwiZrP";
    SSMPOSSDK.getInstance().getSSMPOSSDKConfiguration().developerID = "KgrDiYb7rfFZLcP";
    SSMPOSSDK.getInstance().getTransaction().refreshToken((Activity) context, new MPOSTransaction.TransactionEvents() {
      @Override
      public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
        writeLog("onTransactionResult :: " + result);
//                writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());

        if(result == TransactionSuccessful) {

        } else {
          if(transactionOutcome != null) {
            writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
          }
        }
      }

      @Override
      public void onTransactionUIEvent(int event) {
        writeLog("onTransactionUIEvent :: " + event);
      }
    });
  }

  private void startEMVProcessing(Context context,String amount)
  {
    if (amount != null && Double.parseDouble(amount) <= 0)
    {
      writeLog("Amount cannot be zero!");
      toggleTransactionRunning(false);
      return;
    }

    // run aync task as blocking.
    this.runOnUiThread(() -> {
      clearLogs();
      writeLog("Amount, Authorised: " + amount);
    });

    try {
      _transactionOutcome = null;
      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .setReferenceNumber(edtRefNo)
        .setAmount(amount)
        .build();

      SSMPOSSDK.getInstance().getTransaction().startTransaction((Activity) context, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          _transactionOutcome = transactionOutcome;
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionResult :: " + result);
              writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());

              if(result == TransactionSuccessful)
              {
//                edtTrxID.setText(transactionOutcome.getTransactionID());
//                btnVoidTrx.setEnabled(true);
//                btnRefundTrx.setEnabled(true);
//                btnGetTransactionStatus.setEnabled(true);
//                btnUploadSignature.setEnabled(false);

                String outcome = "Transaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                outcome += "Reference No :: " + transactionOutcome.getReferenceNo() + "\n";
                outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName() + "\n";
                outcome += "Acquirer ID :: " + transactionOutcome.getAcquirerID() + "\n";
                outcome += "Contactless CVM Type :: " + transactionOutcome.getContactlessCVMType() + "\n";
                outcome += "RRN :: " + transactionOutcome.getRrefNo()+ "\n";
                outcome += "Trace No :: " + transactionOutcome.getTraceNo()+ "\n";
                outcome += "Transaction Date Time UTC :: " + transactionOutcome.getTransactionDateTime();
                writeLog(outcome);

                if(CARD_TYPE_VISA.equals(transactionOutcome.getCardType()))
                {
                  animateVisaSensoryBranding();
                }
                else if(CARD_TYPE_MASTERCARD.equals(transactionOutcome.getCardType()))
                {
                  animateMastercardSensoryTransaction();
                }
                else if(CARD_TYPE_AMEX.equals(transactionOutcome.getCardType()))
                {
                  animateAmexSensoryTransaction();
                }
                else if(CARD_TYPE_JCB.equals(transactionOutcome.getCardType()))
                {
                  animateJCBSensoryTransaction();
                }
                else if(CARD_TYPE_DISCOVER.equals(transactionOutcome.getCardType()))
                {
                  animateDiscoverSensoryTransaction();
                }
              }
              else if (result == TransactionFailed)
              {
                if(transactionOutcome != null)
                {
                  String outcome = transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage();
                  if (transactionOutcome.getTransactionID() != null && transactionOutcome.getTransactionID().length() > 0)
                  {
                    outcome += "\nTransaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                    outcome += "Reference No :: " + transactionOutcome.getReferenceNo() + "\n";
                    outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                    outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                    outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName() + "\n";
                    outcome += "Acquirer ID :: " + transactionOutcome.getAcquirerID() + "\n";
                    outcome += "RRN :: " + transactionOutcome.getRrefNo() + "\n";
                    outcome += "Trace No :: " + transactionOutcome.getTraceNo() + "\n";
                    outcome += "Transaction Date Time UTC :: " + transactionOutcome.getTransactionDateTime();
                  }
                  writeLog(outcome);
                }
                else
                {
                  writeLog("Error ::" + result);
                }
              }

              toggleTransactionRunning(false);
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
              if (event == TransactionUIEvent.CardReadOk)
              {
                // you may customize card reads OK sound & vibration, below is some example
                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_P, 500);

                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v.hasVibrator())
                {
                  v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                writeLog("Card read completed");
              }
              else if (event == TransactionUIEvent.RequestSignature)
              {
                writeLog("Signature is required");
                btnUploadSignature.setEnabled(true);
              }
              else
              {
                switch (event)
                {
                  case TransactionUIEvent.PresentCard:
                  {
                    writeLog("Present your card");
                  }
                  break;
                  case TransactionUIEvent.Authorising:
                  {
                    writeLog("Authorising...");
                  }
                  break;
                  case TransactionUIEvent.CardPresented:
                  {
                    writeLog("Card detected");
                  }
                  break;
                  case TransactionUIEvent.CardReadError:
                  {
                    writeLog("Card read failed");
                  }
                  case TransactionUIEvent.CardReadRetry:
                  {
                    writeLog("Card read retry");
                  }
                  break;
                  default:
                    writeLog("onTransactionUIEvent :: " + event);
                    break;
                }
              }
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void uploadSignature()
  {
    writeLog("uploadSignature()");

    String base64SignatureString = ""; // your signature image base64 string

    try {
      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .setSignature(base64SignatureString)
        .build();

      SSMPOSSDK.getInstance().getTransaction().uploadSignature(transactionalParams);

    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void voidTransaction()
  {
    writeLog("voidTransaction()");
    try {
      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .setMPOSTransactionID(edtTrxID.getText().toString())
        .build();

      SSMPOSSDK.getInstance().getTransaction().voidTransaction(this, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionResult :: " + result);
              writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());
              if(result == TransactionSuccessful) {
                btnVoidTrx.setEnabled(false);
                if (transactionOutcome != null && transactionOutcome.getTransactionID() != null && transactionOutcome.getTransactionID().length() > 0)
                {
                  String outcome = "Status :: " + transactionOutcome.getStatusCode() + " - " + (mapStatusCode(transactionOutcome.getStatusCode()).length()>0?mapStatusCode(transactionOutcome.getStatusCode()):transactionOutcome.getStatusMessage()) + "\n";
                  outcome += "Transaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                  outcome += "Reference no :: " + transactionOutcome.getReferenceNo() + "\n";
                  outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                  outcome += "Invoice no :: " + transactionOutcome.getInvoiceNo() + "\n";
                  outcome += "AID :: " + transactionOutcome.getAid() + "\n";
                  outcome += "Card type :: " + transactionOutcome.getCardType() + "\n";
                  outcome += "Application label :: " + transactionOutcome.getApplicationLabel() + "\n";
                  outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                  outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName()+ "\n";
                  outcome += "RRN :: " + transactionOutcome.getRrefNo() + "\n";
                  outcome += "Trace No :: " + transactionOutcome.getTraceNo()+ "\n";
                  outcome += "Transaction Date Time UTC :: " + transactionOutcome.getTransactionDateTime();
                  writeLog(outcome);
                }
              }
              else {
                if (transactionOutcome != null) {
                  writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
                }
              }
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionUIEvent :: " + event);
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void refundTransaction()
  {
    writeLog("refundTransaction()");
    try {
      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .setMPOSTransactionID(edtTrxID.getText().toString())
        .setAmount(edtAmtAuth.getText().toString())
        .build();

      SSMPOSSDK.getInstance().getTransaction().refundTransaction(this, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionResult :: " + result);
              writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());
              if(result == TransactionSuccessful) {
                btnRefundTrx.setEnabled(false);
                if (transactionOutcome != null && transactionOutcome.getTransactionID() != null && transactionOutcome.getTransactionID().length() > 0)
                {
                  String outcome = "Status :: " + transactionOutcome.getStatusCode() + " - " + (mapStatusCode(transactionOutcome.getStatusCode()).length()>0?mapStatusCode(transactionOutcome.getStatusCode()):transactionOutcome.getStatusMessage()) + "\n";
                  outcome += "Transaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                  outcome += "Reference no :: " + transactionOutcome.getReferenceNo() + "\n";
                  outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                  outcome += "Invoice no :: " + transactionOutcome.getInvoiceNo() + "\n";
                  outcome += "AID :: " + transactionOutcome.getAid() + "\n";
                  outcome += "Card type :: " + transactionOutcome.getCardType() + "\n";
                  outcome += "Application label :: " + transactionOutcome.getApplicationLabel() + "\n";
                  outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                  outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName() + "\n";
                  outcome += "RRN :: " + transactionOutcome.getRrefNo() + "\n";
                  outcome += "Trace No :: " + transactionOutcome.getTraceNo() + "\n";
                  outcome += "Transaction Date Time UTC :: " + transactionOutcome.getTransactionDateTime();
                  writeLog(outcome);
                }
              }
              else {
                if (transactionOutcome != null) {
                  writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
                }
              }
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionUIEvent :: " + event);
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void refundTransactionWithCardPresented()
  {
    writeLog("refundTransactionWithCardPresented()");

    if (edtAmtAuth.getText().toString() != null && Double.parseDouble(edtAmtAuth.getText().toString()) <= 0)
    {
      writeLog("Amount cannot be zero!");
      toggleTransactionRunning(false);
      return;
    }

    // run aync task as blocking.
    this.runOnUiThread(() -> {
      clearLogs();
      writeLog("Amount, Authorised: " + edtAmtAuth.getText().toString());
    });

    try {
      _transactionOutcome = null;

      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .setReferenceNumber(edtRefNo)
        .setAmount(edtAmtAuth.getText().toString())
        .setCardRequiredForRefund(true)
        .build();

      if (transactionalParams.isCardRequiredForRefund())
      {
        toggleTransactionRunning(true);
      }

      SSMPOSSDK.getInstance().getTransaction().refundTransaction(this, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          _transactionOutcome = transactionOutcome;
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionResult :: " + result);
              writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());

              if(result == TransactionSuccessful)
              {
                edtTrxID.setText(transactionOutcome.getTransactionID());
                btnVoidTrx.setEnabled(true);
                btnGetTransactionStatus.setEnabled(true);

                String outcome = "Transaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName() + "\n";
                outcome += "RRN :: " + transactionOutcome.getRrefNo();
                writeLog(outcome);

                if(CARD_TYPE_VISA.equals(transactionOutcome.getCardType()))
                {
                  animateVisaSensoryBranding();
                }
                else if(CARD_TYPE_MASTERCARD.equals(transactionOutcome.getCardType()))
                {
                  animateMastercardSensoryTransaction();
                }
                else if(CARD_TYPE_AMEX.equals(transactionOutcome.getCardType()))
                {
                  animateAmexSensoryTransaction();
                }
                else if(CARD_TYPE_JCB.equals(transactionOutcome.getCardType()))
                {
                  animateJCBSensoryTransaction();
                }
              }
              else
              {
                if(transactionOutcome != null)
                {
                  writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
                }
                else
                {
                  writeLog("Error ::" + result);
                }
              }

              toggleTransactionRunning(false);
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
              if (event == TransactionUIEvent.CardReadOk)
              {
                // you may customize card reads OK sound & vibration, below is some example
                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
                toneGenerator.startTone(ToneGenerator.TONE_DTMF_P, 500);

                Vibrator v = (Vibrator) RazerPaymentActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                if (v.hasVibrator())
                {
                  v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
              }
              writeLog("onTransactionUIEvent :: " + event);
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void getTransactionStatus()
  {
    writeLog("getTransactionStatus()");
    try {
      String trxID = edtTrxID.getText().toString();
      String referenceNo = edtRefNo;

      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create().build();

      if (trxID.length() > 0)
      {
        transactionalParams = MPOSTransactionParams.Builder.create()
          .setMPOSTransactionID(edtTrxID.getText().toString())
          .build();
      }
      else if (referenceNo.length() > 0)
      {
        transactionalParams = MPOSTransactionParams.Builder.create()
          .setReferenceNumber(edtRefNo)
          .build();
      }

      SSMPOSSDK.getInstance().getTransaction().getTransactionStatus(this, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if(result == TransactionSuccessful)
              {
                if (transactionOutcome.getStatusCode().equals(TRX_STATUS_APPROVED))
                {
                  btnVoidTrx.setEnabled(true);
                }
                else if (transactionOutcome.getStatusCode().equals(TRX_STATUS_SETTLED))
                {
                  btnRefundTrx.setEnabled(true);
                }
                String outcome = "Status :: " + transactionOutcome.getStatusCode() + " - " + (mapStatusCode(transactionOutcome.getStatusCode()).length()>0?mapStatusCode(transactionOutcome.getStatusCode()):transactionOutcome.getStatusMessage()) + "\n";
                outcome += "Reference no :: " + transactionOutcome.getReferenceNo() + "\n";
                outcome += "Amount auth :: " + transactionOutcome.getAmountAuthorized() + "\n";
                outcome += "Transaction ID :: " + transactionOutcome.getTransactionID() + "\n";
                outcome += "Transaction date :: " + transactionOutcome.getTransactionDate() + "\n";
                outcome += "Batch no :: " + transactionOutcome.getBatchNo() + "\n";
                outcome += "Approval code :: " + transactionOutcome.getApprovalCode() + "\n";
                outcome += "Invoice no :: " + transactionOutcome.getInvoiceNo() + "\n";
                outcome += "AID :: " + transactionOutcome.getAid() + "\n";
                outcome += "Card type :: " + transactionOutcome.getCardType() + "\n";
                outcome += "Application label :: " + transactionOutcome.getApplicationLabel() + "\n";
                outcome += "Card number :: " + transactionOutcome.getCardNo() + "\n";
                outcome += "Cardholder name :: " + transactionOutcome.getCardHolderName()+ "\n";
                outcome += "Trace no :: " + transactionOutcome.getTraceNo()+ "\n";
                outcome += "RRN :: " + transactionOutcome.getRrefNo() + "\n";
                outcome += "Transaction Date Time UTC :: " + transactionOutcome.getTransactionDateTime();

                writeLog(outcome);
              }
              else
              {
                if(transactionOutcome != null)
                {
                  writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
                }
                else
                {
                  writeLog("Error ::" + result);
                }
              }
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionUIEvent :: " + event);
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  private void performSettlement()
  {
    writeLog("performSettlement()");
    try {
      MPOSTransactionParams transactionalParams = MPOSTransactionParams.Builder.create()
        .build();

      SSMPOSSDK.getInstance().getTransaction().performSettlement(this, transactionalParams, new MPOSTransaction.TransactionEvents() {
        @Override
        public void onTransactionResult(int result, MPOSTransactionOutcome transactionOutcome) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionResult :: " + result);
              writeLog("onTransactionOutCome ::" + transactionOutcome.getStatusCode());
              if(result != TransactionSuccessful && transactionOutcome != null) {
                writeLog(transactionOutcome.getStatusCode() + " - " + transactionOutcome.getStatusMessage());
              }
            }
          });
        }

        @Override
        public void onTransactionUIEvent(int event) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              writeLog("onTransactionUIEvent :: " + event);
            }
          });
        }
      });
    } catch (Exception e) {
      Log.e(TAG, e.getMessage(), e);
    }
  }

  // ============================================================================================
  // Sensory Methods
  // ============================================================================================

  private void updateSensoryBranding() {
    visaSensoryBrandingView.setBackdropColor(ContextCompat.getColor(this, R.color.colorWhite));
    visaSensoryBrandingView.setSoundEnabled(true);
    visaSensoryBrandingView.setHapticFeedbackEnabled(true);
    visaSensoryBrandingView.setCheckmarkMode(CheckmarkMode.CHECKMARK_WITH_TEXT);
    visaSensoryBrandingView.setCheckmarkText(CheckmarkTextOption.APPROVE);
    visaSensoryBrandingView.setLanguageCode("ja");
  }

  private void animateVisaSensoryBranding()
  {
    updateSensoryBranding();
    visaSensoryBrandingView.setVisibility(View.VISIBLE);
    layoutSensoryBranding.setVisibility(View.VISIBLE);

    visaSensoryBrandingView.animate( error -> {
      visaSensoryBrandingView.setBackdropColor(ContextCompat.getColor(this, R.color.transparent));
      visaSensoryBrandingView.setVisibility(View.GONE);
      layoutSensoryBranding.setVisibility(View.GONE);
      return null;
    });
  }

  private void animateMastercardSensoryTransaction()
  {
    layoutSensoryBranding.setVisibility(View.VISIBLE);
    sensoryBrandingVideoView.setVisibility(View.VISIBLE);

    String path = "android.resource://" + getPackageName() + "/" + R.raw.mc_sensory_transaction;
    sensoryBrandingVideoView.setVideoURI(Uri.parse(path));
    sensoryBrandingVideoView.setZOrderOnTop(true);
    sensoryBrandingVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mp)
      {
        sensoryBrandingVideoView.suspend();
        layoutSensoryBranding.setVisibility(View.GONE);
        sensoryBrandingVideoView.setVisibility(View.GONE);
      }
    });
    sensoryBrandingVideoView.start();
  }

  private void animateAmexSensoryTransaction()
  {
    layoutSensoryBranding.setVisibility(View.VISIBLE);
    sensoryBrandingVideoView.setVisibility(View.VISIBLE);

    String path = "android.resource://" + getPackageName() + "/" + R.raw.amex_sensory_transaction;
    sensoryBrandingVideoView.setVideoURI(Uri.parse(path));
    sensoryBrandingVideoView.setZOrderOnTop(true);
    sensoryBrandingVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mp)
      {
        sensoryBrandingVideoView.suspend();
        layoutSensoryBranding.setVisibility(View.GONE);
        sensoryBrandingVideoView.setVisibility(View.GONE);
      }
    });
    sensoryBrandingVideoView.start();
  }

  private void animateJCBSensoryTransaction()
  {
    layoutSensoryBranding.setVisibility(View.VISIBLE);
    sensoryBrandingVideoView.setVisibility(View.VISIBLE);

    String path = "android.resource://" + getPackageName() + "/" + R.raw.jcb_sensory_transaction;
    sensoryBrandingVideoView.setVideoURI(Uri.parse(path));
    sensoryBrandingVideoView.setZOrderOnTop(true);
    sensoryBrandingVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mp)
      {
        sensoryBrandingVideoView.suspend();
        layoutSensoryBranding.setVisibility(View.GONE);
        sensoryBrandingVideoView.setVisibility(View.GONE);
      }
    });
    sensoryBrandingVideoView.start();
  }

  private void animateDiscoverSensoryTransaction()
  {
    layoutSensoryBranding.setVisibility(View.VISIBLE);
    sensoryBrandingVideoView.setVisibility(View.VISIBLE);

    String path = "android.resource://" + getPackageName() + "/" + R.raw.discover_sensory_transaction;
    sensoryBrandingVideoView.setVideoURI(Uri.parse(path));
    sensoryBrandingVideoView.setZOrderOnTop(true);
    sensoryBrandingVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mp)
      {
        sensoryBrandingVideoView.suspend();
        layoutSensoryBranding.setVisibility(View.GONE);
        sensoryBrandingVideoView.setVisibility(View.GONE);
      }
    });
    sensoryBrandingVideoView.start();
  }

  private String mapStatusCode(String code)
  {
    switch (code)
    {
      case TRX_STATUS_APPROVED:
        return "Approved";

      case TRX_STATUS_REVERSED:
        return "Reversed";

      case TRX_STATUS_VOIDED:
        return "Voided";

      case TRX_STATUS_PENDING_SIGNATURE:
        return "Pending Signature";

      case TRX_STATUS_SETTLED:
        return "Settled";

      case TRX_STATUS_PENDING_TC:
        return "Pending TC";

      case TRX_STATUS_REFUNDED:
        return "Refunded";
    }

    return code;
  }

  @SuppressLint("MissingSuperCall")
  @Override
  public void onRequestPermissionsResult(int resultCode, String[] permissions, int[] grantResult) {

    switch (resultCode) {
      case 1000:
        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
          writeLog("Permission granted");
        }
        else {
          writeLog("Permission not granted, cant proceed");
        }
        break;
    }
  }

  public ArrayList<String> readLog() {

    return stringList;
  }
}
