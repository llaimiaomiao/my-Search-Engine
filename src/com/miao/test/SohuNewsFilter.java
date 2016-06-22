package com.miao.test;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.postprocessor.FrontierScheduler;

public class SohuNewsFilter extends FrontierScheduler {

	public SohuNewsFilter(String name) {
		super(name);
	}

	protected void schedule(CandidateURI caUri) {
		String uri = caUri.toString();
		if(uri.contains("news.sohu.com")){
		super.schedule(caUri);
		}
	}
	public static void main(String[] args) {
		String str ="ggb,";
		System.out.println(str.substring(0, str.length()-1));
	}

}
