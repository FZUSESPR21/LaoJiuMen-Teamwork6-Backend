package team.ljm.secw.controller;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ljm.secw.dto.LoginDTO;
import team.ljm.secw.dto.TeacherDTO;
import team.ljm.secw.entity.Student;
import team.ljm.secw.entity.Teacher;
import team.ljm.secw.service.IClazzService;
import team.ljm.secw.service.ILoginService;
import team.ljm.secw.shiro.JWTFilter;
import team.ljm.secw.shiro.JwtUtil;
import team.ljm.secw.shiro.MyRealm;
import team.ljm.secw.vo.ResponseVO;

import java.security.Principal;

//@CrossOrigin
@Controller
public class LoginController {

    @Autowired
    ILoginService loginService;

    @Autowired
    IClazzService clazzService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO login(@RequestBody LoginDTO loginDTO) {
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken token = new UsernamePasswordToken(loginDTO.getAccount(), loginDTO.getPassword());
//        try {
//            subject.login(token);
//            String jwtToken = JwtUtil.sign(loginDTO.getType(), loginDTO.getAccount(), loginDTO.getPassword());
//            return new ResponseVO("200", jwtToken);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseVO("401", "账号或密码错误！");
//        }
        if (StrUtil.isBlank(loginDTO.getAccount()) || StrUtil.isBlank(loginDTO.getPassword())) {
            return new ResponseVO("403", "账号或密码不能为空");
        }
        try {
            if (loginDTO.getType().equals("0")) {
                Student student = loginService.findStudentByAccount(loginDTO.getAccount());
                if (student != null && loginDTO.getPassword().equals(student.getPwd())) {
                    Student s = new Student();
                    s.setId(student.getId());
                    s.setAccount(student.getAccount());
                    s.setStudentName(student.getStudentName());
                    s.setClazzId(student.getClazzId());
                    return new ResponseVO("200", JwtUtil.sign(loginDTO.getType(), loginDTO.getAccount(), student.getPwd()), s);
                } else {
                    return new ResponseVO("403", "账号或密码错误！");
                }
            } else {
                Teacher teacher = loginService.findTeacherByAccount(loginDTO.getAccount());
                if (teacher != null && loginDTO.getPassword().equals(teacher.getPwd())) {
                    TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(), teacher.getAccount(), teacher.getTeacherName(),
                            clazzService.findClazzNameListByTeacherId(teacher.getId()));
                    return new ResponseVO("200", JwtUtil.sign(loginDTO.getType(), loginDTO.getAccount(), teacher.getPwd()), teacherDTO);
                } else {
                    return new ResponseVO("403", "账号或密码错误！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseVO("403", e.getMessage());
        }
    }

    @RequestMapping(value = "/401")
    @ResponseBody
    public ResponseVO unauthorized() {
        return new ResponseVO("401", "您暂无此权限", null);
    }

}
