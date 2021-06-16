package team.ljm.secw.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ljm.secw.dto.TopicCommentDTO;
import team.ljm.secw.entity.TopicComment;
import team.ljm.secw.service.ITopicCommentService;
import team.ljm.secw.vo.ResponseVO;

import java.util.List;

@Controller
public class TopicCommentController {

    @Autowired
    private ITopicCommentService TopicCommentService;

    @RequiresRoles(value={"student","teacher"}, logical = Logical.OR)
    @RequestMapping(value = "/comment/all", method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO showTopicCommentListByTopicId(@RequestParam("topicId") String topicId,
                                                    @RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        PageHelper.startPage(pn, 5);
        List<TopicCommentDTO> topicCommentList = TopicCommentService.findTopicCommentListByTopicId(topicId);
        PageInfo<TopicCommentDTO> pageInfo = new PageInfo<>(topicCommentList, 5);
        return new ResponseVO("200", "", pageInfo);
    }

    @RequiresRoles(value={"student","teacher"}, logical = Logical.OR)
    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO deleteTopicCommentById(@RequestBody TopicComment topicComment) {
        return new ResponseVO("200", "", TopicCommentService.removeTopicCommentById(topicComment.getId()));
    }

    @RequiresRoles(value={"student","teacher"}, logical = Logical.OR)
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO addTopicCommentByTopicId(@RequestBody TopicComment topicComment) {
        if (StrUtil.isBlank(topicComment.getContent())) {
            return new ResponseVO("403", "内容不能为空");
        }
        int successNum = TopicCommentService.addTopicComment(topicComment);
        if (successNum == 1) {
            TopicCommentService.modifyTopicCommentNumByTopicId(topicComment.getTopicId());
        }
        return new ResponseVO("200", "", successNum);
    }

}
