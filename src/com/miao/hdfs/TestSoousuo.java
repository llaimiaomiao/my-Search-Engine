package com.miao.hdfs;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.StringUtils;

import jeasy.analysis.MMAnalyzer;

public class TestSoousuo {
	public static void main(String[] args) throws IOException {
		String value ="[看世界]300老师集体下跪，为人师表扫地无余-搜狐社区下跪 师表";
		MMAnalyzer mm = new MMAnalyzer();
		String segment = mm.segment(value, "|");
		String[] values = StringUtils.split(segment, '|');
		for (String k : values) {
			System.out.println(k+"-->"+k+System.currentTimeMillis());
		}
	}
	

}
