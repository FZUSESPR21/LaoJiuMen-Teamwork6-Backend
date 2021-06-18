package team.ljm.secw.service;

import team.ljm.secw.entity.Teacher;

public interface IHomePageService {

    String findTeacherInformation(int clazzId);

    int modifyTeacherInformation(Teacher teacher);

}
