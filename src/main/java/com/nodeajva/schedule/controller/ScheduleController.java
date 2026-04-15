package com.nodeajva.schedule.controller;

import com.nodeajva.schedule.dto.ScheduleRequest;
import com.nodeajva.schedule.dto.ScheduleResponse;
import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.ls.LSException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 특정 글 조회
    @GetMapping("/search")
    public List<ScheduleResponse> search(@RequestParam String keyword) {
        List<ScheduleEntity>  schedules = scheduleService.findAll(null);

        return schedules.stream()
                .filter(s -> s.getTitle().contains(keyword)||
                        s.getContent().contains(keyword))
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    //최신 글 조회
    @GetMapping("/recent")
    public List<ScheduleResponse> recent (@RequestParam(defaultValue = "5") int limit){

        List<ScheduleEntity> schedules = scheduleService.findAll(null);


        return schedules.stream()
                .sorted(Comparator.comparing(ScheduleEntity::getCreateAt).reversed())
                .limit(limit)
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 작성자별 일정 개수
    @GetMapping("/count-by-name")
    public Map<String, Long> countByName(){
        List<ScheduleEntity> schedules = scheduleService.findAll(null);

        return schedules.stream()
                .peek(c -> System.out.println("💡 확인: " + c.getName()))
                .collect(Collectors.groupingBy(
                        ScheduleEntity::getName,
                        Collectors.counting()
                ));
    }
    //일정 일별 조회
    @GetMapping("/count-by-date")
    public Map<LocalDate, Long> countByDate(){
        List<ScheduleEntity> schedules = scheduleService.findAll(null);

        return schedules.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCreateAt().toLocalDate(),
                        Collectors.counting()
                ));
    }

    //수정일별 일정 개수
    @GetMapping("/count-by-update")
    public Map<LocalDate, Long> countByUpdateDate(){
        List<ScheduleEntity> schedules = scheduleService.findAll(null);

        return schedules.stream()
                .filter(s -> s.getUpdateAt() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getUpdateAt().toLocalDate(),
                        Collectors.counting()
                ));
    }

    // 특정 작성자 일정이 있나 체크
    @GetMapping("/has-schedule")
    public boolean hasSchedule(@RequestParam String name) {
        List<ScheduleEntity> schedules = scheduleService.findAll(null);

        return schedules.stream()
                .anyMatch(s -> s.getName().equals(name));
    }

    // 가장 최근 일정 1개만 조회
    @GetMapping("/latest")
    public ScheduleResponse latest(){
        List<ScheduleEntity> schedules = scheduleService.findAll(null);

        return schedules.stream()
                .sorted(Comparator.comparing(ScheduleEntity::getCreateAt).reversed())
                .findFirst()
                .map(ScheduleResponse::from)
                .orElseThrow(() -> new RuntimeException("일정 없음"));
    }

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
