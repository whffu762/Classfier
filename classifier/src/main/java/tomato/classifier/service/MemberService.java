package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tomato.classifier.data.Role;
import tomato.classifier.dto.CustomUserDetails;
import tomato.classifier.dto.MemberDto;
import tomato.classifier.dto.editMember.*;
import tomato.classifier.entity.Member;
import tomato.classifier.repository.auth.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService { //DB에서 회원 정보 조회 역할 UserDetailsService
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean check(Member target){

        if(target != null){
            return false;   //이미 있으면 false
        }
        else{
            return true;    //없으면 true
        }
    }
    public boolean checkId(String id){
        return check(memberRepository.findById(id));
    }

    public boolean checkNickname(String nickname){
        return check(memberRepository.findByNickname(nickname));
    }
    public boolean checkEmail(String email){
        return check(memberRepository.findByEmail(email));
    }

    public boolean checkPassword(String password, String password2){
        if(password.equals(password2)){
            return false;
        }
        else{
            return true;
        }
    }

    public MemberDto register(MemberDto memberDto){

        memberDto.setRole(Role.ROLE_MEMBER); //@InitBinder로 대체 가능?
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        Member member = Member.convertEntity(memberDto);

        memberRepository.save(member);

        return memberDto;
    }

    public MemberDto findAuthenticatedUserInfo(Authentication authentication){

        Member member = memberRepository.findById(authentication.getName());

        return MemberDto.convertDto(member);

    }

    public MemberDto checkReqMemberId(EditBase inputDto, Authentication authentication){
        if(!inputDto.getMemberId().equals(authentication.getName())){
            throw new IllegalArgumentException("사용자 요청이 잘못됨 - id 불일치");
        }
        Member user = memberRepository.findById(inputDto.getMemberId());

        return MemberDto.convertDto(user);
    }

    public MemberDto editNickname(NicknameDto inputDto, Authentication authentication){

        MemberDto memberDto = checkReqMemberId(inputDto, authentication);   //이 부분은 interceptor 같은걸로 빼버리면 좋을 듯
        memberDto.setNickname(inputDto.getNickname());

        Member member = Member.convertEntity(memberDto);
        memberRepository.save(member);

        return memberDto;
    }

    public MemberDto editEmail(EmailDto inputDto, Authentication authentication){

        MemberDto memberDto = checkReqMemberId(inputDto, authentication);
        memberDto.setEmail(inputDto.getEmail());

        Member member = Member.convertEntity(memberDto);
        memberRepository.save(member);

        return memberDto;
    }

    public boolean checkPassword(PasswordDto inputDto, Authentication authentication){
        MemberDto memberDto = checkReqMemberId(inputDto, authentication);

        return passwordEncoder.matches(inputDto.getPassword(), memberDto.getPassword());

    }

    public MemberDto editPassword(NewPasswordDto inputDto, Authentication authentication){

        MemberDto memberDto = checkReqMemberId(inputDto, authentication);
        memberDto.setPassword(passwordEncoder.encode(inputDto.getPassword()));

        Member member = Member.convertEntity(memberDto);
        memberRepository.save(member);

        return memberDto;
    }

    //조회한 결과를 UserDetails 에 저장
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id);

        log.info("load is called");

        if(member == null){
            throw new UsernameNotFoundException(id);
        }

        log.info("memberId = {}", member.getMemberId());

        return new CustomUserDetails(MemberDto.convertDto(member));
    }

}
