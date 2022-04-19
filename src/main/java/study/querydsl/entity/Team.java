package study.querydsl.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
// NoArgsConstructor 아래와 같은 기본 생성자를 생성해준다.
// protected Team() {}
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name"})
public class Team {


    // team은 id랑 name만 가지고 있고
    @Id
    @GeneratedValue
    private Long id;
    private String name;


    // 양방향 연관관계이므로
    // mappedBy(거울로 표현)로 연관관계의 주인이 아니고 연관관계의 주인은 Member에 있다.
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
