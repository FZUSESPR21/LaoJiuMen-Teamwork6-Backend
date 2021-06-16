package team.ljm.secw.mapper;

import team.ljm.secw.dto.TopicDTO;
import team.ljm.secw.entity.Topic;

import java.util.List;

public interface TopicMapper {

    List<TopicDTO> selectTopicList();

    int insertTopic(Topic topic);

    List<TopicDTO> selectMyTopicList(String account);

    int deleteTopicById(int id);

    List<TopicDTO> selectTopicListByTitle(String title);

    List<TopicDTO> selectTopicListByName(String name);

}
