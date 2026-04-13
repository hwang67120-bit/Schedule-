package com.nodeajva.schedule.service;

import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    //저장
    public ScheduleEntity save(ScheduleEntity schedule){
        return scheduleRepository.save(schedule);
    }

    //전체 조회
    public List<ScheduleEntity> findAll(){
        return scheduleRepository.findAll();
    }

    //단건 조회
    public ScheduleEntity findById(Long id){
        return scheduleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("일정 없음"));
    }

    //수정
    public ScheduleEntity update(Long id, String password, ScheduleEntity newSchedule){

        //기존 데이터 조회
        ScheduleEntity schedules = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정 없음"));

        //비밀번호 확인
        if (!schedules.getPassword().equals(password)){
            throw new RuntimeException("비밀번호 불일치");
        }

        //수정
        schedules.setTitle(newSchedule.getTitle());
        schedules.setContent(newSchedule.getContent());

        //저장
        return scheduleRepository.save(schedules);
    }

    //삭제
    public void delete(Long id, String password){

        //기존 데이터 조회
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정 없음"));

        if (!schedule.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        //삭제
        scheduleRepository.delete(schedule);
    }
}
