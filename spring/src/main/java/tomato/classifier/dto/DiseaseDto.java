package tomato.classifier.dto;

import lombok.*;
import tomato.classifier.entity.Disease;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDto {

    private String id;

    private String diseaseName;

    private Integer probability;

    private String sourcePath;

    private String solution;
}
