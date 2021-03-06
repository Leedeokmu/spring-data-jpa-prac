package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;


    @Test
    public void testEntity () {
        // given
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 20, teamB);
        Member member3 = new Member("member1", 30, teamA);
        Member member4 = new Member("member1", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // when
        em.flush();
        em.clear();

        // then
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }
    
    @Test
    public void  jpaEventBaseEntityTest () throws InterruptedException {
        // given

        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        // when
        em.flush();
        em.clear();
        Member findMember = memberRepository.findById(member.getId()).get();
        // then
        System.out.println("findMember = " + member.getCreatedDate());
        System.out.println("findMember = " + member.getLastModifiedDate());
        System.out.println("findMember = " + member.getCreatedBy());
        System.out.println("findMember = " + member.getLastModifiedBy());
    }


}