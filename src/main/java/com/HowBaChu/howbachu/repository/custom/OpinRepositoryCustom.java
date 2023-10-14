package com.HowBaChu.howbachu.repository.custom;


import com.HowBaChu.howbachu.domain.dto.opin.OpinResponseDto;
import com.HowBaChu.howbachu.domain.dto.search.SearchResultResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OpinRepositoryCustom {
    /* 부모 댓글 검색 결과 */
    List<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition);
    /* 자식 댓글 검색 결과 */
    List<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition);
     /* 부모 댓글 검색 결과 - 페이징 적용 <더보기> */
    Page<SearchResultResponseDto.ParentsOpinSearchResponseDto> fetchParentOpinSearch(String condition, Pageable pageable);
    /* 자식 댓글 검색 결과 - 페이징 적용 <더보기> */
    Page<SearchResultResponseDto.ChildOpinSearchResponseDto> fetchChildOpinSearch(String condition, Pageable pageable);
    /* 자식 댓글 리스트 조회 */
    List<OpinResponseDto> fetchOpinChildList(Long parentId);
    /* 부모 댓글 단건 조회 */
    OpinResponseDto fetchParentOpin(Long opinId);
    /* 댓글 조회 */
    Page<OpinResponseDto> fetchParentOpinList(int page);
}
