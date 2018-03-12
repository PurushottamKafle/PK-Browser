package com.android.pkbrowser;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.R.id.progress;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private WebView web;
    private String MainURL ="file:///android_asset/MainActivity.html";
    private ProgressBar progressbar;
    private FrameLayout frame;
    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar tb2 = (Toolbar)findViewById(R.id.tlb1);
        setSupportActionBar(tb2);

        progressbar= (ProgressBar)findViewById(R.id.progress_upper);
        frame =(FrameLayout)findViewById(R.id.framelayout);
        web = (WebView)findViewById(R.id.web_first);

        progressbar.setMax(100);
        web.setWebViewClient(new HelpClient());
        web.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                web.loadUrl("file:///android_asset/error.html");

            }

        });
        web.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int progress){
                frame.setVisibility(View.VISIBLE);
                progressbar.setProgress(progress);
                setTitle("Loading...");
                if(progress==100){
                    frame.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view, progress);

            }

        });
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getFavicon();

        web.getSettings().getAllowFileAccess();
        web.setVerticalFadingEdgeEnabled(false);
        web.loadUrl(MainURL);

        progressbar.setProgress(0);





        //here ads


        prepareAd();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }
                        prepareAd();
                    }
                });
            }
        }, 30, 100, TimeUnit.SECONDS);
    }
    public void prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5095606744410268/3763087987");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }




    private class HelpClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
            frame.setVisibility(View.VISIBLE);
            return  true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(web.canGoBack()){
                web.goBack();
                return  true;
            }

            }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)   {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();

        if(i==R.id.about){
            Intent in3 = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(in3);

        }
        if(i==R.id.home){
            Intent in4 = new Intent(MainActivity.this, MainActivity.class);
            startActivity(in4);

        }

        if(i==R.id.play){
            Intent in2 = new Intent(MainActivity.this, MyActivity.class);
            startActivity(in2);

        }


        return super.onOptionsItemSelected(item);
    }



}
