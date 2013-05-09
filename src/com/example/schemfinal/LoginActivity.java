package com.example.schemfinal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

class RequestTask extends AsyncTask<String, String, String>{
	String id=null;
	String pass=null;

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        HttpPost httppost = new HttpPost(uri[0]);
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("id", id));
        nameValuePair.add(new BasicNameValuePair("pass",pass));
        
        try {
        	httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // writing error to Log
            e.printStackTrace();
        }
        
        try {
            response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
                //Log.d("MFS",responseString);
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                Log.d("MFS","BAD LOGIN");
                throw new IOException(statusLine.getReasonPhrase());
                
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		
		
		
		final Button button = (Button) findViewById(R.id.submit);
		
		
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(v== button){
            		final RequestTask loginCheck = new RequestTask();
                	final EditText id_Box = (EditText)findViewById(R.id.login_Edit);
            		final EditText pass_Box = (EditText)findViewById(R.id.pass_Edit);
                	loginCheck.id = id_Box.getText().toString();
                    loginCheck.pass = pass_Box.getText().toString();
                    loginCheck.execute("http://schempc.com/demos/checkClasses/index.php");
                    try {
                    	Log.d("MFS","Login Attempt " + id_Box.getText().toString() + pass_Box.getText().toString());
            			Log.d("MFS","Content " + loginCheck.get());
            		} catch (InterruptedException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		} catch (ExecutionException e) {
            			// TODO Auto-generated catch block
            			e.printStackTrace();
            		}
            	}
            	
            }

			private void Toast(String string) {
				// TODO Auto-generated method stub
				
			}
        });
		
		
		
	    
		return true;
	}

}
