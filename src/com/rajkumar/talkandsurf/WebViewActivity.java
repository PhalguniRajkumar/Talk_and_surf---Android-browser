package com.rajkumar.talkandsurf;


import java.util.ArrayList;
import java.util.Locale;





import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;


public class WebViewActivity extends Activity implements OnInitListener {

		protected static final int RESULT_SPEECH = 1;
		private WebView webView;
		private ImageButton imgButton;
		private ProgressDialog progressBar;
		private boolean typeFlag = false;
		private String url;
		private TextToSpeech tts;
		private Bundle extra;
		private EditText editText;
		//ActionBar actionbar;
		
		@SuppressLint("SetJavaScriptEnabled")
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);			
			setContentView(R.layout.web_view);
			webView = (WebView)findViewById(R.id.webView1);
			
			//actionbar.setDisplayShowTitleEnabled(false);
			// actionbar.setDisplayShowHomeEnabled(false);
			
			tts = new TextToSpeech(this, this);
			


			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.setScrollbarFadingEnabled(true);
			webView.setWebViewClient(new MyWebViewClient());
			webView.canGoBackOrForward(5);	
			
			//book
			 webView.setWebViewClient(new ViewClient() {
				EditText editText = (EditText)findViewById(R.id.txtURL);      
				@Override
				public boolean shouldOverrideUrlLoading(WebView v, String url) {
					// TODO Auto-generated method stub
					v.loadUrl(url);
					return true;
				}
				public void onPageFinished(WebView view, String url) {  
				    super.onPageFinished(view, url);
				    editText.setText(url);
}
		});
			 
						
			extra = getIntent().getExtras();
			
			typeFlag = (extra.getString("flag").equals("webpage")) ? true : false;
			
			Log.d("Flag value", "" + typeFlag);
			
			url = extra.getString("url");
			url = formAddress(url);
				
			//String display = "Loading " + url + " ...";
			//progressBar = ProgressDialog.show(WebViewActivity.this, "Web Browser", display);			
			webView.loadUrl(url);		
			
			imgButton = (ImageButton)findViewById(R.id.speakBt);
		
			imgButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent speechEngine = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
					speechEngine.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN");
					
					try
					{
						startActivityForResult(speechEngine, RESULT_SPEECH);
					}
					catch(ActivityNotFoundException e)
					{
						Toast t = Toast.makeText(getApplicationContext(),  "Opps! Your device doesn't support Speech to Text",
	                            Toast.LENGTH_SHORT);
						t.show();
					}
				}
			});
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
				
		private class MyWebViewClient extends WebViewClient
		{
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return false;
			}
			
			public void onPageFinished(WebView view, String address)
			{
				if(progressBar.isShowing())
					progressBar.dismiss();
				
				if(!typeFlag)
				{
					String username = extra.getString("username");
					String password = extra.getString("password");
					if(url.contains("gmail"))
					{
						webView.loadUrl("javascript: {document.getElementById(\"Email\").value =\""+username+"\"};"); 
						webView.loadUrl("javascript: {document.getElementById(\"Passwd\").value =\""+password+"\"};");
						webView.loadUrl("javascript: {document.getElementById(\"signIn\").click();};");
					}
					else if(url.contains("yahoo"))
					{						
						webView.loadUrl("javascript: {document.getElementByName(\"id\").value =\""+username+"\"};"); 
						webView.loadUrl("javascript: {document.getElementByName(\"password\").value =\""+password+"\"};");
						webView.loadUrl("javascript: {document.getElementByName(\"__submit\").click();};");
					}
					else if(url.contains("aol"))
					{	
						webView.loadUrl("javascript: {document.getElementByName(\"loginId\").value =\""+username+"\"};"); 
						webView.loadUrl("javascript: {document.getElementByName(\"password\").value =\""+password+"\"};");
						webView.loadUrl("javascript: {document.getElementById(\"submitID\").click();};");
					}
					else if(url.contains("bits"))
					{
						webView.loadUrl("javascript: {document.getElementByName(\"username\").value =\""+username+"\"};"); 
						webView.loadUrl("javascript: {document.getElementByName(\"password\").value =\""+password+"\"};");
//						webView.loadUrl("javascript: {document.getElementById(\"submitID\").click();};");						
					}
				}
					
			}
		}
		
		protected void onActivityResult(int requestCode, int resultCode, Intent data) 
		{
		        super.onActivityResult(requestCode, resultCode, data);
		 
		        switch (requestCode) 
		        {
			        case RESULT_SPEECH: 
			        {
			            if (resultCode == RESULT_OK && null != data) 
			            {
			 
			                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);		 
			                String input = text.get(0);
			                takeAction(input);					
			            }
			            break;
			        }	 
		        }
		    }
		
		public void takeAction(String input)
		{
						
			if(input.equalsIgnoreCase("back"))
				webView.goBack();
			else if(input.equalsIgnoreCase("forward") || input.contains("for") || input.contains("word"))
				webView.goForward();
			else if(input.equalsIgnoreCase("refresh"))
				webView.reload();
			else if(input.equalsIgnoreCase("stop"))
				webView.stopLoading();
			else if(input.equalsIgnoreCase("speak"))
			{
				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				String toSpeak = clipboard.getText().toString();
				Log.d("speak out", toSpeak);
				speakOut(toSpeak);
			}
			else if(input.equalsIgnoreCase("exit"))
				System.exit(0);
			else
			{
				if((webView.getUrl()).contains("google"))
				{
					String search = "";
					for(int i = 0; i < input.length(); i++)
						if(input.charAt(i) == ' ')
							search += "%20";
						else
							search += input.charAt(i);
					String searchUrl = "http://www.google.co.in/search?hl=en&tbo=d&site=&source=hp&q=" +
							search + "&oq=" + search + " &gs_l=mobile-gws-hp.12..0l4j0i3.907.4862."
							+ "0.7075.8.4.0.1.1.0.1191.2167.6-1j1.2.0.les%3B..0.0...1ac.1.tfgrH2ZQAXc";
					webView.loadUrl(searchUrl);
							
				}
				else
				{
					String url = formAddress(input);
					Toast t = Toast.makeText(getApplicationContext(),  url, Toast.LENGTH_SHORT);
					t.show();
					webView.loadUrl(url);
				}
			}
				
		}		
		
		public String formAddress(String address)
		 {
			 String result = "";
			 
			 if(address.equals(""))
				 return result;
			 else
			 {
				 if(address.contains("."))
					 if(address.contains("http:") || address.contains("https:") || address.contains("ftp:"))
						 result = address;
					 else
						 if(!typeFlag)
							 result = "https://" + address + "/";
						 else
							 result = "http://www." + address + "/";				 			 
				 else
				 	 result = "http://www." + address + ".com/";				 	 
			 }
			 
			 return result;
		 }

		@Override
		public void onInit(int status) {
			// TODO Auto-generated method stub
	          //TTS is successfully initialized
	        if (status == TextToSpeech.SUCCESS) 
	        {
	                       //Setting speech language     	
	            int result = tts.setLanguage(Locale.getDefault());
	           //If your device doesn't support language you set above
	            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
	            {
	                           //Cook simple toast message with message
	                Toast.makeText(this, "Language not supported", Toast.LENGTH_LONG).show();
	                Log.e("TTS", "Language is not supported");
	            }	            
	            //TTS is not initialized properly
	        } 
	        else 
	        {
	                    Toast.makeText(this, "TTS Initilization Failed", Toast.LENGTH_LONG).show();
	                    Log.e("TTS", "Initilization Failed");
	        }
			
		}
		
		private void speakOut(String text) 
		{
	        //Get the text typed
//		       String text = txtText.getText().toString();
		        //If no text is typed, tts will read out 'You haven't typed text'
		        //else it reads out the text you typed
		       if (text.length() == 0) 
		           tts.speak("You haven't typed text", TextToSpeech.QUEUE_FLUSH, null);
		        else
		           tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);	       

		   }
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()){
			case R.id.prev:
				if(webView.canGoBack())
				webView.goBack();//go to previous webpage
				break;
			case R.id.next:
				if(webView.canGoForward())
					webView.goForward();//go to next webpage
				break;
			case R.id.refresh:
				webView.reload();//reload the webpage
				break;
			case R.id.bookmark:
				EditText urll=(EditText)findViewById(R.id.txtURL);
				String cur_url=urll.getText().toString();
				Intent i=new Intent(WebViewActivity.this,Bookmark.class);
				i.putExtra("m",cur_url);
				startActivity(i);
				
		
				break;
			}
		
			
			
			
			return super.onOptionsItemSelected(item);
		}
		//book
		@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			
		try {
			Intent k=getIntent();
			Bundle b=k.getExtras();
			String load_url=b.getString("ur");
			webView.loadUrl(load_url);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
			
			
			
		}
		


}
