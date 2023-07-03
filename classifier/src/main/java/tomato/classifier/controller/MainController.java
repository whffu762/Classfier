package tomato.classifier.controller;

<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
=======
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.service.MainService;

import java.io.IOException;
import java.net.URI;
import java.util.List;
=======
import tomato.classifier.dto.main.ResultDto;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.service.MainService;

>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
<<<<<<< HEAD
//@CrossOrigin(origins = "http://127.0.0.1:5000")
=======
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146
public class MainController {

    private final MainService mainService;

    @GetMapping
    public String mainView() {
        return "main/mainPage";
    }

<<<<<<< HEAD
    @PostMapping("/input")
    public ResponseEntity<?> inputImg(List<MultipartFile> files) throws IOException {

        log.info("aaaa");
        mainService.saveImg(files);

        log.info("aaaa");
        String result_url = mainService.predict();

        log.info("aaaa");
        HttpHeaders headers = new HttpHeaders();    //이런 걸 bean 으로 등록해야 하나..?
        headers.setLocation(URI.create(result_url));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/result")
    public String resultView(@RequestParam Map<String, Object> result, Model model){

        DiseaseDto diseaseDto = mainService.getDiseaseInfo(result);
=======
    @PostMapping("/predict")
    @ResponseBody
    public ResponseEntity<DiseaseDto> resultView(@RequestBody ResultDto result){

        DiseaseDto diseaseDto = mainService.getDiseaseInfo(result);

        return ResponseEntity.status(HttpStatus.OK).body(diseaseDto);
    }

    @GetMapping("/result")
    public String resultView(@RequestParam Map<String, Object> params, Model model){

        ObjectMapper mapper = new ObjectMapper();
        DiseaseDto diseaseDto = mapper.convertValue(params, DiseaseDto.class);
>>>>>>> fbf704387ef36c13eaade9e742c00edc1bc55146

        model.addAttribute("result", diseaseDto);

        return "main/resultPage";
    }

    @GetMapping("/map")
    public String map(){
        return "main/map";
    }

}
