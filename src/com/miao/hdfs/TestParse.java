package com.miao.hdfs;

import java.io.File;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class TestParse {
	private MimetypesFileTypeMap fileType;
	private static FileSystem fs = null;
	private static int count = 0;
	private static String HEAD_PRIFIX="hdfs://192.168.100.10:9000/sohu_news_01/";

	public static void main(String[] args) throws ParserException {
		parseDir("D:/software/sousuo/heritrix-1.12.1/jobs/my_pro5-20160617095826319/mirror/");
	}
	public static void parse(String path) throws ParserException{
		path = path.replaceAll("\\\\", "/");
		Parser parser = new Parser(path);
		parser.setEncoding("gbk");
		//��ǩ������
		TagNameFilter titlefilter = new TagNameFilter("title");
		//parse�����õ�һ��ڵ�
		NodeList nodeList =null;
		Node text = null;
		String title = null;
	    try {
			nodeList = parser.parse(titlefilter);
			//ȡ�õ�һ��ڵ�
			text = nodeList.elementAt(0);
			//��ȡ��һ��ڵ��һ�������
			title = text.getFirstChild().getText();
			//��������
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    NodeList keys = null;
	    Node keyword = null;
	    String key = null;
	    String uri = null;
		parser.reset();
		AndFilter keywordFilter = new AndFilter(new TagNameFilter("meta"), 
				new HasAttributeFilter("name","keywords"));
		
		try {
			keys = parser.parse(keywordFilter);
			keyword = keys.elementAt(0);
			key = keyword.toHtml();
			key = key.substring(key.indexOf("content=\"")+9);
			key = key.substring(0,key.indexOf("\""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			uri = "http:/"+path.substring(path.indexOf("mirror")+6);
		} catch (Exception e) {
			e.printStackTrace();
		}
		count++;
		System.out.println("�����:"+count);
		try {
			Path p = new Path( HEAD_PRIFIX+count+".txt");
			fs = p.getFileSystem(new Configuration());
			FSDataOutputStream out = fs.create(p);
			String result = count+"\r\n"+title+"\r\n"+key+"\r\n"+uri;
			out.write(result.getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public static void parseDir(String path) throws ParserException{
		File f = new File(path);
		if(f.isDirectory()){
			File[] files = f.listFiles();
			for(File file:files){
				parseDir(file.getAbsolutePath());
			}
		}
		else{
			if(f.getAbsolutePath().endsWith("html")||f.getAbsolutePath().endsWith("htm")||f.getAbsolutePath().endsWith("shtml"))
			parse(f.getAbsolutePath());
		}
	
		
	}

}
