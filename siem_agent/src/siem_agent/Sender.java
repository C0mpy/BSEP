package siem_agent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONObject;

import sun.misc.BASE64Encoder;

public class Sender {
	public static class MyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			// verification of hostname is switched off
			return true;
		}
	}

	public Sender() {
	}

	public int sendPostRequest(JSONObject json) throws IOException {

		// connection and authentication
		String URL_BASE = "https://";
		String method = "POST";
		String userName = "admin";
		String password = "admin";
		String authentication = userName + ':' + password;
		String host = "localhost";
		String port = "8443";

		String url = URL_BASE + host + ":" + port + "/api/logs/save";
		URL obj = new URL(url);

		// open HTTPS connection
		HttpURLConnection con = null;
		con = (HttpsURLConnection) obj.openConnection();
		((HttpsURLConnection) con).setHostnameVerifier(new MyHostnameVerifier());
		con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod(method);
		BASE64Encoder encoder = new BASE64Encoder();
		String encoded = encoder.encode((authentication).getBytes("UTF-8"));
		con.setRequestProperty("Authorization", "Basic " + encoded);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json.toString());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + json.toString());
		System.out.println("Response Code : " + responseCode);
		
		
		/*
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());*/
		return responseCode;
	}

}