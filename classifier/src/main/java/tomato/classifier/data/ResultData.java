package tomato.classifier.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
public class ResultData {
    public String name;
    public Integer prob;
}
