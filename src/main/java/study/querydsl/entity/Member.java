package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
// Setter는 실무에서 가급적 쓰지말자
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

//ToString은 아래를 자동으로 만들어준다.
//@Override
//public String toString() {
//        return "Member{" +
//        "id=" + id +
//        ", username='" + username + '\'' +
//        ", age=" + age +
//        ", team=" + team +
//        '}';
//        }
// "team"이 들어가면 안된다.
@ToString(of = {"id", "username", "age"})
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    // 연관관계(team과의 연관관계)의 주인은 여기 => JPA 완벽히 숙지
    // ManyToOne은 항상 fetch를 lazy로
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    // 생성자를 만들어주는데 설계할 때는 필요한 것만 만들자.
    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }


    // 양방향 연관관계를 이해 무조건
   public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }


    // 양쪽 연관관계
    // teamA에서 B로 바뀌면 team에 연관된 나도 값을 세팅을 해준다.
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
