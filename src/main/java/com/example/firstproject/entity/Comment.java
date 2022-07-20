package com.example.firstproject.entity;

import com.example.firstproject.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //자동생성
    private Long id;

    @ManyToOne  //다대일 관계 설정=>해당 댓글 여러개가 하나의 Article에 연관됨
    @JoinColumn(name = "article_id")  //테이블에서 연결된 대상 정보의 컬럼을 article_id로 지정=> article_id컬럼에 Article의 대표값을 저장
    private Article article;

    @Column
    private String nickname;

    @Column
    private String body;

    public static Comment createComment(CommentDto dto, Article article) {  //static: 클래스 메소드로 선언됨
        //예외 발생
        if (dto.getId() != null)
            throw new IllegalArgumentException("댓글 생성 실패. 댓글의 id가 없어야 합니다.");
        if (dto.getArticleId() != article.getId())  //url에서 받아온 articleId와 JSON에 담겨진 id가 다를 때
            throw new IllegalArgumentException("댓글 생성 실패. 게시글의 id가 잘못되었습니다");
        //엔티티 생성 및 반환
        return new Comment(
                dto.getId(),
                article,
                dto.getNickname(),
                dto.getBody()
        );
    }

    public void patch(CommentDto dto) {
        //예외 발생
        if(this.id != dto.getId())  //url에서 던진 id와 JSON에서 던진 id가 다른 경우
            throw new IllegalArgumentException("댓글 수정 실패. 잘못된 id가 입력되었습니다");
        //객체를 갱신 (예외가 발생되지 않았을 경우)
        if (dto.getNickname() != null)
            this.nickname = dto.getNickname();
        if (dto.getBody() != null)
            this.body = dto.getBody();
    }
}
