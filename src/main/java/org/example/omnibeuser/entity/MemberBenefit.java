package org.example.omnibeuser.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.omnibeuser.entity.base.BaseEntity;
import org.example.omnibeuser.entity.type.MemberBenefitStatus;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MemberBenefit")
public class MemberBenefit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberBenefitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long benefitId;

    @Enumerated(EnumType.STRING)
    private MemberBenefitStatus status;

    @Version
    private Long version;
}
