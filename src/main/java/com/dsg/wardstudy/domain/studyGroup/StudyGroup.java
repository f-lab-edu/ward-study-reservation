package com.dsg.wardstudy.domain.studyGroup;

import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "study_group")
@ToString(of = {"id", "title", "content"})
public class StudyGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_group_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "studyGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserGroup> userGroups = new ArrayList<>();

    @Builder
    public StudyGroup(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }




}
