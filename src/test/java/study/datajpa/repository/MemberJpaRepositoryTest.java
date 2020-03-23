package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;


    @Test
    public void testMember () {
        // given
        Member member = new Member("memberA");
        // when
        Member savedMember = memberJpaRepository.save(member);
        // then
        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
    }

    @Test
    public void basicCRUD () {
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        long count = memberJpaRepository.count();
        assertEquals(count, 2);
    }

    @Test
    public void findByUsernameAndAgeGreaterThanTest () {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        // when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("memberA", 10);
        // then
        assertEquals(result.get(0).getUsername(), "memberA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }
}