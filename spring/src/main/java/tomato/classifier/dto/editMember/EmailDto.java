package tomato.classifier.dto.editMember;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailDto extends EditBase{

    @NotBlank
    private String email;

    public EmailDto(String id, String email){
        super(id);
        this.email = email;
    }
}
