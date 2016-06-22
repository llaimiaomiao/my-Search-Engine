package com.miao.hdfs;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import jeasy.analysis.MMAnalyzer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SohuNewsSearch extends
		Mapper<LongWritable, Text, Text, Text> {
	private MMAnalyzer mm = new MMAnalyzer();
	private Text text = new Text();
	private int count = 0;
	private static String In_PRIFIX="hdfs://192.168.100.10:9000/sohu_news_01/";
	private static String Out_PRIFIX="hdfs://192.168.100.10:9000/sohu_news_02/";
	static StringBuilder builderMap = new StringBuilder();
	static StringBuilder builderReducer = new StringBuilder();
	public static TreeMap<String, Text> map = new TreeMap<>();
	// 第一个key之是起始偏移量
	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String line = new String(value.getBytes(),"gbk");
		count++;
		switch (count) {
		case 1:
			text.set(line);
			break;
		case 2:
		case 3:
			builderMap.append(line+"");
			break;
		case 4:
			String segment = mm.segment(builderMap.toString(), "|");
			System.out.println(segment);
			String[] values = StringUtils.split(segment, '|');
			for (String k : values) {
				map.put(k, text);
				System.out.println(k+"-->"+text);
			}
			for(Map.Entry<String, Text> entry:map.entrySet()){
				context.write(new Text(entry.getKey()), entry.getValue());
			}
			break;
		}
	}
	static class SohunewsReducer extends Reducer<Text, Text, Text, Text>{
		@Override
		protected void reduce(Text text, Iterable<Text> values,Context context)
				throws IOException, InterruptedException {
			 Iterator<Text> allValues = values.iterator();
			 while(allValues.hasNext()){
				 Text textValue = allValues.next();
				 builderReducer.append(textValue);
				 builderReducer.append(",");
			 }
			String builder = builderReducer.substring(0, builderReducer.length()-1);
			context.write(text, new Text(builder));
			builderReducer.delete(0, builderReducer.length());
		}
		
	}
	class SohuNewsRunner extends Configured implements Tool{
		@Override
		public int run(String[] arg0) throws Exception {
			Job job = Job.getInstance();
			
			job.setJarByClass(SohuNewsSearch.class);
			
			job.setMapperClass(SohuNewsSearch.class);
			job.setReducerClass(SohuNewsSearch.SohunewsReducer.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			
			FileInputFormat.setInputPaths(job, new Path(In_PRIFIX));
			FileOutputFormat.setOutputPath(job, new Path(Out_PRIFIX));
			job.setOutputFormatClass(MyOutputFormat.class);
			return job.waitForCompletion(true)?0:1;
		}
		
	}
	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new SohuNewsSearch().new SohuNewsRunner(),null);
		System.exit(res);
	}

}
