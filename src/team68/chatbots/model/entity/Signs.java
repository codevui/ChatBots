package team68.chatbots.model.entity;

public class Signs {
	private int id;
	private String img;
	
	private String title;
	private String text;
	
	public Signs(){
		this.id = 0;
		this.img = "";
		this.title = "";
		this.text = "";
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setImg(String img){
		this.img = img;
	}
	public String getImg(){
		return this.img;
	}
	public void setTitle(String img){
		this.title = img;
	}
	public String getTitle(){
		return this.title;
	}
	public void setText(String img){
		this.text = img;
	}
	public String getText(){
		return this.text;
	}

}
