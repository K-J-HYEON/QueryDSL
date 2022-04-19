package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
// Commit을 하면 다음 테스트할때 데이터가 계속 남아있다.
//@Commit
class MemberTest {


    //PersistenceContext로 해도되고
    //    @PersistenceContext
    //최신 스프링에선 Autowired해도된다.
    @Autowired
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        // 영속성 컨텍스트에 있는 오브젝트들을 커내 쿼리를 만들어 db에 쏙쏙 넣는다.
        em.flush();
        // 영속성 컨텍스트 초기 => 캐시 날라간다!
        em.clear();

        // 확인
        // JPQL 짜서 쿼리 생성
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team" + member.getTeam());
        }


    }
}