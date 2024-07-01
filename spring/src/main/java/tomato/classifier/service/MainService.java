package tomato.classifier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.dto.DiseaseDto;
import tomato.classifier.entity.Disease;
import tomato.classifier.repository.main.DiseaseRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {
    private final DiseaseRepository diseaseRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("C:/Users/whffu/VScode/forTest/5_dest/Target/")
    //@Value("/home/ubuntu/ai/inputImg/target/")
    private String fileDir; //입력된 이미지가 저장될 경로

    @Value("http://127.0.0.1:5000/predict-tomato-disease")
    private String url; //Flask 서버의 URL

    public void saveImg(List<MultipartFile> files) {
        //stream 으로 다 바꾸기

        files.stream()
                .filter(file -> !file.isEmpty())
                .forEach(file -> {
                    try {
                        String filePath = fileDir + file.getOriginalFilename();
                        file.transferTo(new File(filePath));
                    } catch (IOException e) {
                        log.error("image input error : { }",e);
                        //사진이 정상적으로 저장되지 않았을 때의 예외 처리 구현 필요
                    }
                });

    }

    public DiseaseDto predict() throws IOException{
        //간단하게 쓸 수 있는 restTemplate

        String response = restTemplate.getForObject(url, String.class);
        log.info(response);

        DiseaseDto tmp = objectMapper.readValue(response, DiseaseDto.class);

        log.info("id : " + tmp.getId());
        log.info("probability : " + tmp.getProbability());

        Disease target = diseaseRepository.findById(tmp.getId())
                .orElseThrow(()-> new IllegalArgumentException("질병 조회 실패"));

        return target.convertDto(tmp.getProbability());

        //여기서도 flask에게 결과 못 받았을 때의 예외 처리
    }
}
