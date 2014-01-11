package com.alec.mealDeal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MyUrl {
	String rootUrl, footer;
	Map<String,String> parameters;
	public MyUrl() {
		parameters = new HashMap<String,String>();
	}
	
	public MyUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}
	
	public MyUrl(int category) {
		
	}
	
	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}
	
	public MyUrl(String rootUrl, Map<String, String> parameters, String footer) {
		this.rootUrl = rootUrl;
		this.parameters = parameters;
		this.footer = footer;
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(rootUrl);
		if (parameters != null && parameters.size() > 0) {
			// for each parameter add it to the url
			for (Entry<String, String> entry : parameters.entrySet()) {
				output.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
		}
		if (footer != null) {
			output.append(footer);
		}
		return output.toString(); 
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	
}
