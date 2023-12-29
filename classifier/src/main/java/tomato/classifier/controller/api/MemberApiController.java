package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.MemberDto;
import tomato.classifier.dto.editMember.EmailDto;
import tomato.classifier.dto.editMember.NicknameDto;
import tomato.classifier.dto.editMember.NewPasswordDto;
import tomato.classifier.dto.editMember.PasswordDto;
import tomato.classifier.service.MemberService;

import javax.validation.Valid;

//@RestController("/auth")
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {


    private final MemberService memberService;

    @PostMapping("/register")
    public String addMember(@Valid @ModelAttribute("Register") MemberDto memberDto, BindingResult bindingResult, Model model){

        if(!memberService.checkId(memberDto.getMemberId())){
            bindingResult.reject("existedAccount");
        }

        if(!memberService.checkEmail(memberDto.getEmail())){
            bindingResult.reject("existedEmail");
        }

        if(!memberService.checkNickname(memberDto.getNickname())){
            bindingResult.reject("existedNickname");
        }

        if(memberService.checkPassword(memberDto.getPassword(), memberDto.getPassword2())){
            bindingResult.reject("pwNotEqual");
        }

        if(bindingResult.hasErrors()){
            return "auth/register";
        }


        memberService.register(memberDto);

        return "redirect:/auth/login";
    }

    @PostMapping("/edit/nickname")
    public String editNickname(@Valid @ModelAttribute("Nickname") NicknameDto inputDto, BindingResult bindingResult, Authentication authentication, Model model){

        if(!memberService.checkNickname(inputDto.getNickname())){
            bindingResult.reject("existedNickname");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editNickname";
        }

        log.info("Nickname = " + inputDto.getNickname());
        log.info("Nickname = " + inputDto.getMemberId());

        MemberDto memberDto = memberService.editNickname(inputDto, authentication);
        log.info(memberDto.getNickname());
        model.addAttribute("MyPage", memberDto);

        return "auth/myPage";
    }

    @PostMapping("/edit/email")
    public String editEmail(@Valid @ModelAttribute("Email") EmailDto inputDto, BindingResult bindingResult, Authentication authentication, Model model ){

        if(!memberService.checkEmail(inputDto.getEmail())){
            bindingResult.reject("existedEmail");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editEmail";
        }

        MemberDto memberDto = memberService.editEmail(inputDto,authentication);
        model.addAttribute("MyPage", memberDto);

        return "auth/myPage";
    }

    @PostMapping("/check/password")
    public String checkPassword(@Valid @ModelAttribute("Password") PasswordDto inputDto, BindingResult bindingResult , Authentication authentication, Model model){

        if(!memberService.checkPassword(inputDto, authentication)){
            bindingResult.reject("pwWrong");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/checkPassword";
        }

        return "redirect:/auth/edit/password";
    }

    @PostMapping("/edit/password")
    public String editPassword(@Valid @ModelAttribute("NewPassword") NewPasswordDto inputDto, BindingResult bindingResult, Authentication authentication, Model model){

        if(memberService.checkPassword(inputDto.getPassword(), inputDto.getPassword2())){
            bindingResult.reject("pwNotEqual");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editPassword";
        }

        memberService.editPassword(inputDto, authentication);

        return "redirect:/auth/edit/mypage";
    }
}
