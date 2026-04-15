package com.nodeajva.schedule.dto;

import com.nodeajva.schedule.entity.ScheduleEntity;



public record  ScheduleRequest (

     String title,
     String content,
     String name,
     String password){


    //엔티티 변환
    public ScheduleEntity toEntity() {

        ScheduleEntity entity = new ScheduleEntity();
        entity.setTitle(title);
        entity.setContent(content);
        entity.setName(name);
        entity.setPassword(password);
        return entity;
    }
}


