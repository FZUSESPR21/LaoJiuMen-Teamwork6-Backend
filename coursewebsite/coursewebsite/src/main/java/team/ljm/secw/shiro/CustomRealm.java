package team.ljm.secw.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import team.ljm.secw.entity.Student;
import team.ljm.secw.entity.Teacher;
import team.ljm.secw.service.ILoginService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    @Resource
    ILoginService loginService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("CustomRealm: doGetAuthorizationInfo");
        String account = principalCollection.getPrimaryPrincipal().toString();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> roles = new HashSet<>();
        if (!account.startsWith("admin")) {
            roles.add("student");
        } else {
            roles.add("teacher");
        }
        //Set<String> permissions = userService.findPermissions(username);
        info.setRoles(roles);
        //info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("CustomRealm: doGetAuthenticationInfo");
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String account = usernamePasswordToken.getUsername();
        if (!account.startsWith("admin")) {
            Student student = loginService.findStudentByAccount(account);
            if(student == null) {
                throw new AuthenticationException("用户名或者密码错误");
            }
            return new SimpleAuthenticationInfo(student.getStudentName(), student.getPwd(), "CustomRealm");
        } else {
            Teacher teacher = loginService.findTeacherByAccount(account);
            if(teacher == null) {
                throw new AuthenticationException("用户名或者密码错误");
            }
            return new SimpleAuthenticationInfo(teacher.getTeacherName(), teacher.getPwd(), "CustomRealm");
        }
    }
}
