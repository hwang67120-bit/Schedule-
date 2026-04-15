package com.nodeajva.schedule.dto;

import com.nodeajva.schedule.entity.ScheduleEntity;
import lombok.Builder;
;

import java.time.LocalDateTime;




@Builder
public record ScheduleResponse(

     Long id,
     String title,
     String content,
     String name,
     LocalDateTime createAt,
     LocalDateTime updateAt ) {

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
