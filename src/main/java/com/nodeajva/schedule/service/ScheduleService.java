package com.nodeajva.schedule.service;

import com.nodeajva.schedule.dto.ScheduleRequest;
import com.nodeajva.schedule.dto.ScheduleResponse;
import com.nodeajva.schedule.entity.ScheduleEntity;
import com.nodeajva.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;



    // 저장
    public ScheduleResponse save(ScheduleRequest request) {
        ScheduleEntity entity = request.toEntity();
        ScheduleEntity saved = scheduleRepository.save(entity);
        return ScheduleResponse.from(saved);
    }

    // 전체 조회
    public List<ScheduleResponse> findAll(String name) {
        List<ScheduleEntity> entities = Optional.ofNullable(name)
                .filter(n -> !n.isEmpty())
                .map(scheduleRepository::findByNameOrderByUpdateAtDesc)
                .orElseGet(scheduleRepository::findAllByOrderByUpdateAtDesc);

        return entities.stream()
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 단건 조회
    public ScheduleResponse findById(Long id) {
        ScheduleEntity entity = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));
        return ScheduleResponse.from(entity);
    }

    // 수정
    public ScheduleResponse update(Long id, String password, ScheduleRequest request) {
        // 1) 조회
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));

        // 2) 비밀번호 확인
        if (!schedule.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        // 3) 수정
        request.applyTo(schedule);

        // 4) 저장
        ScheduleEntity updated = scheduleRepository.save(schedule);
        return ScheduleResponse.from(updated);
    }

    // 삭제
    public void delete(Long id, String password) {
        // 1) 조회
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다"));

        // 2) 비밀번호 확인
        if (!schedule.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호 불일치");
        }

        // 3) 삭제
        scheduleRepository.delete(schedule);
    }



    // 검색
    public List<ScheduleResponse> search(String keyword) {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .filter(s -> s.getTitle().contains(keyword) ||
                        s.getContent().contains(keyword))
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 최신 글 조회
    public List<ScheduleResponse> findRecent(int limit) {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .sorted(Comparator.comparing(ScheduleEntity::getCreateAt).reversed())
                .limit(limit)
                .map(ScheduleResponse::from)
                .collect(Collectors.toList());
    }

    // 최근 일정 1개
    public ScheduleResponse findLatest() {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .sorted(Comparator.comparing(ScheduleEntity::getCreateAt).reversed())
                .findFirst()
                .map(ScheduleResponse::from)
                .orElseThrow(() -> new RuntimeException("일정 없음"));
    }

    // 작성자 일정 존재 확인
    public boolean hasSchedule(String name) {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .anyMatch(s -> s.getName().equals(name));
    }



    // 작성자별 개수
    public Map<String, Long> countByName() {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .collect(Collectors.groupingBy(
                        ScheduleEntity::getName,
                        Collectors.counting()
                ));
    }

    // 일별 개수
    public Map<LocalDate, Long> countByDate() {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCreateAt().toLocalDate(),
                        Collectors.counting()
                ));
    }

    // 수정일별 개수
    public Map<LocalDate, Long> countByUpdateDate() {
        List<ScheduleEntity> schedules = scheduleRepository.findAllByOrderByUpdateAtDesc();

        return schedules.stream()
                .filter(s -> s.getUpdateAt() != null)
                .collect(Collectors.groupingBy(
                        s -> s.getUpdateAt().toLocalDate(),
                        Collectors.counting()
                ));
    }
}