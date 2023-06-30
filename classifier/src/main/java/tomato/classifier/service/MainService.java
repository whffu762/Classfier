package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.classifier.data.ResultData;
import tomato.classifier.dto.DiseaseDto;
import tomato.classifier.entity.Disease;
import tomato.classifier.repository.main.DiseaseRepository;
import tomato.classifier.repository.main.TomatoRepository;

@Service
@RequiredArgsConstructor
public class MainService {

    private final TomatoRepository tomatoRepository;
    private final DiseaseRepository diseaseRepository;

    public void save(ResultData data){
        tomatoRepository.save(data);
    }

    public DiseaseDto result(){

        ResultData data = tomatoRepository.find();

        Disease target = diseaseRepository.findById(data.getName())
                .orElseThrow(()-> new IllegalArgumentException("질병 조회 실패"));

        return DiseaseDto.convertDto(target, data);
    }
}
