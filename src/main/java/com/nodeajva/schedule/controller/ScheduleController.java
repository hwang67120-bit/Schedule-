package com.nodeajva.schedule.controller;

import com.nodeajva.schedule.dto.ScheduleRequest;
import com.nodeajva.schedule.dto.ScheduleResponse;
import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //등록
    @PostMapping
    public ScheduleResponse save(@RequestBody ScheduleRequest request) {
        ScheduleEntity entity = request.toEntity();
        ScheduleEntity saved = scheduleService.save(entity);
        return ScheduleResponse.from(saved);
    }

    //전체조회
    @GetMapping
    public List<ScheduleResponse> findAll(
            @RequestParam(required = false) String name
    ) {
        return scheduleService.findAll(name).stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    //단건 조회
    @GetMapping("/{id}")
    public ScheduleResponse findBy(@PathVariable Long id) {
        ScheduleEntity entity = scheduleService.findById(id);

        return ScheduleResponse.from(entity);
    }

    //수정
    @PutMapping("/{id}")
    public ScheduleResponse update(
            @PathVariable Long id,
            @RequestParam String password,
            @RequestBody ScheduleRequest request
    ) {
        ScheduleEntity entity = request.toEntity();
        ScheduleEntity updated = scheduleService.update(id, password, entity);
        return ScheduleResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String password
    ) {
        scheduleService.delete(id, password);
    }


}
