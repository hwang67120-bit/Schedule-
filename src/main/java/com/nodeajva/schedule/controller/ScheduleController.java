package com.nodeajva.schedule.controller;

import com.nodeajva.schedule.dto.ScheduleRequest;
import com.nodeajva.schedule.dto.ScheduleResponse;
import com.nodeajva.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;



    // 등록
    @PostMapping
    public ScheduleResponse save(@RequestBody ScheduleRequest request) {
        return scheduleService.save(request);
    }

    // 전체조회
    @GetMapping
    public List<ScheduleResponse> findAll(@RequestParam(required = false) String name) {
        return scheduleService.findAll(name);
    }

    // 단건조회
    @GetMapping("/{id}")
    public ScheduleResponse findById(@PathVariable Long id) {
        return scheduleService.findById(id);
    }

    // 수정
    @PutMapping("/{id}")
    public ScheduleResponse update(
            @PathVariable Long id,
            @RequestParam String password,
            @RequestBody ScheduleRequest request) {
        return scheduleService.update(id, password, request);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String password) {
        scheduleService.delete(id, password);
    }



    // 검색
    @GetMapping("/search")
    public List<ScheduleResponse> search(@RequestParam String keyword) {
        return scheduleService.search(keyword);
    }

    // 최신 글 조회
    @GetMapping("/recent")
    public List<ScheduleResponse> recent(@RequestParam(defaultValue = "5") int limit) {
        return scheduleService.findRecent(limit);
    }

    // 최근 일정 1개
    @GetMapping("/latest")
    public ScheduleResponse latest() {
        return scheduleService.findLatest();
    }

    // 작성자 일정 존재 확인
    @GetMapping("/has-schedule")
    public boolean hasSchedule(@RequestParam String name) {
        return scheduleService.hasSchedule(name);
    }

    // ==================== 통계 기능 ====================

    // 작성자별 개수
    @GetMapping("/count-by-name")
    public Map<String, Long> countByName() {
        return scheduleService.countByName();
    }

    // 일별 개수
    @GetMapping("/count-by-date")
    public Map<LocalDate, Long> countByDate() {
        return scheduleService.countByDate();
    }

    // 수정일별 개수
    @GetMapping("/count-by-update")
    public Map<LocalDate, Long> countByUpdateDate() {
        return scheduleService.countByUpdateDate();
    }
}