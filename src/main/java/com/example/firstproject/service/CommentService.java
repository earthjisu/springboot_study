package com.example.firstproject.service;

import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import com.example.firstproject.repository.CommentRepository;
import com.example.firstproject.repository.articleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private articleRepository articleRepository; //article데이터도 db에서 가져와야함


    public List<CommentDto> comments(Long articleId) {
        // 조회: 댓글 목록
//        List<Comment> comments = commentRepository.findByArticleId(articleId);
        // 변환: 엔티티 -> DTO
//        List<CommentDto> dtos = new ArrayList<CommentDto>();
//            //비어있는 dtos(arrayList)에 하나씩 댓글을 변환해서 추가
//        for(int i = 0; i <comments.size(); i++) {
//            Comment c = comments.get(i);
//            CommentDto dto = CommentDto.createCommentDto(c);  //createCommentDto를 CommentDto에 생성
//            dtos.add(dto);   //commets에 있는 값을 하나씩 꺼내서 변환 후 add
//        }

        // 반환:
        //for반복문을 stream으로 개선
        return commentRepository.findByArticleId(articleId)  //repository에서 articleId가져오기 comment엔티티의 리스트를 리턴
                .stream()  //stream으로 바꿔서 하나씩 꺼냄
                .map(comment -> CommentDto.createCommentDto(comment))  //map() 메소드를 통해 꺼내진 comment를 생성메소드인 createCommentDto를 통해 CommentDto로 변환.
                .collect(Collectors.toList()); //리스트로 묶어줌
    }

    @Transactional  //create는 db의 변경이 일어날 수 있기 때문에 트랜잭션 처리 필요
    public CommentDto create(Long articleId, CommentDto dto) {
        // 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId) //articleRepository로 게시글 가져옴
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패. 대상 게시글이 없습니다."));   //orElseThow: 만약에 없다면 예외를 발생시킴
        // 댓글 엔티티 생성 (예외가 발생하지 않고 article을 찾았을 때)
        Comment comment = Comment.createComment(dto,article); //두 개의 데이터를 넣었을 때 Comment로 받아옴
        // 생성된 엔티티 db로 저장
        Comment created = commentRepository.save(comment);
        // DTO로 변경하여 반환
        return CommentDto.createCommentDto(created);
    }

    @Transactional //데이터를 변화시키기때문에 트랜잭션 필요
    public CommentDto update(Long id, CommentDto dto) {
        // 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패. 대상 댓글이 없습니다.")); //없다면 예외 발생
        // 댓글 수정 (조회 성공시)
        target.patch(dto);  //JSON에서 받아온 dto를 입력값으로 받아서 수정. Comment 엔티티에 patch메소드 생성 필요
        // 수정된 댓글 db로 갱신
        Comment updated = commentRepository.save(target);  //commentRepository가 target을 save하고 comment를 리턴
        //댓글 엔티티를 dto로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional  //잘못된 경우 db롤백
    public CommentDto delete(Long id) {
        //댓글 조회 및 예외 발생
        Comment target =  commentRepository.findById(id)  //정상적이라면 target이 되는 Comment가 리턴
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패. 대상이 없습니다."));
        //db에서 댓글 삭제
        commentRepository.delete(target);  //반환값 없음
        //삭제 댓글을 dto로 반환
        return CommentDto.createCommentDto(target);  //CommentDto의 생성메소드에서 반환
    }
}
