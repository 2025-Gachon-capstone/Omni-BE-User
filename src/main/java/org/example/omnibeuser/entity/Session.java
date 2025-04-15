package org.example.omnibeuser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.omnibeuser.entity.base.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Session")
public class Session extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String refresh;

    @Column(nullable = false)
    private LocalDateTime expired;

    @Version
    private Long version;
}
