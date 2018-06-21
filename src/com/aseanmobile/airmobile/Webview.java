package com.aseanmobile.airmobile;

import com.aseanmobile.airmobile.R;
import com.aseanmobile.airmobile.RateThisApp;
import com.google.android.gms.ads.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

//If your site don't use JS lib them you just delete this and this line 
//below mWebView.getSettings().setJavaScriptEnabled(true);.
@SuppressLint("SetJavaScriptEnabled")
public class Webview extends Activity {

    WebView mWebView;
  //here insert your url.
  String URL="http://flights.asiarooms.club/";

  ProgressBar loadingProgressBar,loadingTitle;
//this part is important for make the posibility of upload files.
  private ValueCallback<Uri> mUploadMessage;  
  private final static int FILECHOOSER_RESULTCODE=1;  
  
  @Override  
  protected void onActivityResult(int requestCode, int resultCode,  
                                     Intent intent) {  
   if(requestCode==FILECHOOSER_RESULTCODE)  
   {  
    if (null == mUploadMessage) return;  
             Uri result = intent == null || resultCode != RESULT_OK ? null  
                     : intent.getData();  
             mUploadMessage.onReceiveValue(result);  
             mUploadMessage = null;  
   }
   }  
  
@Override
   public void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   setContentView(R.layout.webview);
	mWebView = (WebView) findViewById(R.id.webview);
	mWebView.setHorizontalScrollBarEnabled(true); 
	mWebView.getSettings().setLoadsImagesAutomatically(true);
	mWebView.getSettings().setAllowFileAccess(true);
	mWebView.getSettings().setJavaScriptEnabled(true);
	setProgressBarVisibility(true);
	mWebView.getSettings().setSupportZoom(false);
	mWebView.getSettings().setBuiltInZoomControls(false);
	mWebView.loadUrl(URL);
	mWebView.setWebViewClient(new WebViewClient() {
		//the follow part is for make dial phone or cellphone, send mail, listen mp3 or mp4,3gp and geolocations without crash the apk.
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
   			if (url.startsWith("tel:")) {
   		        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
   		        return true;
   		    } else if (url != null && url.startsWith("vnd.youtube")) {
   	    	    view.getContext().startActivity(
   	    	    	    new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
   	    	    	    return true;
   	    	    	   } 
   	    	    	    else if (url.startsWith("mailto:")) {
   	    	    	    url = url.replaceFirst("mailto:", "");
   	    	    	    url = url.trim();
   	    	    	    Intent i = new Intent(Intent.ACTION_SEND);
   	    	    	    i.setType("plain/text").putExtra(Intent.EXTRA_EMAIL, new String[]{url});
   	    	    	    startActivity(i);
   	    	    	    return true;
   	    	    	  }
   	    	    	   else if (url.endsWith(".mp4") || url.endsWith(".3gp")) {
   	    	    	   Intent intent = new Intent(Intent.ACTION_VIEW);
   	    	    	   intent.setDataAndType(Uri.parse(url), "video/*");
   	    	    	   startActivity(intent);
   	    	    	   return true;
   	    	    	  } else if (url.endsWith(".mp3")) {
   	    	    	    Intent intent = new Intent(Intent.ACTION_VIEW);
   	    	    	    intent.setDataAndType(Uri.parse(url), "audio/*");
   	    	    	    view.getContext().startActivity(intent);   
   	    	    	    return true; 
   	    	    	  } 
   	    	    	    else if (url.startsWith("geo:")) {
   	    	    	    Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
   	    	    	    startActivity(searchAddress); 
   	    	    	    return true;
   	    	    	   } else {
   	    	    	    return false;
   	    	    	 }
   			    }   		
		//Here you can insert the url to your custom error page.
        @Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mWebView.loadUrl("file:///android_asset/www/error.html");
        }
    });

   loadingProgressBar=(ProgressBar)findViewById(R.id.progressbar_Horizontal); 
       mWebView.setWebChromeClient(new WebChromeClient() {	
    	   			//This part switch the upload button in each sdk os.
    	           //The undocumented magic method override  
    	           //Eclipse will swear at you if you try to put @Override here  
    	        // For Android 3.0+
    	        @SuppressWarnings("unused")
				public void openFileChooser(ValueCallback<Uri> uploadMsg) {  

    	            mUploadMessage = uploadMsg;  
    	            Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
    	            i.addCategory(Intent.CATEGORY_OPENABLE);  
    	            i.setType("image/*");  
    	            Webview.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);  

    	           }

    	        // For Android 3.0+
    	           @SuppressWarnings("unused")
				public void openFileChooser( ValueCallback<Uri> uploadMsg, String acceptType ) {
    	           mUploadMessage = uploadMsg;
    	           Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    	           i.addCategory(Intent.CATEGORY_OPENABLE);
    	           i.setType("*/*");
    	           Webview.this.startActivityForResult(
    	           Intent.createChooser(i, "File Browser"),
    	           FILECHOOSER_RESULTCODE);
    	           }

    	        //For Android 4.1
    	           @SuppressWarnings("unused")
				public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
    	               mUploadMessage = uploadMsg;  
    	               Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
    	               i.addCategory(Intent.CATEGORY_OPENABLE);  
    	               i.setType("image/*");  
    	               Webview.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), Webview.FILECHOOSER_RESULTCODE );

    	           }  	   
    	   @Override
		public void onProgressChanged(WebView view, int newProgress) {
    	   super.onProgressChanged(view, newProgress);
    	   loadingProgressBar.setProgress(newProgress);
    	   if (newProgress == 100) {
    	   loadingProgressBar.setVisibility(View.GONE);   	   
    	   } else{
    	   loadingProgressBar.setVisibility(View.VISIBLE); 	   
    	   				}
    	   			}
    	   		});
     //Here you can change the language of not connection or lost connection.
       if(!isNetworkConnectionAvailable()){
           Context context = getApplicationContext();
           CharSequence text = "Lo siento, usted necesita una conexión a internet!  Por favor, trate de nuevo cuando  este la " +
                   " conexión disponible.";
           int duration = Toast.LENGTH_LONG;

           Toast toast = Toast.makeText(context, text, duration);
           toast.show();
           finish();
    	   }
}

	public boolean isNetworkConnectionAvailable() { 
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
		    NetworkInfo netInfo = cm.getActiveNetworkInfo(); 

		    if (netInfo != null && netInfo.isConnectedOrConnecting()){ 
		        return true;
		    }
		    else{
		        return false;
		    } 
		}
	@Override
	protected void onStart() {
		super.onStart();
		// Monitor launch times and interval from installation
		RateThisApp.onStart(this);
		// Show a dialog if criteria is satisfied
		RateThisApp.showRateDialogIfNeeded(this);
	}
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(mWebView.canGoBack() == true){
                    mWebView.goBack();
                }else{
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
	public boolean shouldOverrideUrlLoading1(WebView view, String url) {

	 view.loadUrl(url);
	 return true;
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.layout.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	//The follow part is to insert your home url and your play store id.
        switch (item.getItemId())
        {
        case R.id.menu_home:
        	mWebView.loadUrl("http://www.grupovg.info/musica/index.php"); 
            return true;
        case R.id.menu_refresh:
        	mWebView.reload();
            return true;
        case R.id.menu_share:
        	Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=info.grupovg.WebSplashRate");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Hey!, Check out this great App that I just found");
            startActivity(Intent.createChooser(sharingIntent, "Share this App with your friends"));
            return true;
        case R.id.menu_close:
        	{ finish();
                System.exit(0);
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}