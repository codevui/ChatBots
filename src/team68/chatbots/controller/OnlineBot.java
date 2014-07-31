package team68.chatbots.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import team68.chatbots.activity.MessageActivity;
import team68.chatbots.model.Message;
import android.os.AsyncTask;
import android.util.Log;

public class OnlineBot extends AsyncTask<String, String, String> {
	private String botID = "53c8cb22e4b04a9d4459958d";
	private String token = "3848cfd4-faa5-425c-8dbc-23aa69001419";
	private String url;
	private String res;
	private String request;
	private MessageActivity myActivity;
	private InputStream is = null;
	private JSONObject result = null;

	public OnlineBot(MessageActivity m) {
		this.myActivity = m;
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			url = "http://tech.fpt.com.vn/AIML/api/bots/" + botID
					+ "/chat?request=" + URLEncoder.encode(request,"UTF-8") + "&token=" + token;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Log.d("test", url);
		/*
		 * HTTP GET
		 */
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.d("ta", is.toString());
		} catch (Exception e) {
			Log.d("fail", url);
			return null;
		}

		/*
		 * Convert data to string
		 */
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			res = sb.toString();
		} catch (Exception e) {
			return null;
		}

		/*
		 * Convert string result to JSONObject
		 */

		try {
			result = new JSONObject(res);
		} catch (JSONException e) {
			return null;
		}
		try {
			res = result.getString("response");
			return res;
		} catch (JSONException e) {
			return null;
		}

	}

	@Override
	protected void onPostExecute(String res) {
		if (res != null) {

			myActivity.addNewMessage(new Message(res, false),true);

		}

	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

}
