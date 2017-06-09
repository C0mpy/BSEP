package siem_agent;

import java.io.*;
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

	String URL_BASE ;
	String method ;
	String userName ;
	String password ;
	String authentication ;
	String host;
	String port;
	String post_url;
	String auth_url;
	String id;
	String key;

	String JWT;
	
	public Sender(JSONObject cfg) {
		
		URL_BASE=(String) cfg.get("url_base");
		method=(String) cfg.get("method");
		userName=(String) cfg.get("username");
		password=(String) cfg.get("password");
		host=(String) cfg.get("host");
		port=(String) cfg.get("port");
		authentication = userName + ':' + password;
		post_url= (String) cfg.get("post_url");
		id=(String) cfg.get("id");
		key=(String) cfg.get("key");
		auth_url = (String) cfg.get("auth_url");
	}

	public synchronized int sendPostRequest(JSONObject json) throws IOException {

		// connection and authentication

		String url = URL_BASE + host + ":" + port + post_url;
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
		con.setRequestProperty("X-Auth-Token",JWT);
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(json.toString());
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

		return responseCode;
	}

	public synchronized boolean authenticate() throws IOException {
		JSONObject json = new JSONObject();
		json.put("email",id);
		json.put("password",key);

		String url = URL_BASE + host + ":" + port + auth_url;
		URL obj = new URL(url);

		// open HTTPS connection
		HttpURLConnection con = null;
		con = (HttpsURLConnection) obj.openConnection();
		((HttpsURLConnection) con).setHostnameVerifier(new MyHostnameVerifier());
		con.setRequestProperty("Content-Type", "application/json");

		con.setRequestMethod("POST");
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
		System.out.println("Response Code : " + responseCode);

					//izvuci token iz odgovora
		this.JWT=readInputStreamToString(con);// this.JWT sacuvaj token tu

		//ako nesto nije kul
		if(responseCode!=200)return false;
		return true;
	}

	//obrisi posle
	public synchronized void  send(String log){
		System.out.println(log);
	}

	private String readInputStreamToString(HttpURLConnection connection) {
		String result = null;
		StringBuffer sb = new StringBuffer();
		InputStream is = null;

		try {
			is = new BufferedInputStream(connection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			result = sb.toString();
		}
		catch (Exception e) {

			result = null;
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException e) {

				}
			}
		}

		return result;
	}

}