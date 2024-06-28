package tomato.classifier.dto.editMember;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewPasswordDto extends EditBase{

    @NotBlank
    private String password;
    @NotBlank
    private String password2;

    public NewPasswordDto(String id){
        super(id);
    }
}
