package team.ljm.secw.service;

import team.ljm.secw.dto.VerificationDTO;

public interface IChangePwdService {

    String findStudentEmailByAccount(String account);

    String findTeacherEmailByAccount(String account);

    int modifyStudentPassword(VerificationDTO verificationDTO);

    int modifyTeacherPassword(VerificationDTO verificationDTO);

}
