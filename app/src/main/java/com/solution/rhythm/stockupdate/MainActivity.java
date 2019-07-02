package com.solution.rhythm.stockupdate;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference dbref;

    String custNo, custName,password;
    String ms_st="1", hsd_st = "1", pow_st="0", tur_st="0", ms_sa="1", hsd_sa="0", pow_sa="0", tur_sa="0",dtp="0", edc="0", wallet="0";

    WebView webView;
    EditText custNameEditText;
    Button prev,next;

    int position=0;

   public void hideKeyboard()
   {
       //Hide Keyboard
       InputMethodManager inputManager = (InputMethodManager)
               getSystemService(Context.INPUT_METHOD_SERVICE);

       inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
               InputMethodManager.HIDE_NOT_ALWAYS);
   }

   public void fetchData()
   {
       dbref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               custNo = dataSnapshot.child(String.valueOf(position)).child("customer code").getValue().toString();
               custName = dataSnapshot.child(String.valueOf(position)).child("customer name").getValue().toString();
               password = dataSnapshot.child(String.valueOf(position)).child("password").getValue().toString();

               ms_st = dataSnapshot.child(String.valueOf(position)).child("ms_stock").getValue().toString();
               hsd_st = dataSnapshot.child(String.valueOf(position)).child("hsd_stock").getValue().toString();
               pow_st = dataSnapshot.child(String.valueOf(position)).child("pow_stock").getValue().toString();
               tur_st = dataSnapshot.child(String.valueOf(position)).child("tur_stock").getValue().toString();

               ms_sa = dataSnapshot.child(String.valueOf(position)).child("ms_sales").getValue().toString();
               hsd_sa = dataSnapshot.child(String.valueOf(position)).child("hsd_sales").getValue().toString();
               pow_sa = dataSnapshot.child(String.valueOf(position)).child("pow_sales").getValue().toString();
               tur_sa = dataSnapshot.child(String.valueOf(position)).child("tur_sales").getValue().toString();

               dtp = dataSnapshot.child(String.valueOf(position)).child("dtp").getValue().toString();
               edc = dataSnapshot.child(String.valueOf(position)).child("edc").getValue().toString();
               wallet = dataSnapshot.child(String.valueOf(position)).child("wallet").getValue().toString();

               Log.i("Login Status",custNo+custName+password);
               custNameEditText.setText(custName);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Log.i("Database error",databaseError.getMessage());
           }
       });
   }
   public void factorize()
   {
       EditText stockfactor = (EditText)findViewById(R.id.stockEditText);
       EditText salesfactor = (EditText)findViewById(R.id.salesEditText);
       EditText cashfactor = (EditText)findViewById(R.id.cashEditText);

       Double st_fac = Double.parseDouble(stockfactor.getText().toString());
       Double sa_fac = Double.parseDouble(salesfactor.getText().toString());
       Double ca_fac = Double.parseDouble(cashfactor.getText().toString());

       ms_st = String.valueOf(Math.round(Double.parseDouble(ms_st)*st_fac));
       hsd_st = String.valueOf(Math.round(Double.parseDouble(hsd_st)*st_fac));
       pow_st = String.valueOf(Math.round(Double.parseDouble(pow_st)*st_fac));
       tur_st = String.valueOf(Math.round(Double.parseDouble(tur_st)*st_fac));

       ms_sa = String.valueOf(Math.round(Double.parseDouble(ms_sa)*sa_fac));
       hsd_sa = String.valueOf(Math.round(Double.parseDouble(hsd_sa)*sa_fac));
       pow_sa = String.valueOf(Math.round(Double.parseDouble(pow_sa)*sa_fac));
       tur_sa = String.valueOf(Math.round(Double.parseDouble(tur_sa)*sa_fac));

       dtp = String.valueOf(Math.round(Double.parseDouble(dtp)*ca_fac));
       edc = String.valueOf(Math.round(Double.parseDouble(edc)*ca_fac));
       wallet = String.valueOf(Math.round(Double.parseDouble(wallet)*ca_fac));
   }
    public void nextButtonClick(View view)
    {
        hideKeyboard();

        position++;
        if(position>78){position=0;}

        fetchData();
    }

    public void prevButtonClick(View view)
    {
        hideKeyboard();

        position--;
        if(position<0){position=78;}

        fetchData();
    }

    public void submitButtonClick(View view)
    {
        hideKeyboard();

        prev.setEnabled(false);
        next.setEnabled(false);
        //updateStock();
        stock();

    }
    public void loginPage()
    {
        webView.loadUrl("https://sales.hpcl.co.in/bportal/index_sales.jsp");
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("Status","Page Started"+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("Status","Page Finished"+url);

                inputCustomer();
            }
        });
    }
    public void inputCustomer()
    {
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("Status","Input Page Started");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.i("Status","Input Page Finished");
                final String js = "javascript:" +
                        "document.getElementById('password').value = '" + password + "';" +
                        "document.getElementById('custid').value = '" + custNo + "';" +
                        "document.getElementById('submit').click()";

                if (Build.VERSION.SDK_INT >= 19) {
                    view.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.i("Status","Build KITKAT");
                        }
                    });
                } else {
                    view.loadUrl(js);
                    Log.i("Status","Build NOT KITKAT");
                }
                Log.i("Status", "Logged In");
                webView.setWebViewClient(new WebViewClient()
                {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.i("Status","Page Started"+url);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.i("Status", "Sending Stock Page");

                        stockpage();
                    }
                });
                //stockpage();
            }
        });

    }
    public void stockpage()
    {

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("Status","Page Started"+url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("Status", "Stock Page");

                webView.loadUrl("https://sales.hpcl.co.in/bportal/dealerstockweb/send_stock.jsp");
                enterStock();
            }
        });
    }
    public void enterStock()
    {
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("Status","Stock Page Started");
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                super.onPageFinished(view, url);

                Log.i("Status","Stock Page Finished");
                factorize();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(
                            "javascript: document.getElementById('totalsalescashcrdt_stored').value ;",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String s) {
                                    s = s.replaceAll("^\"|\"$", "");
                                    Log.i("Total Sales Status",String.valueOf(s.length()));

                                    if(s.length()==0)
                                    {
                                        Log.i("Status", "Data NOt Updated");
                                        //pos[0]=1;
                                        final String jsc = "javascript: " +
                                                "document.getElementById('msstock').value = '" + ms_st + "';" +
                                                "document.getElementById('hsdstock').value = '" + hsd_st + "';"+
                                                "document.getElementById('powerstock').value = '" + pow_st + "';"+
                                                "document.getElementById('turbostock').value = '" + tur_st + "';"+
                                                "document.getElementById('mssale').value = '" + ms_sa + "';"+
                                                "document.getElementById('hsdsale').value = '" + hsd_sa + "';"+
                                                "document.getElementById('powersale').value = '" + pow_sa + "';"+
                                                "document.getElementById('turbosale').value = '" + tur_sa + "';" +
                                                "document.getElementById('totalsalesdtplshp').value = '" + dtp+ "';" +
                                                "document.getElementById('totalsalesccdc').value = '" + edc+ "';" +
                                                "document.getElementById('totalsalesmwallet').value = '" + wallet+ "';"+
                                                "calculateSum()+dosubmit()";

                                        if (Build.VERSION.SDK_INT >= 19) {
                                            view.evaluateJavascript(jsc, new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String s) {

                                                }
                                            });
                                        } else {
                                            view.loadUrl(jsc);
                                        }
                                    }else
                                    {
                                        Log.i("Status", "Data Already Updated");
                                        Toast.makeText(getBaseContext(), "Data Already Updated!",
                                                Toast.LENGTH_SHORT).show();
                                        prev.setEnabled(true);
                                        next.setEnabled(true);
                                    }
                                    // code here
                                }
                            });
                }


                webView.setWebViewClient(new WebViewClient()
                {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.i("Status","Page Started"+url);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        Log.i("Status", "Submitting Stock Page");
                        prev.setEnabled(true);
                        next.setEnabled(true);
                    }
                });
            }
        });

    }
    public void stock()
    {
        final int[] page = {0};
        Log.i("Button Status","Clicked");
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://sales.hpcl.co.in/bportal/login/logout.jsp");

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("Status","Page Started");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                page[0] =1;
                Log.i("Status","Page Finished"+page[0]);
                loginPage();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        custNameEditText = (EditText) findViewById(R.id.custNameEditText);

        dbref = FirebaseDatabase.getInstance().getReference();

        webView = (WebView) findViewById(R.id.webView);
        //webView1 = (WebView) findViewById(R.id.webView1);

        prev = (Button) findViewById(R.id.prevButton);
        next = (Button) findViewById(R.id.nextButton);

        fetchData();

    }
}
