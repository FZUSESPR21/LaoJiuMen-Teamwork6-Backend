package team.ljm.secw.mapper;

import team.ljm.secw.entity.Teacher;

public interface HomePageMapper {

    String selectTeacherInformation(int clazzId);

    int updateTeacherInformation(Teacher teacher);

}
