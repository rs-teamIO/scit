package com.scit.xml.DTO;

public class DocumentResponse {
	
	private String id;
	private String type;
	private String status;
	private String title;
	
	public DocumentResponse(String id, String type, String status, String title) {
		super();
		this.id = id;
		this.type = type;
		this.status = status;
		this.title = title;
	}
	
	public DocumentResponse() {}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	
	

}
