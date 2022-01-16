package com.iotph.paa.ui.form_survey;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iotph.paa.R;

public class FormSurveyDetailFragment extends Fragment {
    String link, label;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         Bundle bundle = this.getArguments();
        if (bundle != null) {
            link = bundle.getString("link");
            label = bundle.getString("label");
        }

        View view =  inflater.inflate(R.layout.fragment_form_survey_detail, container, false);

        WebView webView = view.findViewById(R.id.webview_form_survey);
        webView.loadUrl(link);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {@Override public void onReceivedSslError(WebView v, SslErrorHandler handler, SslError er){ handler.proceed(); }});
        return view;
    }
}