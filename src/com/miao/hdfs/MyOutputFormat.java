package com.miao.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyOutputFormat<K,V> extends FileOutputFormat<K, V> {
	private FileSystem fs =null;
	private FSDataOutputStream fsout;
	private static String Out_PRIFIX="D:/sohu_news_03/";
	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		fs = FileSystem.get(new Configuration());
		 fsout = fs.create(new Path(Out_PRIFIX));
		return new MyRecordWriter(fsout);
	}
	public class MyRecordWriter extends RecordWriter<K, V>{


		private FSDataOutputStream fsout;

		public MyRecordWriter(FSDataOutputStream fsout) {
			this.fsout = fsout;
		}

		@Override
		public void write(K key, V value) throws IOException,
				InterruptedException {
			fsout.write(key.toString().getBytes("gbk"));
			fsout.write("  ".getBytes());
			fsout.write(value.toString().getBytes());
			fsout.write("\n".getBytes("gbk"));
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			if(fsout!=null){
				fsout.close();
			}
		}
	}
}
