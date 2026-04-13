package com.nodeajva.schedule.dto;

import com.nodeajva.schedule.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {

    private Long id;
    private String title;
    private String content;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static ScheduleResponse from(ScheduleEntity entity) {
        return ScheduleResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .name(entity.getName())
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }


}
