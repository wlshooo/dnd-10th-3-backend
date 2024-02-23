package dnd.donworry.service.impl;

import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.domain.dto.comment.CommentRequestDto;
import dnd.donworry.domain.dto.comment.CommentResponseDto;
import dnd.donworry.domain.entity.Comment;
import dnd.donworry.domain.entity.CommentLike;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import dnd.donworry.exception.CustomException;
import dnd.donworry.repository.CommentLikeRepository;
import dnd.donworry.repository.CommentRepository;
import dnd.donworry.repository.UserRepository;
import dnd.donworry.repository.VoteRepository;
import dnd.donworry.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    @Override
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long voteId, String email) {

        Comment savedComment = commentRepository.save(
                Comment.toEntity(findVote(voteId), findUser(email), commentRequestDto));

        return CommentResponseDto.of(savedComment, false);
    }

    public List<CommentResponseDto> getComments(String email, Long voteId, Long lastCommentId, int size) {
        List<CommentResponseDto> list = new ArrayList<>();
        Vote vote = findVote(voteId);
        List<Comment> comments = commentRepository.findCommentsByVote(vote, lastCommentId, size);

        if (lastCommentId == 0L && comments.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        } else if (comments.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_SEARCH_NOT_FOUND);
        }
        comments.forEach(comment -> {
            Optional<CommentLike> commentLike = commentLikeRepository.findByCommentId(comment.getId());
            boolean isStatus = commentLike.isEmpty() || !commentLike.get().isStatus();

            list.add(CommentResponseDto.of(comment, !isStatus));
        });

        return list;
    }

    @Transactional
    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, String email) {
        Comment comment = validateUserAndComment(commentId, email);
        comment.updateContent(commentRequestDto.getContent());
        commentRepository.save(comment);

        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentId(comment.getId());

        return commentLike.map(like -> CommentResponseDto.of(comment, like.isStatus()))
                .orElse(CommentResponseDto.of(comment, false));
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, String email) {
        Comment comment = validateUserAndComment(commentId, email);
        commentRepository.delete(comment);
    }

    public CommentResponseDto updateEmpathy(Long commentId, String email) {
        User user = findUser(email);
        Comment comment = validateUserAndComment(commentId, user.getEmail());
        Optional<CommentLike> findCommentLike = commentLikeRepository.findByCommentId(comment.getId());

        CommentLike commentLike = findCommentLike.orElseGet(() -> CommentLike.toEntity(user, comment, false));

        savedCommentLike(commentLike);

        savedComment(comment, commentLike);

        return CommentResponseDto.of(comment, commentLike.isStatus());
    }

    private void savedCommentLike(CommentLike commentLike) {
        commentLike.updateStatus();
        commentLikeRepository.save(commentLike);
    }

    private void savedComment(Comment comment, CommentLike commentLike) {
        comment.updateLike(commentLike.isStatus());
        commentRepository.save(comment);
    }


    private Comment validateUserAndComment(Long commentId, String email) {
        User user = findUser(email);
        Comment comment = findComment(commentId);
        if (!user.getId().equals(comment.getUser().getId())) {
            throw new CustomException(ErrorCode.COMMENT_NOT_MATCH);
        }
        return comment;
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private User findUser(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Vote findVote(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new CustomException(ErrorCode.VOTE_NOT_FOUND));
    }
}
