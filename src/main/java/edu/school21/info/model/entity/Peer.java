package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "peers")
public class Peer implements BaseModel {

    @Id
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
}
