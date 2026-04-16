package com.nodeajva.schedule.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.ErrorResponse;


import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@EntityListeners(AllArgsConstructor.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false, length = 50)
    private String name;


    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;




    @PrePersist
    protected void createdAt(){
        createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }


    public void update(String title, String content, String name) {
        this.title = title;
        this.content = content;
        this.name = name;
    }
}
