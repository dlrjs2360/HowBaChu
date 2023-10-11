package com.HowBaChu.howbachu.domain.dto.opin;

import com.HowBaChu.howbachu.domain.constants.Selection;
import com.HowBaChu.howbachu.domain.entity.Opin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OpinResponseDto {

    private Long id;
    private String topicSubTitle;
    private Selection selection;
    private String nickname;
    private String content;
    private int likeCnt;

    public static OpinResponseDto of(Opin opin) {
        return OpinResponseDto.builder()
            .id(opin.getId())
            .topicSubTitle(opin.getVote().getSelectSubTitle())
            .selection(opin.getVote().getSelection())
            .nickname(opin.getVote().getMember().getUsername())
            .content(opin.getContent())
            .likeCnt(opin.getLikeCnt())
            .build();
    }
}
