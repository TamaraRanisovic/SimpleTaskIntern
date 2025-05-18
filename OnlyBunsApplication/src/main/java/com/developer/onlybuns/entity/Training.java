package com.developer.onlybuns.entity;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="training")
public class Training {
    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;


    @Column(name="duration", unique=false, nullable=false)
    private Integer duration;

    @Column(name="startTime", unique=false, nullable=false)
    private LocalDateTime startTime;


    @ManyToMany(mappedBy = "trainings")
    private Set<RegisteredUser> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    public Training() {
    }

    public Training(Integer id, Integer duration, LocalDateTime startTime) {
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
