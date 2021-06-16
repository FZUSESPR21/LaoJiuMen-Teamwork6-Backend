package team.ljm.secw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ljm.secw.dto.TopicDTO;
import team.ljm.secw.entity.Topic;

import team.ljm.secw.mapper.TopicMapper;
import team.ljm.secw.service.ITopicService;
import team.ljm.secw.utils.ContentCleanUtil;

import java.util.List;

@Service
public class TopicServiceImpl implements ITopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Override
    public List<TopicDTO> findTopicList() {
        return topicMapper.selectTopicList();
    }

    @Override
    public int addTopic(Topic topic) {
        topic.setTitle(ContentCleanUtil.clean(topic.getTitle()));
        topic.setContent(ContentCleanUtil.clean(topic.getContent()));
        return topicMapper.insertTopic(topic);
    }

    @Override
    public List<TopicDTO> findMyTopicList(String account) {
        return topicMapper.selectMyTopicList(account);
    }

    @Override
    public int removeTopicById(int id) {
        return topicMapper.deleteTopicById(id);
    }

    @Override
    public List<TopicDTO> findTopicListByTitle(String title) {
        return topicMapper.selectTopicListByTitle("%" + title + "%");
    }

    @Override
    public List<TopicDTO> findTopicListByName(String name) {
        return topicMapper.selectTopicListByName("%" + name + "%");
    }

}
