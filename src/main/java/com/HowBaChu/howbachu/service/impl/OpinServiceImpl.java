package com.HowBaChu.howbachu.service.impl;

import com.HowBaChu.howbachu.domain.dto.opin.OpinRequestDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.opin.OpinThreadResponseDto;
import com.HowBaChu.howbachu.domain.entity.Opin;
import com.HowBaChu.howbachu.domain.entity.Vote;
import com.HowBaChu.howbachu.exception.CustomException;
import com.HowBaChu.howbachu.exception.constants.ErrorCode;
import com.HowBaChu.howbachu.repository.OpinRepository;
import com.HowBaChu.howbachu.repository.VoteRepository;
import com.HowBaChu.howbachu.service.OpinService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OpinServiceImpl implements OpinService {


    private final VoteRepository voteRepository;
    private final OpinRepository opinRepository;
    private final ApplicationContext context;
    private final List<Long> opinIdList = new ArrayList<>();

    @Override
    @Transactional
    @CachePut(value = "opin", key = "#result.id")
    public OpinResponseDto createOpin(OpinRequestDto requestDto, String email, Long parentId) {

        Vote vote = voteRepository.findVoteByEmail(email).orElseThrow(
            () -> new CustomException(ErrorCode.VOTE_NOT_FOUND)
        );

        Opin opin;

        if (parentId == null) {
            opin = Opin.of(requestDto.getContent(), vote);
        } else {
            Opin parentOpin = opinRepository.findById(parentId).orElseThrow(
                () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
            );
            opin = Opin.of(requestDto.getContent(), vote, parentOpin);
        }

        Opin opinSaved = opinRepository.save(opin);

        opinIdList.add(opinSaved.getId());

        return OpinResponseDto.of(opinSaved);
    }

    @Override
    public List<OpinResponseDto> getOpinList() {
        OpinServiceImpl self = context.getBean(OpinServiceImpl.class);
        return opinIdList.stream().map(self::getOpin).collect(Collectors.toList());
    }

    @Override
    public OpinThreadResponseDto getOpinThread(Long parentId) {
        OpinResponseDto parentOpin = opinRepository.fetchParentOpin(parentId);
        List<OpinResponseDto> childOpinList = opinRepository.fetchOpinChildList(parentId);
        return new OpinThreadResponseDto(parentOpin, childOpinList);
    }

    @Override
    @Transactional
    public Long removeOpin(Long opinId, String email) {
        opinRepository.delete(getOpin(opinId, email));
        return opinId;
    }

    @Override
    @Transactional
    @CachePut(value = "opin", key = "#result.id")
    public Opin updateOpin(OpinRequestDto requestDto, Long opinId, String email) {
        Opin opin = getOpin(opinId, email);
        opin.updateContent(requestDto.getContent());
        return opin;
    }

    @Override
    public Opin getOpin(Long opinId, String email) {
        return opinRepository.findByOpinIdAndEmail(opinId, email).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_MISS_MATCH)
        );
    }
    @Override
    @Cacheable(value = "opin", key = "#opinId")
    public OpinResponseDto getOpin(Long opinId) {
        return OpinResponseDto.of(opinRepository.findById(opinId).orElseThrow(
            () -> new CustomException(ErrorCode.OPIN_NOT_FOUND)
        ));
    }
}
