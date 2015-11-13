package com.example.picturecpmpare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static String sendRequest(String param)
	{
		HttpURLConnection connection = null;
		PrintWriter out = null;
		BufferedReader read = null;
		String result = "";
		try {
			URL url = new URL("http://192.168.1.113:9090/cloudserver/login/upload");
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(8000);
			connection.setReadTimeout(8000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			out = new PrintWriter(connection.getOutputStream());
			out.print(param);
			out.flush();
			//读取客户端返回数据
			read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line =read.readLine())!=null )
			{
				result = line;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return result;		
		}
		
		finally{
			if(out!=null)
			{
				out.close();
			}
			if(read!=null)
			{
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	
}
