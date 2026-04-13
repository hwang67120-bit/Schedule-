package com.nodeajva.schedule.dto;

import com.nodeajva.schedule.entity.ScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class  ScheduleRequest {

    private String title;
    private String content;
    private String name;
    private String password;

    //엔티티 변환
    public ScheduleEntity toEntity(){
        ScheduleEntity entity = new ScheduleEntity();
        entity.setTitle(this.title);
        entity.setContent(this.content);
        entity.setName(this.name);
        entity.setTitle(this.password);
        return entity;
    }

}
