package com.fazlelab.sscresult2021;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class Result_online extends AppCompatActivity {




    WebView myWebView;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    AdView wAdView;
    InterstitialAd mInterstitialAd;
    public static final String TAG = "YOUR-TAG-NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_online);

        //ads initialize
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        wAdView = findViewById(R.id.wAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        wAdView.loadAd(adRequest);

        //Full screen ads

                AdRequest adRequest2 = new AdRequest.Builder().build();
                InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest2,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                // The mInterstitialAd reference will be null until
                                // an ad is loaded.
                                mInterstitialAd = interstitialAd;
//                                Log.i(TAG, "onAdLoaded");
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error
                                Log.i(TAG, loadAdError.getMessage());
                                mInterstitialAd = null;
                            }
                        });


        if (mInterstitialAd != null) {
            mInterstitialAd.show(Result_online.this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }

        getSupportActionBar().hide();

        //signing

        myWebView =findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        relativeLayout = findViewById(R.id.relationLayout);
        progressBar = findViewById(R.id.progressBar);

        //check connection function
        connectionCheck();


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://eboardresults.com/v2/home");


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading! Please wait");


        myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                progressDialog.show();
                if (newProgress==100){
                    progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        });




    }

    public void connectionCheck(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected()){
            myWebView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }else if (mobile.isConnected()){
            myWebView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        }else{

            myWebView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }


    }


}