package tomato.classifier.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tomato.classifier.data.ResultData;
import tomato.classifier.dto.DiseaseDto;
import tomato.classifier.service.MainService;

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
    public String resultView(@RequestBody ResultData data){

        mainService.save(data);

        return "redirect:/main/result";
    }

    @GetMapping("/result")
    public String resultView(Model model){

        DiseaseDto result = mainService.result();

        model.addAttribute("dto", result);

        return "main/resultPage";
    }

    @GetMapping("/map")
    public String map(){
        return "main/map";
    }

}
