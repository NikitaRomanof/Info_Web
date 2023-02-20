package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "tasks")
public class Task implements BaseModel {

    @Id
    @Column(name = "title")
    private String title;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_task", referencedColumnName = "title")
    private Task parentTask;

    @Column(name = "max_xp", nullable = false)
    private Long maxExperience;

    public boolean isFirstTask() {
        return parentTask == null;
    }
}
