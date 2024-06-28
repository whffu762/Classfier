package tomato.classifier.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDto {

    @NotBlank
    private String memberId;

    @NotBlank
    private String password;
}
