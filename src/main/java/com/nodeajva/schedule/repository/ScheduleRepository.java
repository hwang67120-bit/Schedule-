package com.nodeajva.schedule.repository;

import com.nodeajva.schedule.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findAllByOrderByUpdateAtDesc();

    List<ScheduleEntity> findByNameOrderByUpdateAtDesc(String name);

    default ScheduleEntity findByOrThrow(Long id){
        return this.findById(id)
                .orElseThrow(()-> new RuntimeException("일정 없음"));
    }
}
