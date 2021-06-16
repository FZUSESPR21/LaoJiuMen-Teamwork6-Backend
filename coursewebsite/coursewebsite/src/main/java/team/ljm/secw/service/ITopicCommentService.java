package team.ljm.secw.service;

import team.ljm.secw.dto.TopicCommentDTO;
import team.ljm.secw.entity.TopicComment;

import java.util.List;

public interface ITopicCommentService {

    List<TopicCommentDTO> findTopicCommentListByTopicId(String topicId);

    int removeTopicCommentById(int id);

    int addTopicComment(TopicComment topicComment);

    int modifyTopicCommentNumByTopicId(int id);

    int removeTopicCommentByTopicId(int topicId);
}
