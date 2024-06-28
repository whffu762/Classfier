package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.CommentDto;
import tomato.classifier.service.CommentService;

import javax.transaction.Transactional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/comment")
    public String afterLogin(@RequestParam("no") Integer articleId){

        log.info("GET 호출됨");

        return "redirect:/article?no=" + articleId;
    }

//    @PostMapping("/comment")
//    public String write(@RequestParam("no") Integer id) throws InterruptedException{
//
//        log.info("POST 호출됨");
//
//        return "redirect:/article?no=" + id;
//    }

    @PostMapping("/comment")
    public ResponseEntity<CommentDto> write(@RequestBody CommentDto commentDTO){

        log.info("POST 호출됨");

        CommentDto comment = commentService.save(commentDTO);

        return ResponseEntity.status(HttpStatus.OK).body(comment);
    }


    @PutMapping("/comment")
    public ResponseEntity<CommentDto> edit(@RequestBody CommentDto commentDto) {

        CommentDto updateDto = commentService.update(commentDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }


    @DeleteMapping("/comment")
    public ResponseEntity<CommentDto> delete(@RequestBody CommentDto commentDto) {

        CommentDto deletedDto = commentService.delete(commentDto);

        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
    }

}
