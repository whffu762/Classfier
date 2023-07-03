package tomato.classifier.dto.main;

import lombok.*;
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

<<<<<<< HEAD
    public static DiseaseDto convertDto(Disease target, Integer prob){
=======
    public static DiseaseDto convertDto(Disease target, ResultDto result){
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
        return new DiseaseDto(
                target.getId(),
                target.getDName(),
                target.getSrc(),
                target.getSolution(),
<<<<<<< HEAD
                prob
=======
                result.getProb()
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
        );
    }
}
