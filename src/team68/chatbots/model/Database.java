package team68.chatbots.model;

public class Database {
	
	/**table Course */
	private static final String TABLE_COURSE = "tblCourse";
	public static final String COURSE_ID = "idCourse";
	public static final String NUMBER_SLIDE = "numberSlide";
	public static final String NUMBER_QUESTION = "numberQuestion";
	public static final String TITLE = "title";
	
	private static final String CREATE_TABLE_COURSE = "create table tblCourse ("
			+ "id integer primary key," + "numberSlide int,"
			+ "numberQuestion int," + "title text )";
	
	
	/** table Slide */
	private static final String TABLE_SLIDE = "tblSlide";
	
	public static final String SLIDE_ID = "idSlide";
	public static final String ORDER = "order";
	
}
