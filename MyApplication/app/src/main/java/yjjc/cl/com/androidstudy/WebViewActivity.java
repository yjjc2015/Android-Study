package yjjc.cl.com.androidstudy;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override//显示网页标题
            public void onReceivedTitle(WebView view, String title) {
                tv.setText(title);
            }

            @Override//Android4.2以下，使用更安全的监听Js的Prompt方法，实现WebView与js交互
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                if (message.equals("1"))
                {
                    Toast.makeText(WebViewActivity.this, "Prompt Open", Toast.LENGTH_SHORT).show();
                    result.confirm("result");
                    return true;
                }
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        //针对3.0以上的系统版本移除有安全问题的js接口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            dealJavaScriptLeak();

        //加载网络请求的界面
//        webView.postUrl(url, null);//Post请求，第二个参数的类型是字节数组，代表请求参数
//        webView.loadUrl(url);
        //加载html字符串
//        String data = "<html><body><h1>Hello, WebView</h1></body></html>";
//        webView.loadData(data, "text/html", "UTF-8");

        //注入JavaScript对象
        webView.addJavascriptInterface(new Object()
        {
            @JavascriptInterface
            public void send(String message)
            {
                Toast.makeText(WebViewActivity.this, "Received message : " + message, Toast.LENGTH_SHORT).show();
                Log.i("chenlong", "Received message : " + message);
            }
        },"androidObject");
        webView.addJavascriptInterface(new MyObject(this), "myObj");
        //加载本地assets下的html资源文件
        webView.loadUrl("file:///android_asset/demo.html");
    }

    //为了解决安全问题，移除Android系统开放的部分js接口
    private void dealJavaScriptLeak()
    {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }
}
