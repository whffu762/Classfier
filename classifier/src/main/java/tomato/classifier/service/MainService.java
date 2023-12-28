package tomato.classifier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.dto.main.ResultDto;
import tomato.classifier.entity.Disease;
import tomato.classifier.repository.main.DiseaseRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainService {
    private final DiseaseRepository diseaseRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("/home/ubuntu/ai/inputImg/target")
    private String fileDir; //입력된 이미지가 저장될 경로

    @Value("http://127.0.0.1:5000/predict")
    private String url; //Flask 서버의 URL

    public void saveImg(List<MultipartFile> files) throws IOException {
        //stream 으로 다 바꾸기

        files.stream()
                .filter(file -> !file.isEmpty())
                .forEach(file -> {
                    try {
                        String filePath = fileDir + file.getOriginalFilename();
                        file.transferTo(new File(filePath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }

    public ResultDto predict() throws IOException{
        //간단하게 쓸 수 있는 restTemplate

        String response = restTemplate.getForObject(url, String.class);
        ResultDto resultDto = objectMapper.readValue(response, ResultDto.class);

        return resultDto;
    }

    public DiseaseDto getDiseaseInfo(ResultDto resultDto){

        Disease target = diseaseRepository.findById(resultDto.getName())
                .orElseThrow(()-> new IllegalArgumentException("질병 조회 실패"));

        return DiseaseDto.convertDto(target, resultDto.getProb());
    }
}
