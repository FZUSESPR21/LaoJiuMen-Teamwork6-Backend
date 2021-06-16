package team.ljm.secw.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import team.ljm.secw.dto.AttendanceDTO;
import team.ljm.secw.entity.AttendanceResult;
import team.ljm.secw.entity.Student;
import team.ljm.secw.service.IAttendanceService;
import team.ljm.secw.vo.ResponseVO;


/**
 * /all?clazzId= &studentId=,学生签到界面
 * /insert,不要管
 * /stuUpdate?attendeAt=,插入学生签到时间,需要传一个时间过来
 * /teacherAll?clazzId=,教师的签到首页
 * /stuList?attendanceId=,点击一个签到进去,获得学生的签到情况
 * /release?attendanceName=  &endAt(结束时间) &issuer=  &clazzId=,教师发布签到
 * /updateTime?id=   &endAt=   ,教师更改签到时间
 */

@Controller
public class AttendanceController {

    @Autowired
    private IAttendanceService attendanceService;

    @RequiresRoles("student")
    @RequestMapping("/attendance/all")
    @ResponseBody
    public ResponseVO getAll(@RequestBody Student student){
        return new ResponseVO("200","", attendanceService.findAllAttendance(student));
    }

    @RequiresRoles("teacher")
    @RequestMapping("/attendance/insert")
    @ResponseBody
    public ResponseVO insert(@RequestBody AttendanceResult attendanceResult){
        return new ResponseVO("200","", attendanceService.insertStuAttendance(attendanceResult));
    }

    @RequiresRoles("teacher")
    @RequestMapping("/attendance/teacherAll")
    @ResponseBody
    public ResponseVO teacherAll(@RequestParam(value = "clazzId") Integer clazzId){
        return new ResponseVO("200","", attendanceService.findTeacherAttendance(clazzId));
    }

    @RequiresRoles("student")
    @RequestMapping("/attendance/stuUpdate")
    @ResponseBody
    public ResponseVO stuUpdate(@RequestBody AttendanceResult attendanceResult){
        return new ResponseVO("200","", attendanceService.updateResult(attendanceResult));
    }

    @RequiresRoles("teacher")
    @RequestMapping("/attendance/stuList")
    @ResponseBody
    public ResponseVO AttendenceList(@RequestBody Integer attendanceId){
        return new ResponseVO("200","", attendanceService.findStuResult(attendanceId));
    }

    @RequiresRoles("teacher")
    @RequestMapping("/attendance/release")
    @ResponseBody
    public ResponseVO release(@RequestBody AttendanceDTO attendance){
        return new ResponseVO("200","", attendanceService.releaseAttendance(attendance));
    }

    @RequiresRoles("teacher")
    @RequestMapping("/attendance/updateTime")
    @ResponseBody
    public ResponseVO updateTime(@RequestBody AttendanceDTO attendance){
        return new ResponseVO("200","", attendanceService.updateEndAt(attendance));
    }
}
