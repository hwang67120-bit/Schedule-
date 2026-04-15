package com.nodeajva.schedule.service;

import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    //저장
    public ScheduleEntity save(ScheduleEntity schedule){
        return scheduleRepository.save(schedule);
    }

    //전체 조회
    public List<ScheduleEntity> findAll(String name) {
        return Optional.ofNullable(name) // nane을 Optional로 감싸기 // name이 null이면 → Optional.empty() //name이 있으면 → Optional.of(name)

                .filter(n -> !n.isEmpty()) // 비어있지 않은 것만 통과 // 비어있으면 → Optional.empty()

                .map(scheduleRepository::findByNameOrderByUpdateAtDesc) // name으로 조회 // Optional<List<ScheduleEntity>> 반환

                .orElseGet(scheduleRepository::findAllByOrderByUpdateAtDesc);// Optional이 비어있으면 (name이 null이거나 empty)
                                                                                // → 전체 조회
    }
    //단건 조회
    public ScheduleEntity findById(Long id){
        return scheduleRepository.findByOrThrow(id);
    }

    //수정
    public ScheduleEntity update(Long id, String password, ScheduleEntity newSchedule){

        //기존 데이터 조회
        ScheduleEntity schedules = scheduleRepository.findByOrThrow(id);

        //비밀번호 확인
        if (!schedules.getPassword().equals(password)){
            throw new RuntimeException("비밀번호 불일치");
        }

        //수정
        schedules.setTitle(newSchedule.getTitle());
        schedules.setName(newSchedule.getName());

        //저장
        return scheduleRepository.save(schedules);
    }

    //삭제
    public void delete(Long id, String password){

        //기존 데이터 조회
        ScheduleEntity schedule = scheduleRepository.findByOrThrow(id);

        if (!schedule.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        //삭제
        scheduleRepository.delete(schedule);
    }
}
