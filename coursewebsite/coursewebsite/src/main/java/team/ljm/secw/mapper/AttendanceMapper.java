package team.ljm.secw.mapper;

import team.ljm.secw.dto.AttendanceDTO;
import team.ljm.secw.entity.Attendance;
import team.ljm.secw.entity.AttendanceResult;
import team.ljm.secw.entity.Student;
import team.ljm.secw.entity.StudentVo;

import java.util.List;

public interface AttendanceMapper {
    List<AttendanceDTO> findAllAttendance(Integer clazzId);
    int insertStuAttendance(AttendanceResult attendanceResult);
    List<AttendanceResult> findResult(Integer studentid);
    int updateResult(AttendanceResult attendanceResult);
    List<AttendanceResult> findStuResult(Integer attendanceId);
    List<Student> findAllStu(Integer clazzId);
    int insertAttendance(AttendanceDTO attendance);
    int updateEndAt(AttendanceDTO attendance);
}
