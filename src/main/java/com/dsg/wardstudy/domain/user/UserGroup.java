package com.dsg.wardstudy.domain.user;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.BaseTimeEntity;
import com.dsg.wardstudy.type.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_group")
@ToString(of = {"id", "userType"})
public class UserGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;


    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder
    public UserGroup(Long id, User user, StudyGroup studyGroup, UserType userType) {
        this.id = id;
        this.user = user;
        this.studyGroup = studyGroup;
        this.userType = userType;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
