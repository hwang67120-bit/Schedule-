package com.nodeajva.schedule.controller;

import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //등록
    @PostMapping
    public ScheduleEntity save (@RequestBody ScheduleEntity schedule) {
        return scheduleService.save(schedule);
    }

    //전체조회
    @GetMapping
    public List<ScheduleEntity> findAll(
            @RequestParam(required = false) String name
    ) {
        return scheduleService.findAll(name);
    }

    //단건 조회
    @GetMapping("/{id}")
    public ScheduleEntity findBy(@PathVariable Long id) {
        return scheduleService.findById(id);
    }

    //수정
    @PutMapping("/{id}")
    public ScheduleEntity update(
            @PathVariable Long id,
            @RequestParam String password,
            @RequestBody ScheduleEntity schedule
    ){
        return scheduleService.update(id, password, schedule);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String password
    ) {
        scheduleService.delete(id, password);
    }


}
