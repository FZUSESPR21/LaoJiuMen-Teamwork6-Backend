package team.ljm.secw.controller;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ljm.secw.entity.Teacher;
import team.ljm.secw.service.IHomePageService;
import team.ljm.secw.vo.ResponseVO;

@Controller
public class HomePageController {

    @Autowired
    IHomePageService homePageService;

    @RequiresRoles(value={"student","teacher"}, logical = Logical.OR)
    @RequestMapping(value = "/tch_info/show", method = RequestMethod.GET)
    @ResponseBody
    public ResponseVO showTeacherInformation(@RequestParam(value = "clazzId") Integer clazzId) {
        return new ResponseVO("200", "ok", homePageService.findTeacherInformation(clazzId));
    }

    @RequiresRoles("teacher")
    @RequestMapping(value = "/tch_info/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO changeTeacherInformation(@RequestBody Teacher teacher) {
        return new ResponseVO("200", "ok", homePageService.modifyTeacherInformation(teacher));
    }

}
