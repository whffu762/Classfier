package tomato.classifier.dto.editMember;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
public class PasswordDto extends EditBase{

    @NotBlank
    private String password;

    public PasswordDto(String id){
        super(id);
    }



}
