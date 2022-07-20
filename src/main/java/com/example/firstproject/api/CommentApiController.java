package com.example.firstproject.api;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentApiController {
    @Autowired
    private CommentService commentService;

    //댓글 목록 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentDto>> comments(@PathVariable Long articleId) {    //Comment를 dto로 만들어서 리스트를 반환,CommentDTo생성해야함
        //서비스에게 위임해서 리스트 가져옴
        List<CommentDto> dtos = commentService.comments(articleId);  //<Comment>의 List를 반환. commentsService에 comments메소드 생성해야함
        //결과 응답
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
    //댓글 생성
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentDto> create(@PathVariable Long articleId, //파라미터값으로 articleId 받아옴
                                             @RequestBody CommentDto dto) { //@RequestBody로 JSON data를 CommentDto로 가져옴
        //서비스에게 위임
        CommentDto createDto = commentService.create(articleId, dto); //comment가 소속된 게시글의 id와 함께 dto던져줌. create메소드 생성 필요
        //결과 응답으로 createDto를 반환하기
        return ResponseEntity.status(HttpStatus.OK).body(createDto); //잘 반환되었을 때
        //create메소드 내에서 오류 발생시

    }

    //댓글 수정
    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable Long id, //파라미터값으로 id 받아옴
                                             @RequestBody CommentDto dto) { //@RequestBody로 JSON data를 CommentDto로 가져옴
        //서비스에게 위임
        CommentDto updatedDto = commentService.update(id, dto); //comment가 소속된 게시글의 id와 함께 dto던져줌. create메소드 생성 필요
        //결과 응답으로 updatedDto를 반환하기
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto); //잘 반환되었을 때
    }
    //댓글 삭제
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<CommentDto> delete(@PathVariable Long id) { //파라미터값으로 id 받아옴
        //서비스에게 위임
        CommentDto updatedDto = commentService.delete(id); //delete메소드 생성 필요
        //결과 응답으로 updatedDto를 반환하기
        return ResponseEntity.status(HttpStatus.OK).body(updatedDto); //잘 반환되었을 때
    }
}
