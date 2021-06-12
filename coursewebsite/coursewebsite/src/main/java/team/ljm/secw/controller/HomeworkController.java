package team.ljm.secw.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import team.ljm.secw.dto.HomeworkInfoDTO;
import team.ljm.secw.entity.Homework;
import team.ljm.secw.entity.HomeworkResult;
import team.ljm.secw.entity.Resource;
import team.ljm.secw.entity.Student;
import team.ljm.secw.service.IHomeworkService;
import team.ljm.secw.service.IHomeworkresultService;
import team.ljm.secw.service.IStudentMgtService;
import team.ljm.secw.vo.ResponseVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class HomeworkController {

    @Autowired
    private IHomeworkService homeworkService;

    @Autowired
    private IHomeworkresultService homeworkResultService;

    //课程作业，教师，全部,测试用
    @RequiresRoles("teacher")
    @RequestMapping("/teacher/homework/real_all")
    @ResponseBody
    public ResponseVO allHomework(@RequestParam(value = "pn",defaultValue = "1") int pn){
        PageHelper.startPage(pn,5);
        List<HomeworkInfoDTO> list = new ArrayList<>();
        Date date = new Date();
        for (Homework homework:homeworkService.findAll()){
            if (homework.getStartAt().before(date)){
                HomeworkInfoDTO homeworkInfoDTO = new HomeworkInfoDTO();
                homeworkInfoDTO.setClazzId(homework.getClazzId());
                homeworkInfoDTO.setContent(homework.getContent());
                homeworkInfoDTO.setId(homework.getId());
                homeworkInfoDTO.setTitle(homework.getTitle());
                homeworkInfoDTO.setStartAt(homework.getStartAt());
                homeworkInfoDTO.setEndAt(homework.getEndAt());
                if (homework.getEndAt().before(date))homeworkInfoDTO.setStatus("已结束");
                else homeworkInfoDTO.setStatus("进行中");
                if (homework.getStartAt().after(date))homeworkInfoDTO.setStatus("未发布");
                list.add(homeworkInfoDTO);
            }
        }
        PageInfo<HomeworkInfoDTO> pageInfo = new PageInfo<>(list,5);
        return new ResponseVO("200", "success", pageInfo);
    }

    //课程作业，教师，按班级查
    @RequiresRoles("teacher")
    @RequestMapping("/teacher/homework/all")
    @ResponseBody
    public ResponseVO myClassHomework(@RequestParam("clazzId")  int clazzId,
                                      @RequestParam(value = "pn",defaultValue = "1") int pn) {
        PageHelper.startPage(pn,5);
        List<HomeworkInfoDTO> list = new ArrayList<>();
        Date date = new Date();
        for (Homework homework:homeworkService.findListByClazzId(clazzId)){
            HomeworkInfoDTO homeworkInfoDTO = new HomeworkInfoDTO();
            homeworkInfoDTO.setClazzId(homework.getClazzId());
            homeworkInfoDTO.setContent(homework.getContent());
            homeworkInfoDTO.setId(homework.getId());
            homeworkInfoDTO.setTitle(homework.getTitle());
            homeworkInfoDTO.setStartAt(homework.getStartAt());
            homeworkInfoDTO.setEndAt(homework.getEndAt());
            if (homework.getEndAt().before(date))homeworkInfoDTO.setStatus("已结束");
            else homeworkInfoDTO.setStatus("进行中");
            if (homework.getStartAt().after(date))homeworkInfoDTO.setStatus("未发布");
            list.add(homeworkInfoDTO);
        }
        PageInfo<HomeworkInfoDTO> pageInfo = new PageInfo<>(list,5);
        return new ResponseVO("200", "success", pageInfo);
    }

    //课程作业，教师，发布新作业
    @RequiresRoles("teacher")
    @RequestMapping("/teacher/homework/add")
    @ResponseBody
    public ResponseVO addHomework(@RequestBody Homework requestHomework){
        if (requestHomework.getTitle().length()>30)return new ResponseVO("200","标题过长");
        if (requestHomework.getContent().length()>1000)return new ResponseVO("200","内容过长");
        if (requestHomework.getTitle().isEmpty())return new ResponseVO("200","标题不能为空");
        if (requestHomework.getContent().isEmpty())return new ResponseVO("200","内容不能为空");
        homeworkService.add(requestHomework);
        requestHomework.setId(requestHomework.getId());
        List<Student> list = homeworkService.findStudentListByClazzId(requestHomework.getClazzId());
        // 加入全班同学结果，设成绩为-2
        HomeworkResult homeworkResultTemp = new HomeworkResult();
        homeworkResultTemp.setHomeworkId(requestHomework.getId());
        for (Student student : list) {
            homeworkResultTemp.setStudentId(student.getId());
            homeworkResultService.add(homeworkResultTemp);
        }
        return new ResponseVO("200","success");
    }

    //课程作业，教师，修改作业
    @RequiresRoles("teacher")
    @RequestMapping("/teacher/homework/update")
    @ResponseBody
    public ResponseVO updateHomework(@RequestBody Homework requestHomework){
        if (requestHomework.getTitle().length()>30)return new ResponseVO("200","标题过长");
        if (requestHomework.getContent().length()>1000)return new ResponseVO("200","内容过长");
        if (requestHomework.getTitle().isEmpty())return new ResponseVO("200","标题不能为空");
        if (requestHomework.getContent().isEmpty())return new ResponseVO("200","内容不能为空");
        homeworkService.modify(requestHomework);
        return new ResponseVO("200","success");
    }

    //课程作业，教师学生，按id查
    @RequiresRoles(value={"student","teacher"}, logical = Logical.OR)
    @RequestMapping("/homework/search")
    @ResponseBody
    public ResponseVO searchById(@RequestBody Homework requestHomework){
        int id = requestHomework.getId();
        Homework homework = homeworkService.findById(id);
        return new ResponseVO("200","success",homework);
    }

    //课程作业，教师，删除
    @RequiresRoles("teacher")
    @RequestMapping("/teacher/homework/delete")
    @ResponseBody
    public ResponseVO delete(@RequestBody Homework requestHomework){
        int id = requestHomework.getId();
        int rel = homeworkService.remove(id);
        return new ResponseVO("200","success");
    }

    //课程作业，学生，按班级查,测试用
    @RequiresRoles("student")
    @RequestMapping("/student/homework/real_all")
    @ResponseBody
    public ResponseVO classHomework(@RequestParam("clazzId") int clazzId,
                                    @RequestParam(value = "pn",defaultValue = "1") int pn) {
        PageHelper.startPage(pn,5);
        List<Homework> list = homeworkService.findListByClazzId(clazzId);
        PageInfo<Homework> pageInfo = new PageInfo<>(list,5);
        return new ResponseVO("200", "success", pageInfo);
    }

    //课程作业，学生，按班级查，开始时间要在当前时间之后
    @RequiresRoles("student")
    @RequestMapping("/student/homework/all")
    @ResponseBody
    public ResponseVO classHomeworkNow(@RequestParam("clazzId") int clazzId,
                                       @RequestParam("studentId") int studentId,
                                       @RequestParam(value = "pn",defaultValue = "1") int pn) {
        PageHelper.startPage(pn,5);
        List<HomeworkInfoDTO> list = new ArrayList<>();
        Date date = new Date();
        HomeworkResult homeworkResult = new HomeworkResult();
        homeworkResult.setStudentId(studentId);
        for (Homework homework:homeworkService.findListByClazzId(clazzId)){
            if (homework.getStartAt().before(date)){
                HomeworkInfoDTO homeworkInfoDTO = new HomeworkInfoDTO();
                homeworkInfoDTO.setClazzId(homework.getClazzId());
                homeworkInfoDTO.setContent(homework.getContent());
                homeworkInfoDTO.setId(homework.getId());
                homeworkResult.setHomeworkId(homework.getId());
                Integer score = homeworkResultService.findResultStatus(homeworkResult);
                if (score == null)score = -2;
                homeworkInfoDTO.setScore(score);
                homeworkInfoDTO.setTitle(homework.getTitle());
                homeworkInfoDTO.setStartAt(homework.getStartAt());
                homeworkInfoDTO.setEndAt(homework.getEndAt());
                if (homework.getEndAt().before(date))homeworkInfoDTO.setStatus("已结束");
                else homeworkInfoDTO.setStatus("进行中");
                list.add(homeworkInfoDTO);
            }
        }
        PageInfo<HomeworkInfoDTO> pageInfo = new PageInfo<>(list,5);
        return new ResponseVO("200", "success", pageInfo);
    }

}
