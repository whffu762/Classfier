package tomato.classifier.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tomato.classifier.dto.MemberDto;
import tomato.classifier.dto.editMember.EmailDto;
import tomato.classifier.dto.editMember.NewPasswordDto;
import tomato.classifier.dto.editMember.NicknameDto;
import tomato.classifier.dto.editMember.PasswordDto;
import tomato.classifier.service.MemberService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model){
        return "auth/login";
    }

    @GetMapping("/login/error")
    public String loginError(Model model){

       model.addAttribute("loginErrorMsg", "ID PW 불일치");
        return "auth/login";
    }


    @GetMapping("/register")
    public String addMember(Model model){
        model.addAttribute("Register", new MemberDto());
        return "auth/register";
    }


    @GetMapping("/edit/mypage")  //인증 필요
    public String myPage(Authentication authentication, Model model){

        MemberDto user = memberService.findAuthenticatedUserInfo(authentication);
        model.addAttribute("MyPage", user);

        return "auth/myPage";
    }

    @GetMapping("/edit/nickname")
    public String editNickname(Authentication authentication ,Model model){

        MemberDto user = memberService.findAuthenticatedUserInfo(authentication);
        model.addAttribute("Nickname", new NicknameDto(user.getMemberId(), user.getNickname()));

        return "auth/edit/editNickname";
    }

    @GetMapping("/edit/email")
    public String editEmail(Authentication authentication, Model model){

        MemberDto user = memberService.findAuthenticatedUserInfo(authentication);
        model.addAttribute("Email", new EmailDto(user.getMemberId(), user.getEmail()));

        return "auth/edit/editEmail";
    }

    @GetMapping("/check/password")    //인증 필요
    public String checkPassword(Authentication authentication, Model model){

        model.addAttribute("Password", new PasswordDto(authentication.getName()));
        return "auth/edit/checkPassword";
    }

    @GetMapping("/edit/password")
    public String editPassword(Authentication authentication, Model model){

        model.addAttribute("NewPassword", new NewPasswordDto(authentication.getName()));
        return "auth/edit/editPassword";
    }

}