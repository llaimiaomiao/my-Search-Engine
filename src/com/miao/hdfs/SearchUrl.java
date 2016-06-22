package com.miao.hdfs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SearchUrl {
	public static void main(String[] args) throws Exception {
		parse("http://lol.qq.com");
	}
	public static void parse(String urlStr) throws Exception{
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line =null;
		while((line=buf.readLine())!=null){
			System.out.println(line);
		}
		
		
	}
}
