package tomato.classifier.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.dto.main.ResultDto;
import tomato.classifier.service.MainService;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/main")
//@CrossOrigin(origins = "http://127.0.0.1:5000")
public class MainController {

    private final MainService mainService;

    @GetMapping
    public String mainView() {
        return "main/mainPage";
    }

    @PostMapping("/input")
    public String inputImg(@RequestParam("imgFiles") List<MultipartFile> files, Model model) throws IOException {

        mainService.saveImg(files);
        ResultDto resultDto = mainService.predict();
        DiseaseDto diseaseDto = mainService.getDiseaseInfo(resultDto);
        model.addAttribute("result", diseaseDto);

        return "main/resultPage";
    }


    @GetMapping("/map")
    public String map(){
        return "main/map";
    }

}
