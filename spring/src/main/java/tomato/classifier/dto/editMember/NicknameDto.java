package tomato.classifier.dto.editMember;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class NicknameDto extends EditBase{


    @NotBlank
    private String nickname;

    public NicknameDto(String id, String nickname){
        super(id);
        this.nickname=nickname;
    }

}
