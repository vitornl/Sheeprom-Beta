package vnl.sheeprombeta;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button search, derivation, integration, limit, power, cos, sin, tan, invert, log, exp;
    private EditText clause;
    private WebView webView;
    private ProgressBar progressBar;
    private InputMethodManager inputMethodManager;
    private GoogleApiClient client;
    private String cookiesBolados = "WR_SID=200.20.0.168.1456250676181406; WolframAlphaSplashOff=true; CookieWarning=nom; _gat_homepage=1; _gat=1; JSESSIONID=29D8F49536A6EFDAC8EF348DB38BB73B; wa_ga_session=73064671875908970000; _ga=GA1.2.897539015.1456250723";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setBackgroundColor(Color.BLACK);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);
        clause = (EditText) findViewById(R.id.teClause);
        webView = (WebView) findViewById(R.id.webView);
        webView.setBackgroundColor(Color.BLACK);
        derivation = (Button) findViewById(R.id.bDerivation);
        derivation.setBackgroundColor(Color.BLACK);
        derivation.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "derivate ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );


        integration = (Button) findViewById(R.id.bIntegrantion);
        integration.setBackgroundColor(Color.BLACK);
        integration.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "integrate ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        limit = (Button) findViewById(R.id.bLim);
        limit.setBackgroundColor(Color.BLACK);
        limit.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "limit x->");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        power = (Button) findViewById(R.id.bPow);
        power.setBackgroundColor(Color.BLACK);
        power.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "^");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        cos = (Button) findViewById(R.id.bCos);
        cos.setBackgroundColor(Color.BLACK);
        cos.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "cos ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        sin = (Button) findViewById(R.id.bSin);
        sin.setBackgroundColor(Color.BLACK);
        sin.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "sin ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        tan = (Button) findViewById(R.id.bTan);
        tan.setBackgroundColor(Color.BLACK);
        tan.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "tan ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        invert = (Button) findViewById(R.id.bInv);
        invert.setBackgroundColor(Color.BLACK);
        invert.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "1/");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        log = (Button) findViewById(R.id.bLog);
        log.setBackgroundColor(Color.BLACK);
        log.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "ln ");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        exp = (Button) findViewById(R.id.bExp);
        exp.setBackgroundColor(Color.BLACK);
        exp.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        clause.setText(clause.getText().toString() + "e");
                        clause.setSelection(clause.getText().length());
                    }
                }
        );

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        search = (Button) findViewById(R.id.bSearch);
        search.setBackgroundColor(Color.BLACK);
        search.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setDomStorageEnabled(true);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setDomStorageEnabled(true);
                        Map<String, String> donuts = new HashMap<String, String>();
                        donuts.put("Cookie", cookiesBolados);
                        if (clause.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Please search for something reasonable", Toast.LENGTH_LONG).show();
                        } else {
                            webView.setWebViewClient(new WebViewClient() {

                                @Override
                                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                    String summary = "<html><body style='background: black;'><p style='color: red;'>Something went worng.</p></body></html>";
                                    view.loadData(summary, "text/html", null);
                                    return;
                                }

                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    webView.loadUrl("javascript:(function() { document.getElementById('titleLogo').style.display='none';})()");
                                    webView.loadUrl("javascript:(function() { document.getElementById('inputForm').style.display='none';})()");
                                    webView.loadUrl("javascript:(function() { document.getElementById('footer').style.display='none';})()");
                                    webView.loadUrl("javascript:(function() { function getElementsByClassName(classname, node)  {\n" +
                                            "    if(!node) node = document.getElementsByTagName(\"body\")[0];\n" +
                                            "    var a = [];\n" +
                                            "    var re = new RegExp('\\\\b' + classname + '\\\\b');\n" +
                                            "    var els = node.getElementsByTagName(\"*\");\n" +
                                            "    for(var i=0,j=els.length; i<j; i++)\n" +
                                            "        if(re.test(els[i].className))a.push(els[i]);\n" +
                                            "    return a;\n" +
                                            "}\n" +
                                            "var elements = new Array();\n" +
                                            "elements = getElementsByClassName('mobile-ad');\n" +
                                            "for(i in elements ){\n" +
                                            "     elements[i].style.display = \"none\";\n" +
                                            "}})()");
                                    progressBar.setVisibility(View.GONE);
                                    webView.setVisibility(View.VISIBLE);
                                }
                            });
                            webView.loadUrl("http://m.wolframalpha.com/input/?i=" + clause.getText().toString(), donuts);
                            webView.setVisibility(View.INVISIBLE);
                            cookiesBolados = CookieManager.getInstance().getCookie("http://m.wolframalpha.com/");
                        }
                    }
                }
        );
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://vnl.sheeprombeta/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://vnl.sheeprombeta/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}