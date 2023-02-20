package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "checks")
public class Check implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "checks_id_generator"
    )
    @SequenceGenerator(
            name = "checks_id_generator",
            sequenceName = "checks_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "peer", referencedColumnName = "nickname", nullable = false)
    private Peer peer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Task", referencedColumnName = "title", nullable = false)
    private Task task;

    @Column(name = "date")
    private LocalDate date;
}
