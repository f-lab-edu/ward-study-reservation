package com.dsg.wardstudy.domain.studyGroup.entity;

import com.dsg.wardstudy.domain.BaseTimeEntity;
import com.dsg.wardstudy.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "likes")
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    @Setter
    private StudyGroup studyGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @Builder
    public Like(Long id, User user, StudyGroup studyGroup) {
        this.id = id;
        this.user = user;
        this.studyGroup = studyGroup;
    }

    public static Like of(User user, StudyGroup studyGroup) {
        return Like.builder()
                .user(user)
                .studyGroup(studyGroup)
                .build();
    }

}
