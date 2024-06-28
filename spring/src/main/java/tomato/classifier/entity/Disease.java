package tomato.classifier.entity;

import lombok.*;
import tomato.classifier.dto.DiseaseDto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Disease {

    @Id
    private String id;

    private String diseaseName;

    private String solution;

    private String sourcePath;

    public DiseaseDto convertDto(Integer probability){

        return DiseaseDto.builder()
                .id(this.id)
                .diseaseName(this.diseaseName)
                .probability(probability)
                .solution(this.solution)
                .sourcePath(this.sourcePath)
                .build();
    }
}
