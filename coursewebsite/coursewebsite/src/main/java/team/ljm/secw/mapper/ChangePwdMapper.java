package team.ljm.secw.mapper;

import team.ljm.secw.dto.VerificationDTO;

public interface ChangePwdMapper {

    String selectStudentEmailByAccount(String account);

    String selectTeacherEmailByAccount(String account);

    int updateStudentPassword(VerificationDTO verificationDTO);

    int updateTeacherPassword(VerificationDTO verificationDTO);
}
