package com.example.iclassy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class DialogueActivity extends Activity {
	public Context mContext;

	public DialogueActivity(Context context) {
		mContext = context;
	}

	public void dialogExample(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(message);
		
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
			}

		});
		builder.show();
	}
}