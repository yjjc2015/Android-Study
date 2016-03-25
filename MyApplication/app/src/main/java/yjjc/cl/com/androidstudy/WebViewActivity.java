package yjjc.cl.com.androidstudy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class WebViewActivity extends Activity {
    private WebView webView;
    private ProgressBar progressBar;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv = (TextView) findViewById(R.id.tv);
        progressBar.setMax(100);
        String url = getIntent().getStringExtra("info");
        //能够访问javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //允许访问文件
        webView.getSettings().getAllowFileAccess();
        //支持缩放
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        //防止中途调用本地浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override//显示加载的进度
            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
                if (newProgress == 100)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                } else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override//显示网页标题
            public void onReceivedTitle(WebView view, String title) {
                tv.setText(title);
            }
        });
        webView.postUrl(url, null);//Post请求，第二个参数的类型是字节数组，代表请求参数
//        webView.loadUrl(url);
    }
}
