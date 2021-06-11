package team.ljm.secw.utils;

import team.ljm.secw.dto.StudentScoreDTO;

import java.util.HashMap;
import java.util.List;

public class ScoreSortUtil {

    private int A = 0;
    private int B = 0;
    private int C = 0;
    private int D = 0;
    private int E = 0;
    private int F = 0;
    private int G = 0;
    private int H = 0;
    private int I = 0;

    public StudentScoreDTO sortScore(List<Integer> scoreList) {
        for (Integer score:scoreList) {
            if (score >= 0 && score < 60) {
                G++;
            } else if (score >= 60 && score < 70) {
                F++;
            } else if (score >= 70 && score < 80) {
                E++;
            } else if (score >= 80 && score < 85) {
                D++;
            } else if (score >= 85 && score < 90) {
                C++;
            } else if (score >= 90 && score < 100) {
                B++;
            } else if (score == 100) {
                A++;
            } else if (score == -2) {
                H++;
            } else if (score == -1) {
                I++;
            }
        }
        return new StudentScoreDTO(A, B, C, D, E, F, G, H, I);
    }
}
