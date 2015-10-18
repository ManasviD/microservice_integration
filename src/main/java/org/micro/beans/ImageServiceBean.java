package org.micro.beans;

public class ImageServiceBean {
	
	private Url [] results;
	
	public Url[] getResults() {
		return results;
	}

	public void setResults(Url[] results) {
		this.results = results;
	}

	public static class Url
	{
		private String  url_fullxfull;

		public String getUrl_fullxfull() {
			return url_fullxfull;
		}

		public void setUrl_fullxfull(String url_fullxfull) {
			this.url_fullxfull = url_fullxfull;
		}
		
	}

}
