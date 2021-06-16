package team.ljm.secw.service;

import team.ljm.secw.dto.AttendanceDTO;
import team.ljm.secw.entity.Attendance;
import team.ljm.secw.entity.AttendanceResult;
import team.ljm.secw.entity.Student;
import team.ljm.secw.entity.StudentVo;

import java.util.List;

public interface IAttendanceService {
    List<AttendanceDTO> findAllAttendance(Student student);
    int insertStuAttendance(AttendanceResult attendanceResult);
    int updateResult(AttendanceResult attendanceResult);
    List<AttendanceDTO> findTeacherAttendance(Integer clazzId);
    List<AttendanceResult> findStuResult(Integer attendanceId);
    int releaseAttendance(AttendanceDTO attendance);
    int updateEndAt(AttendanceDTO attendance);
}
