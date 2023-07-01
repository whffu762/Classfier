package tomato.classifier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.main.ResultDto;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.service.MainService;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    private final MainService mainService;

    @GetMapping
    public String mainView() {
        return "main/mainPage";
    }

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

        model.addAttribute("result", diseaseDto);

        return "main/resultPage";
    }

    @GetMapping("/map")
    public String map(){
        return "main/map";
    }

}
