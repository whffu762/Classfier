package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.CustomUserDetails;
import tomato.classifier.dto.MemberDto;
import tomato.classifier.dto.editMember.EmailDto;
import tomato.classifier.dto.editMember.NicknameDto;
import tomato.classifier.dto.editMember.NewPasswordDto;
import tomato.classifier.dto.editMember.PasswordDto;
import tomato.classifier.service.MemberService;

import javax.validation.Valid;
import java.util.List;

//@RestController("/auth")
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {


    private final MemberService memberService;

    @PostMapping("/register")
    public String addMember(@Valid @ModelAttribute("Register") MemberDto memberDto, BindingResult bindingResult, Model model){

        //예외 이미 가입한 회원, 비밀번호 확인했는데 틀림
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


        MemberDto registered = memberService.register(memberDto);

        return "redirect:/auth/login";
    }

    @PostMapping("/edit/nickname")   //인증필요
    public String editNickname(@Valid @ModelAttribute("Nickname") NicknameDto inputDto, BindingResult bindingResult, Authentication authentication, Model model){

        if(!memberService.checkNickname(inputDto.getNickname())){
            bindingResult.reject("existedNickname");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editNickname";
        }

        MemberDto memberDto = memberService.editNickname(inputDto, authentication);
        model.addAttribute("MyPage", memberDto);

        return "auth/myPage";    //memberDto 리턴하는 것도 바꿔야될 듯
    }

    @PostMapping("/edit/email")  //인증 필요
    public String editEmail(@Valid @ModelAttribute("Email") EmailDto inputDto, BindingResult bindingResult, Authentication authentication, Model model ){

        if(!memberService.checkEmail(inputDto.getEmail())){
            bindingResult.reject("existedEmail");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editEmail";
        }

        MemberDto memberDto = memberService.editEmail(inputDto,authentication);
        model.addAttribute("MyPage", memberDto);

        return "auth/myPage";    //memberDto 리턴하는 것도 바꿔야될 듯
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

    @PostMapping("/edit/password")   //인증이 있긴 한데 좀 비효율적인거 같은 느낌..
    public String editPassword(@Valid @ModelAttribute("NewPassword") NewPasswordDto inputDto, BindingResult bindingResult, Authentication authentication, Model model){

        if(memberService.checkPassword(inputDto.getPassword(), inputDto.getPassword2())){
            bindingResult.reject("pwNotEqual");
        }
        if(bindingResult.hasErrors()){
            return "auth/edit/editPassword";
        }

        memberService.editPassword(inputDto, authentication);

        return "redirect:/auth/edit/mypage";    //memberDto 리턴하는 것도 바꿔야될 듯
    }




    /**========================================================================================**/

    //검증에 대한 처리를 JSON으로 하고 싶은데 프론트에서 처리를 어캐하는지 모르겠음
    //json 데이터를 받아서 프론트에서 출력하는 방법이 뭔지... 내가 굳이 거기까지 공부해야하나 싶기도 하고..
    public ResponseEntity<MemberDto> register(@Valid @RequestBody MemberDto memberDto, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError fieldError = (FieldError) objectError;
                String message = objectError.getDefaultMessage();
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(memberDto);
            //body 에 bingingResult 넣으면 오류남 이거 처리 방법 필요함
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberDto);
    }

    //이 방법은 응답을 JSON 으로 보내기 때문에 타임리프를 쓰는 식으론 ErrorMessage 를 받는 방법이 다름
    //응답으로 받는 JSON 을 따로 처리해서 프론트에서 출력해야 하는데 그 방법은 시간 나면 공부하는 걸로하고 프론트니까;;;
        //일단 에러에 대한 정보를 JSON 형태로 에러 메시지와 함께 보내는 방법까지만 공부하는 걸로하자
        //방법은 ResponseDto 를 만들어서 거기에 메시지를 넣는 식인데.. @ExceptionHandler 를 어떻게 쓸지는 고민해봐야 할 듯
    //Validator, @ExceptionHandler, @ControllerAdvice 이것들 쓰는 방식을 확실히 하자 계속 까먹는거 같음
            //@ExceptionHandler 로 return ModelAndView 하면 동적인 페이지 출력이 가능하다고 하는데 일단 2단계로 보류

}
