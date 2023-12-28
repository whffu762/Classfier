package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.classifier.dto.main.ResultDto;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.entity.Disease;
import tomato.classifier.repository.main.DiseaseRepository;

@Service
@RequiredArgsConstructor
public class MainService {

    private final DiseaseRepository diseaseRepository;

    public DiseaseDto getDiseaseInfo(ResultDto result){

        Disease target = diseaseRepository.findById(result.getName())
                .orElseThrow(()-> new IllegalArgumentException("질병 조회 실패"));

        return DiseaseDto.convertDto(target, result);
    }
}
