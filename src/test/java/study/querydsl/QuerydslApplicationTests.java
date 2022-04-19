package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Commit
class QuerydslApplicationTests {


	// spring 최신버전
	@Autowired
	EntityManager em;
	// java 표준스펙
//	@PersistenceContext
	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);
		QHello qHello = QHello.hello;


		// querydsl도 잘돌아간다.
		Hello result = query
				.selectFrom(qHello)
				.fetchOne();

		assertThat(result).isEqualTo(hello);

		// lombok으로 세팅
		assertThat(result.getId()).isEqualTo(hello.getId());
	}

}
