package team68.chatbots.model.entity;

public class PlanAnswer {
	private int id;
	private int id_question;
	private String content;
	private boolean isTrue;
	public PlanAnswer(){
		this.id = 0;
		this.id_question = 0;
		this.content = "";
		this.isTrue = false;
	}
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}
	public void setIdQuestion(int idq){
		this.id_question = idq;
	}
	public int getIdQuestion(){
		return this.id_question;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getContent(){
		return this.content;
	}
	public void setIsTrue(boolean is){
		this.isTrue = is;
	}
	public boolean getIsTrue(){
		return this.isTrue;
	}
	

}
