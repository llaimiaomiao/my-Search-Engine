package com.miao.hdfs;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.StringUtils;

import jeasy.analysis.MMAnalyzer;

public class TestSoousuo {
	public static void main(String[] args) throws IOException {
		String value ="[������]300��ʦ�����¹�Ϊ��ʦ��ɨ������-�Ѻ������¹� ʦ��";
		MMAnalyzer mm = new MMAnalyzer();
		String segment = mm.segment(value, "|");
		String[] values = StringUtils.split(segment, '|');
		for (String k : values) {
			System.out.println(k+"-->"+k+System.currentTimeMillis());
		}
	}
	

}
