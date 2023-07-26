package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 엔티티에 API 검증을 위한 로직이 들어간다.(@NotEmpty 등)
     * - 실무에서는 회원 엔티티를 위한 API 가 다양하게 만들어지는데, 한 엔티티에 각각의 API 를 위한 모든 요청 요구사항을 담기는 어렵다.
     * - 엔티티가 변경되면 API 스펙이 변경된다.
     * 결론
     * - API 요청 스펙에 맞추어 별도의 DTO 를 파라미터로 받는다.
     */
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }

    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }

    /**
     * 회원 수정 API
     */
    @PatchMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     * 문제점
     * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
     * - 기본적으로 엔티티의 모든 값이 노출된다.
     * - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JasonIgnore, 별도의 뷰 등등)
     * - 실무에서는 같은 엔티티에 대해 API 가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API 를 위한 프레젠테이션 응답 로직을 담기는 어렵다.(별도의 Result 클래스 생성으로 해결)
     * 결론
     * - API 응답 스펙에 맞추어 별도의 DTO 를 반환한다.
     */
    // 조회 V1: 안 좋은 버전, 모든 엔티티가 노출, @JasonIgnore -> 엔티티에 적용하기 좋지 않은 어노테이션, api 가 이거 하나가 아니기 때문이다. 화면에 종속적이면 안된다.
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {

        return memberService.findMembers();

    }

    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO 를 반환한다.
     */
    @GetMapping("/api/v2/members")
    public Result membersV2() {

        List<Member> findMembers = memberService.findMembers();
        // Entity -> DTO
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getName()))
                .toList();

        return new Result(collect);

    }

    @Data
    static class CreateMemberResponse {

        private Long id;

        public CreateMemberResponse(Long id) {

            this.id = id;

        }
    }

    @Data
    static class CreateMemberRequest {

        @NotEmpty
        private String name;

    }

    @Data
    static class UpdateMemberRequest {

        @NotEmpty
        private String name;

    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {

        private Long id;

        private String name;

    }

    @Data
    @AllArgsConstructor
    static class Result<T> {

        private T data;

    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {

        private String name;

    }

}
