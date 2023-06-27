package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 원래는 이름은 중복되어도 되지만 이번 예제에선 아이디를 따로 만들지 않기 때문에 이름을 중복되지 않도록 만든다.
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        /**
         * 해당 로직을 통해 중복성을 검토하지만 insert 이전에 다수의 트랜잭션에서 해당 함수를 호출한다면 동일한 이름이 없는 것으로 인식한다.
         * 이 로직은 결국 중복성을 체크하는 시점이 insert 가 일어나기 전이라면 동일한 name 의 member 객체가 insert 되는 것을 막을 순 없는 것이다.
         * 따라서 명확하게 중복을 방지하기 위해선 테이블에 해당 컬럼을 UK 로 설정하는 방법을 선택한다.
         */
        List<Member> members = memberRepository.findByName(member.getName());
        if (!members.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
