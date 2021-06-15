package team.ljm.secw.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.ljm.secw.dto.VerificationDTO;
import team.ljm.secw.service.IChangePwdService;
import team.ljm.secw.utils.SendMailUtil;
import team.ljm.secw.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ChangePwdController {

    private static Map<String, String> captchaMap = new HashMap<>();

    @Autowired
    IChangePwdService changePwdService;

    @RequestMapping(value = "/captcha", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO sendCaptchaEmail(@RequestBody VerificationDTO verificationDTO) {
        if (StrUtil.isBlank(verificationDTO.getAccount()) || StrUtil.isBlank(verificationDTO.getPwd())) {
            return new ResponseVO("403", "账号或密码不能为空");
        }
        String captcha = String.valueOf(new Random().nextInt(8999) + 1000);
        if ("0".equals(verificationDTO.getType())) {
            String email = changePwdService.findStudentEmailByAccount(verificationDTO.getAccount());
            return getResponseVO(verificationDTO, captcha, email);
        } else if("1".equals(verificationDTO.getType())){
            String email = changePwdService.findTeacherEmailByAccount(verificationDTO.getAccount());
            return getResponseVO(verificationDTO, captcha, email);
        } else {
            return new ResponseVO("500", "未知错误");
        }
    }

    private ResponseVO getResponseVO(VerificationDTO verificationDTO, String captcha, String email) {
        if (email != null && email.equals(verificationDTO.getEmail())) {
            SendMailUtil.sendMail(email, captcha);
            captchaMap.put(verificationDTO.getAccount(), captcha);
            for (Map.Entry<String, String> entry : captchaMap.entrySet()) {
                System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
            }
            return new ResponseVO("200", "验证码已发送");
        } else {
            return new ResponseVO("403", "账号或邮箱错误");
        }
    }

    @RequestMapping(value = "/chg_pwd", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO changePassword(@RequestBody VerificationDTO verificationDTO, HttpServletRequest request) {
        if (StrUtil.isBlank(verificationDTO.getAccount()) || StrUtil.isBlank(verificationDTO.getPwd())) {
            return new ResponseVO("403", "账号或密码不能为空");
        }
        if (StrUtil.isBlank(verificationDTO.getEmail())) {
            return new ResponseVO("403", "邮箱不能为空");
        }
        if (StrUtil.isBlank(verificationDTO.getCaptcha())) {
            return new ResponseVO("403", "验证码不能为空");
        }
        String regEx1 = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(verificationDTO.getPwd());
        if(!m.matches()) {
            return new ResponseVO("403", "密码格式错误");
        }
        if (captchaMap.get(verificationDTO.getAccount()).equals(verificationDTO.getCaptcha())) {
            if (verificationDTO.getType().equals("0")) {
                String email = changePwdService.findStudentEmailByAccount(verificationDTO.getAccount());
                if (email != null && email.equals(verificationDTO.getEmail())) {
                    changePwdService.modifyStudentPassword(verificationDTO);
                    return new ResponseVO("200", "修改密码成功");
                } else {
                    return new ResponseVO("403", "账号或邮箱错误");
                }
            } else if (verificationDTO.getType().equals("1")) {
                String email = changePwdService.findTeacherEmailByAccount(verificationDTO.getAccount());
                if (email != null && email.equals(verificationDTO.getEmail())) {
                    changePwdService.modifyTeacherPassword(verificationDTO);
                    return new ResponseVO("200", "修改密码成功");
                } else {
                    return new ResponseVO("403", "账号或邮箱错误");
                }
            } else {
                return new ResponseVO("500", "未知错误");
            }
        } else {
            return new ResponseVO("403", "验证码错误");
        }
    }

}
