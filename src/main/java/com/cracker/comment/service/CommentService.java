package com.cracker.comment.service;

import com.cracker.comment.domain.Comment;
import com.cracker.comment.dto.CommentCreateRequestDto;
import com.cracker.comment.dto.CommentUpdateRequestDto;
import com.cracker.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;

@Service //실제 로직을 처리하는 부분
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
public class CommentService{

    private final CommentRepository commentRepository;

    //comment 작성
    @Transactional
    public long save(CommentCreateRequestDto commentCreateRequestDto){
        Comment comment = Comment.builder()
                .username(commentCreateRequestDto.getUserName())
                .comment(commentCreateRequestDto.getComment())
                .build();
        return commentRepository.save(comment).getId();
    }

    // comment를 지움
    @Transactional
    public Long delete(@PathVariable Long id){
        commentRepository.deleteById(id);
        return id;
    }
    // comment 업데이트
    @Transactional
    public Comment update(Long id, CommentUpdateRequestDto commentUpdateRequestDto){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("사용자가 아닙니다.")
        );
        comment.updateComment(commentUpdateRequestDto);
        return comment;
    }
}