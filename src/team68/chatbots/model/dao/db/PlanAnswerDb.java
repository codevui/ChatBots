package team68.chatbots.model.dao.db;

import java.util.List;

import team68.chatbots.model.entity.PlanAnswer;

public interface PlanAnswerDb {
	PlanAnswer getById(int id);
	List<PlanAnswer> getListAnswer();

}
