package com.example.chuckapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

public class WebFragment extends Fragment {

    WebView webView;

    public static WebFragment getInstance(){
        WebFragment webFragment = new WebFragment();
        return webFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        View view_main = inflater.inflate(R.layout.activity_main, container, false);

        webView = (WebView) view.findViewById(R.id.webView);

        webView.setWebViewClient(new myWebClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("http://www.icndb.com/api/");

        return view;
    }

//    public void myClickMethod(View v) {
//        switch (v.getId()) {
//            case R.id.btn_back:
//                if (webView.canGoBack())
//                    webView.goBack();
//                break;
//        }
//
//    }
}