package team.ljm.secw.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ljm.secw.entity.Homework;
import team.ljm.secw.entity.HomeworkResult;
import team.ljm.secw.entity.Student;
import team.ljm.secw.mapper.HomeworkMapper;
import team.ljm.secw.mapper.HomeworkresultMapper;
import team.ljm.secw.service.IHomeworkService;
import team.ljm.secw.utils.ContentCleanUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service("HomeworkService")
public class HomeworkServiceImpl implements IHomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkresultMapper homeworkresultMapper;

    @Override
    public int add(Homework homework) {
        homework.setTitle(ContentCleanUtil.clean(homework.getTitle()));
        homework.setContent(ContentCleanUtil.clean(homework.getContent()));
        return homeworkMapper.insert(homework);
    }

    @Override
    public List<Homework> findAll() {
        List<Homework> list = homeworkMapper.selectList();
        return list;
    }

    @Override
    public List<Student> findStudentListByClazzId(int id) {
        List<Student> list = homeworkMapper.selectStudentListByClazzId(id);
        return list;
    }

    @Override
    public List<Homework> findListByClazzId(int id) {
        List<Homework> list = homeworkMapper.selectListByClazzId(id);
        return list;
    }

    @Override
    public Homework findById(int id) {
        return homeworkMapper.selectById(id);
    }

    @Override
    public int modify(Homework homework) {
        homework.setTitle(ContentCleanUtil.clean(homework.getTitle()));
        homework.setContent(ContentCleanUtil.clean(homework.getContent()));
        return homeworkMapper.update(homework);
    }

    @Override
    public int remove(int id) {
        List<HomeworkResult> list = homeworkresultMapper.selectListById(id);
        for(HomeworkResult homeworkResult : list){
            String url = homeworkResult.getFilePath();
            if (!url.isEmpty()){
            Path path = Paths.get(url);
            try {
                //删除原附件
                Files.delete(path);
            } catch (Exception e) {
                e.printStackTrace();
            }}
            homeworkresultMapper.delete(homeworkResult.getId());
        }
        return homeworkMapper.deleteById(id);
    }
}
