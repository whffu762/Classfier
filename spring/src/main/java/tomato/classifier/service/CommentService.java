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
                .map(Comment::convertDto)
                .collect(Collectors.toList());

        List<CommentDto> comments = new ArrayList<>();
        for (CommentDto comment : allComments) {
            if (!comment.isDeleteYn()) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public CommentDto save(CommentDto commentDto){

        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("comment save - 게시글 조회 실패"));

        commentDto.setDeleteYn(false);
        commentDto.setUpdateYn(false);
        return commentRepository.save(commentDto.convertEntity(article)).convertDto();

    }

    public CommentDto update(CommentDto inputCommentDto){

        Article article = articleRepository.findById(inputCommentDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("comment update - 게시글 조회 실패"));

        CommentDto newCommentDto = commentRepository.findById(inputCommentDto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("comment update - 댓글 조회 실패"))
                .convertDto();

        if (!inputCommentDto.getContent().isEmpty()) {
            newCommentDto.setContent(inputCommentDto.getContent());
            newCommentDto.setUpdateYn(true);
        }

        return commentRepository.save(newCommentDto.convertEntity(article)).convertDto();
    }

    public CommentDto delete(CommentDto commentDto){
        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(() -> new IllegalArgumentException("comment delete - 게시글 조회 실패"));

        CommentDto oldCommentDto = commentRepository.findById(commentDto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("comment delete - 댓글 조회 실패"))
                .convertDto();

        oldCommentDto.setDeleteYn(true);

        return commentRepository.save(oldCommentDto.convertEntity(article)).convertDto();
    }
}
