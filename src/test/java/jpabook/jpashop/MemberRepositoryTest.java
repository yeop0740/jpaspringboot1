package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // spring 것을 쓰는 것을 권장!! - 기능이 다양하다
    @Rollback(false)
    public void memberTest() throws Exception {
        // given
        Member member = new Member();
        member.setUsername("memberA");

        // when
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // true 임 같은 영속성 컨텍스트 안에선 id 값이 같으면 같은 객체로 인식함.
        // 1차 캐시로 불리는 곳에 영속성 컨텍스로 관리되고 있는 것에 이미 존재하기 때문에 기존에 관리하던 객체가 나온다.
        System.out.println("findMember == member: " + (findMember == member));
        System.out.println("findMember = " + findMember);
        System.out.println("member = " + member);
    }
         
    
}