package tomato.classifier.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tomato.classifier.dto.main.DiseaseDto;
import tomato.classifier.service.MainService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> inputImg(List<MultipartFile> files) throws IOException {

        mainService.saveImg(files);
        String result_url = mainService.predict();
        HttpHeaders headers = new HttpHeaders();    //이런 걸 bean 으로 등록해야 하나..?
        headers.setLocation(URI.create(result_url));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/result")
    public String resultView(@RequestParam Map<String, Object> result, Model model){

        DiseaseDto diseaseDto = mainService.getDiseaseInfo(result);
        model.addAttribute("result", diseaseDto);

        return "main/resultPage";
    }

    @GetMapping("/map")
    public String map(){
        return "main/map";
    }

}
