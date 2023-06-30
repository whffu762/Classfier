package tomato.classifier.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomato.classifier.dto.CommentDto;
import tomato.classifier.service.CommentService;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    /*
    @GetMapping("/comment/{commentId}")
    public String get(@PathVariable Integer commentId, Model model){

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("댓글 조회 실패"));

        CommentDto commentDto = CommentDto.convertDto(comment);
        model.addAttribute("comment", commentDto);

        return "board/commentUpdate";
    }
    */


    @PostMapping("/article/{articleId}/comment-add")
    @Transactional
    public ResponseEntity<CommentDto> write(@PathVariable Integer articleId, @RequestBody CommentDto commentDTO){

        CommentDto dto = commentService.save(articleId, commentDTO);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    @Transactional
    @PatchMapping("/comment-edit/{commentId}")
    public ResponseEntity<CommentDto> edit(@PathVariable Integer commentId, @RequestBody CommentDto commentDto) {

        CommentDto updateDto = commentService.update(commentId, commentDto);

        return ResponseEntity.status(HttpStatus.OK).body(updateDto);
    }


    @Transactional
    @DeleteMapping("/comment-delete/{commentId}")
    public ResponseEntity<CommentDto> delete(@PathVariable Integer commentId) {

        CommentDto deletedDto = commentService.delete(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(deletedDto);
        // delete_yn 여부에 따라 프론트에 보여지는 건 아직 구현 안함
    }

}
