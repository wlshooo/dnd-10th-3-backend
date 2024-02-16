package dnd.donworry.domain.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "댓글 API ResponseDto")
public class CommentResponseDto {
    @Schema(description = "댓글 ID", example = "12")
    private Long commentId;

    @Schema(description = "투표 ID", example = "3")
    private Long voteId;

    @Schema(description = "댓글 생성자")
    private User user;

    @Schema(description = "댓글 내용", example = "축의금 내지 말고 밥만 먹고 오세요")
    private String content;

    @Schema(description = "댓글 좋아요 개수", example = "21")
    private int likes;

    @Schema(description = "생성일", example = "2024-03-03T03:03:03")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2024-03-03T03:03:03")
    private LocalDateTime modifiedAt;

    public static CommentResponseDto of(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .voteId(comment.getVote().getId())
                .content(comment.getContent())
                .user(comment.getUser())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
