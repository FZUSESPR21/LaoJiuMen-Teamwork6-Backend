package team.ljm.secw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ljm.secw.entity.Teacher;
import team.ljm.secw.mapper.HomePageMapper;
import team.ljm.secw.service.IHomePageService;
import team.ljm.secw.utils.ContentCleanUtil;

@Service
public class HomePageServiceImpl implements IHomePageService {

    @Autowired
    HomePageMapper homePageMapper;

    @Override
    public String findTeacherInformation(int clazzId) {
        return homePageMapper.selectTeacherInformation(clazzId);
    }

    @Override
    public int modifyTeacherInformation(Teacher teacher) {
        teacher.setInformation(ContentCleanUtil.clean(teacher.getInformation()));
        return homePageMapper.updateTeacherInformation(teacher);
    }
}
