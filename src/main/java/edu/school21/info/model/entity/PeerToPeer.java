package edu.school21.info.model.entity;

import edu.school21.info.model.BaseModel;
import edu.school21.info.model.Status;
import edu.school21.info.util.StatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "p2p")
public class PeerToPeer implements BaseModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "p2p_id_generator"
    )
    @SequenceGenerator(
            name = "p2p_id_generator",
            sequenceName = "p2p_id_seq",
            allocationSize = 1
    )
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "check_id", referencedColumnName = "id", nullable = false)
    private Check check;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "checking_peer", referencedColumnName = "nickname", nullable = false)
    private Peer checkingPeer;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status status;

    @Column(name = "time")
    private LocalTime time;
}
