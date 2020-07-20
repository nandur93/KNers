package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;


public class WebViewActivity extends AppCompatActivity {


    private static final String TAG = "WebViewActivity";
    public ObservableWebView webView;
    public Toolbar tToolbar;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;

    String pageURL, pageTitle ;
    //private String nandur93 = "http://nandur93.blogspot.com";
    //private String OffLineAddress = "http://turnjs.com/";
    private String Title = "Loading...";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sumber dari XML
        setContentView(R.layout.activity_webview);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setProgress(12);
        //Simple WebView
        webView = findViewById(R.id.observableWebView);
        //WebView Settings
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.clearCache(false);
        webView.clearHistory();
        //Url yang mau di load
        String offLineAddress = "file:///android_asset/HTML/DIABETASOL.html";
        String msPortal = "https://bit.ly/MSPORTAL";
        webView.loadUrl(msPortal);


        registerForContextMenu(webView);

            //Assign Toolbar to the activity
        tToolbar = findViewById(R.id.tToolbar);
        setSupportActionBar(tToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.title_webview);

        //back forward
        AppBarLayout appBarLayout = findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);


        webView.setOnScrollChangedCallback((l, t) -> {
            //Do stuff
            Log.d(TAG,"We Scrolled etc...");
        });


        //KodeBaru
        frameLayout = findViewById(R.id.frameLayout);
        final SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.refresh,R.color.refresh1,R.color.refresh2);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            tToolbar.setTitle(Title);
            frameLayout.setVisibility(View.VISIBLE);
            (new Handler()).postDelayed(() -> {
                refreshLayout.setRefreshing(false);
                webView.reload();// start refresh
            },3000);
        });
        final Activity activity = this;
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                activity.setProgress(newProgress * 100);
                frameLayout.setVisibility(View.VISIBLE);
                //setTitle(Title);
                //tToolbar.setTitle(Title);

                if (newProgress == 100) {
                    frameLayout.setVisibility(View.GONE);
                    //tToolbar.setTitle(pageTitle);
                    // refresh complete
                    refreshLayout.setRefreshing(false);
                    //refresLayout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                }
                    super.onProgressChanged(webView, newProgress);
            }
        });



        //Supaya WebView tidak membuka Browser bawaan atau Chrome
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient(this));

    }

    @Override
    //longpress
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);

        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();

        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {

            contextMenu.setHeaderTitle("Download Image");

            contextMenu.add(0, 1, 0, "Save - Download Image")
                    .setOnMenuItemClickListener(menuItem -> {

                        String DownloadImageURL = webViewHitTestResult.getExtra();

                        if(URLUtil.isValidUrl(DownloadImageURL)){

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            downloadManager.enqueue(request);

                            Toast.makeText(WebViewActivity.this,"Image Downloaded Successfully.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(WebViewActivity.this,"Sorry.. Something Went Wrong.",Toast.LENGTH_LONG).show();
                        }
                        return false;

                    });
        }

    }


    public void onForward(MenuItem item) {
        if (webView.canGoForward()) {
            webView.goForward();
            tToolbar.setTitle(Title);
        }
    }

    public void onGoBack(MenuItem item) {
        if (webView.canGoBack()) {
            webView.goBack();
            tToolbar.setTitle(Title);
        }else {
            finish();
        }
    }

    public void onInfo(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);

        builder.setTitle(R.string.title_info);

        builder.setMessage(R.string.sum_info);
        builder.setIcon(R.drawable.ic_error_outline_black_24dp);


        AlertDialog diag = builder.create();

        //Display the message!
        diag.show();

    }


    private class MyWebViewClient extends WebViewClient {

                @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            frameLayout.setVisibility(View.VISIBLE);
            pageURL = view.getUrl();
            tToolbar.setSubtitle(pageURL);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            frameLayout.setVisibility(View.VISIBLE);
            tToolbar.setTitle(Title);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            //Getting WebPage URL .
            pageURL = view.getUrl();

            //Getting WebPage TITLE .
            pageTitle = view.getTitle();

            //Adding Page URL inside Action Bar;
            tToolbar.setTitle(pageTitle);

            //Adding Page Title inside Action Bar;
            tToolbar.setSubtitle(pageURL);
            frameLayout.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            tToolbar.setTitle(Title);
            return true;
        //} else {
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        if(menu instanceof MenuBuilder){

            MenuBuilder menuBuilder = (MenuBuilder) menu;
            menuBuilder.setOptionalIconsVisible(true);
        }

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchViewAndroidActionBar.setQueryHint("Google Search...");
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                webView.loadUrl("https://www.google.com/search?q="+query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


        });

            //webView.loadUrl("https://www.google.co.id/search?q="+query);
        // Configure the search info and add any event listeners...
        return super.onCreateOptionsMenu(menu);
    }

  /*  @Override
    public void onBackPressed() {
        android.widget.SearchView searchViewAndroidActionBar = null;
        if (!searchViewAndroidActionBar.isIconified()) {
            searchViewAndroidActionBar.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }*/

    private class MyWebChromeClient extends WebChromeClient {
        Context context;
        MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

}

