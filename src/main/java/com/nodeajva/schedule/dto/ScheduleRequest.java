package com.nodeajva.schedule.dto;

import com.nodeajva.schedule.entity.ScheduleEntity;
import lombok.Builder;


public record  ScheduleRequest (

     String title,
     String content,
     String name,
     String password){


    //엔티티 변환
    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .title(this.title)
                .content(this.content)
                .name(this.name)
                .password(this.password)
                .build();
    }

    public void applyTo(ScheduleEntity entity) {
        entity.update(this.title, this.content , this.name);
    }
}


