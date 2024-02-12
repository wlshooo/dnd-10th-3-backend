package dnd.donworry.domain.dto.vote;

import java.time.LocalDateTime;
import java.util.List;

import dnd.donworry.domain.dto.selection.SelectionResponseDto;
import dnd.donworry.domain.entity.User;
import dnd.donworry.domain.entity.Vote;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "투표 API Response")
public class VoteResponseDto {

	@Schema(description = "투표 ID", example = "1")
	private Long id;

	@Schema(description = "투표 생성자")
	private User user;

	@Schema(description = "투표 제목", example = "축의금은 얼마가 적당할까요?")
	private String title;

	@Schema(description = "투표 내용", example = "절친 결혼식에 축의금을 얼마나 주는게 적당할까요?")
	private String content;

	@Schema(description = "투표 선택지")
	private List<SelectionResponseDto> selections;

	@Schema(description = "좋아요 개수", example = "0")
	private int likes;

	@Schema(description = "조회수", example = "0")
	private int views;

	@Schema(description = "투표자 수", example = "0")
	private int voters;

	@Schema(description = "투표 상태", example = "false")
	private boolean status;

	@Schema(description = "투표 마감일", example = "2021-08-01T00:00:00")
	private LocalDateTime closeDate;

	@Schema(description = "생성일", example = "2021-07-01T00:00:00")
	private LocalDateTime createdAt;

	@Schema(description = "수정일", example = "2021-07-01T00:00:00")
	private LocalDateTime updatedAt;

	public static VoteResponseDto of(Vote vote, List<SelectionResponseDto> selections) {
		return VoteResponseDto.builder()
			.id(vote.getId())
			.user(vote.getUser())
			.title(vote.getTitle())
			.content(vote.getContent())
			.selections(selections)
			.likes(vote.getLikes())
			.views(vote.getViews())
			.voters(vote.getVoters())
			.status(vote.isStatus())
			.closeDate(vote.getCloseDate())
			.createdAt(vote.getCreatedAt())
			.updatedAt(vote.getModifiedAt())
			.build();
	}
}