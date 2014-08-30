package team68.chatbots.model.dao.db;

import java.util.List;

import team68.chatbots.model.entity.Question;

public interface QuestionDb {
	Question getById(int id);
	List<Question> getListQuestion();
	List<Question> getListQuestionByCourseId(int id);
}
