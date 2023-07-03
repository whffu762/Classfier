package tomato.classifier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomato.classifier.dto.CommentDto;
import tomato.classifier.entity.Article;
import tomato.classifier.entity.Comment;
import tomato.classifier.repository.article.ArticleRepository;
import tomato.classifier.repository.article.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public List<CommentDto> comments(Integer articleId) {

        List<CommentDto> allComments = commentRepository.findByArticleId(articleId)
                .stream()
                .map(comment -> CommentDto.convertDto(comment))
                .collect(Collectors.toList());

        List<CommentDto> comments = new ArrayList<>();

        for (CommentDto comment : allComments) {
            if (!comment.isDeleteYn()) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public CommentDto save(Integer articleId, CommentDto commentDto){

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 등록할 게시글 조회 실패"));

        commentDto.setDeleteYn(false);
        commentDto.setUpdateYn(false);
        Comment comment = Comment.convertEntity(commentDto, article);
        commentRepository.save(comment);

        CommentDto dto = CommentDto.convertDto(comment);

        return dto;
    }


    public CommentDto update(Integer commentId, CommentDto commentDto){
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 id 오류"));

        target.patch(commentDto);

        Comment updated = commentRepository.save(target);

        CommentDto updateDto = CommentDto.convertDto(updated);

        return updateDto;
    }

    public CommentDto delete(Integer commentId){
        Comment target = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 id 오류"));

        target.delete();

        Comment deleted = commentRepository.save(target);

        CommentDto deletedDto = CommentDto.convertDto(deleted);

        return deletedDto;
    }


}
