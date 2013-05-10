package com.example.iclassy;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ClassListViewActivity extends ListActivity {

	ListView myList;
	String id, pass;
	int mNotificationId = 001;
	String[] listContent, listStatus;
	private Handler handler = new Handler();
	NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.alert);

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			update();
			handler.postDelayed(this, 10000);
		}
	};

	class IconicAdapter extends ArrayAdapter<String> {
		public IconicAdapter() {
			super(getApplicationContext(), R.layout.row, R.id.label,
					listContent);
		}
		
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			if (listStatus[position].length() > 4) {
				icon.setImageResource(R.drawable.bad);
			} else {
				icon.setImageResource(R.drawable.ok);
			}
			TextView size = (TextView) row.findViewById(R.id.size);
			size.setText("Status: " + listStatus[position]);

			return (row);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Intent i = getIntent();
		ArrayList<String> stringArrayList = i
				.getStringArrayListExtra("stringArrayList");
		ArrayList<String> stringArrayListStatus = i
				.getStringArrayListExtra("stringArrayListStatus");

		id = getIntent().getExtras().getString("id");
		pass = getIntent().getExtras().getString("pass");

		listContent = stringArrayList
				.toArray(new String[stringArrayList.size()]);
		listStatus = stringArrayListStatus
				.toArray(new String[stringArrayListStatus.size()]);

		super.onCreate(savedInstanceState);
		setListAdapter(new IconicAdapter());

		handler.postDelayed(runnable, 10000);

		// setContentView(R.layout.activity_classlistview);

		// myList = (ListView) findViewById(R.id.list);
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_multiple_choice, listContent);
		// myList.setAdapter(adapter);

	}

	public void notify(String title, String message) {
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder.setContentTitle(title).setContentText(message);
		mNotifyMgr.notify(mNotificationId++, mBuilder.build());

		DialogueActivity otherClass = new DialogueActivity(this);
		otherClass.dialogExample(message);

	}

	public void update() {
		Log.d("MFS", "update()");
		final RequestTask loginCheck = new RequestTask();
		loginCheck.id = id;
		loginCheck.pass = pass;
		loginCheck.execute("http://schempc.com/demos/checkClasses/index.php");
		try {
			// Log.d("MFS","Login Attempt " + id_Box.getText().toString() +
			// pass_Box.getText().toString());
			if (loginCheck.get() != null) {
				// Log.d("MFS",loginCheck.get());
				ArrayList<String> stringArrayList = new ArrayList<String>();
				ArrayList<String> stringArrayListStatus = new ArrayList<String>();
				JSONArray classes = null;
				try {
					classes = new JSONArray(loginCheck.get().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < classes.length(); i++) {
					// Log.d("MFS",classes.getString(i));
					JSONArray class_i = new JSONArray(classes.getString(i));
					stringArrayList.add(class_i.getString(0));
					stringArrayListStatus.add(class_i.getString(1));
				}

				for (int i = 0; i < listStatus.length; i++) {
					if (!stringArrayListStatus.get(i).equals(listStatus[i])
							&& stringArrayList.get(i).equals(listContent[i])) {
						
						Log.d("MFS",stringArrayListStatus.get(i) + " " + listStatus[i] + " "
							+ stringArrayListStatus.get(i) + " " + listStatus[i]
							+ " " + stringArrayList.get(i) + " " + listContent[i]);
									
						
						notify("Class Activity Detected!", listContent[i]
								+ " is now " + stringArrayListStatus.get(i) + ".");
					}
				}

				listContent = stringArrayList
						.toArray(new String[stringArrayList.size()]);
				listStatus = stringArrayListStatus
						.toArray(new String[stringArrayListStatus.size()]);
				setListAdapter(new IconicAdapter());

			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("MFS", "onResume()");
		update();
	}
}