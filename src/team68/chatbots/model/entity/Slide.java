package team68.chatbots.model.entity;

public class Slide {
	private int id;
	private int idCourse;
	private int order;
	private String image;
	private String textPreview;
	private String textToSay;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdCourse() {
		return idCourse;
	}
	public void setIdCourse(int idCourse) {
		this.idCourse = idCourse;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTextPreview() {
		return textPreview;
	}
	public void setTextPreview(String textPreview) {
		this.textPreview = textPreview;
	}
	public String getTextToSay() {
		return textToSay;
	}
	public void setTextToSay(String textToSay) {
		this.textToSay = textToSay;
	}
	
	
}
