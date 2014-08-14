package team68.chatbots.model.dao.db;

import java.util.List;

import team68.chatbots.model.entity.Course;

public interface CourseDb {
	Course getById(int id);
	//boolean update(Course course);
	//boolean insertCourse(Course course);
	//boolean delele(int id);
	List<Course> getListCourse();
}