package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember () {
        // given
        Member member = new Member("memberA");
        // when
        Member savedMember = memberRepository.save(member);
        // then
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);
    }

    @Test
    public void JPA_테스트 () {
        Member member1 = new Member("memberA");
        Member member2 = new Member("memberB");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        List<Member> all = memberRepository.findAll();
        assertEquals(all.size(), 2);

        long count = memberRepository.count();
        assertEquals(count, 2);
    }

    @Test
    public void findByUsernameAndAgeGreaterThenTest () {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("memberA", 10);

        // then
        assertEquals(result.get(0).getUsername(), "memberA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);

    }


    @Test
    public void findByNamesTest () {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        List<String> names = new ArrayList<String>(){{
            add("memberA");
            add("memberB");
        }};
        List<Member> result = memberRepository.findByNames(names);


        for (Member member : result) {
            System.out.println("name = " + member.getUsername());

        }
    }

    @Test
    public void testQuery () {
        // given
        Member member1 = new Member("memberA", 10);
        Member member2 = new Member("memberB", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        // when
        List<Member> findMember = memberRepository.findBy("memberA", 10);
        // then
        assertEquals(findMember.get(0).getUsername(), "memberA");
    }

    @Test
    public void testQueryDto () {
        // given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("memberA", 10);
        member1.setTeam(team);
        memberRepository.save(member1);

        // when
        List<MemberDto> findMember = memberRepository.findMemberDto();

        // then
        for (MemberDto memberDto : findMember) {
            System.out.println("memberDto = " + memberDto);
        }
    }
    
    @Test
    public void pagingTest () {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> contents = page.getContent();
//        long totalElements = page.getTotalElements();

        assertEquals(contents.size(), 3);
//        assertEquals(totalElements, 5);
        assertEquals(page.getNumber(), 0);
//        assertEquals(page.getTotalPages(), 2);
        assertEquals(page.isFirst(), true);
        assertEquals(page.hasNext(), true);

    }
}