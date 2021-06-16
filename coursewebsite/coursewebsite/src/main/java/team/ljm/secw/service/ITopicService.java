package team.ljm.secw.service;

import team.ljm.secw.dto.TopicDTO;
import team.ljm.secw.entity.Topic;

import java.util.List;

public interface ITopicService {

    List<TopicDTO> findTopicList();

    int addTopic(Topic topic);

    List<TopicDTO> findMyTopicList(String account);

    int removeTopicById(int id);

    List<TopicDTO> findTopicListByTitle(String title);

    List<TopicDTO> findTopicListByName(String account);

}
