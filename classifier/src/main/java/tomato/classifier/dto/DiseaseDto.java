package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.data.ResultData;
import tomato.classifier.entity.Disease;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDto {

    private String id;

    private String d_name;

    private String src;

    private String solution;

    private Integer prob;

    public static DiseaseDto convertDto(Disease target, ResultData data){
        return new DiseaseDto(
                target.getId(),
                target.getDName(),
                target.getSrc(),
                target.getSolution(),
                data.getProb()
        );
    }
}
