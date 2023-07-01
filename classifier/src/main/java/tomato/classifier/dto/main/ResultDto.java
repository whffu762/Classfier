package tomato.classifier.dto.main;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResultDto {
    public String name;
    public Integer prob;
}
