package org.example.omnibeuser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.omnibeuser.entity.base.BaseEntity;
import org.example.omnibeuser.entity.type.MemberStatus;
import org.example.omnibeuser.entity.type.Role;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Version
    private int version;

}
