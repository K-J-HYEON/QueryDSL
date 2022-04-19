package study.querydsl.entity;


import com.querydsl.jpa.impl.JPAQueryFactory;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    // 각 테스트 실행전에 데이터를 세팅을 하고 들어간다.
    @BeforeEach
    public void before() {
        // 필드레벨로 가져가도 괜찮다.
        // spring framework가 주입해주는 엔티티 자체가 멀티스레드에 문제없이 설계가 되어있다.
        // 여러 멀티스레드가 걸랴와도 현재 트랜잭션이 어디에 걸려있는지에 따라서 트랜잭션에 바인딩 되도록 다 분배를 해준다.
        // 즉 동시성 문제가 없다.
        queryFactory = new JPAQueryFactory(em);
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
    }

    // JPQL 먼저 작성
    @Test
    public void startJPQL() {
         // member1을 찾아라.
        // JPQL에서는 parameter 바인딩을 해주고
        String qlString =
                "select m from Member m " +
                "where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // 이렇게 static member를 이용해서 깔끔하게 쓸 수 있다.
        // querydsl은 JPQL의 builder 역할을 하는데 즉 querydsl로 작성한 코드는 JPQL이 된다고 보면된다.
        Member findMember = queryFactory
                .select(member)
                .from(member)
                // 파라미터 바인딩을 안해도 eq를 해서 자동으로 JDBC의 prepare stament로 파라미터로 바인딩을 자동으로 한다.
                .where(member.username.eq("member1")) // 파라미터 바인딩 처리
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
