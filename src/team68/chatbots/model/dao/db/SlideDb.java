package team68.chatbots.model.dao.db;

import java.util.List;

import team68.chatbots.model.entity.Slide;

public interface SlideDb {
	Slide getById(int id);
	List<Slide> getListSlide();

}
