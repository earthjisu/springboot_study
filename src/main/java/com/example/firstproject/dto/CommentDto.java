package com.example.firstproject.dto;

import com.example.firstproject.entity.Comment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class CommentDto {
    private Long id;  //comment의 id
    @JsonProperty("article_id")  //JSON에서 데이터를 받아올 때는 @JSONPrperties("JSON데이터")로 선언하면, 자동으로 매핑시켜줌
    private Long articleId;  //comment가 포함된 게시글의 id
    private String nickname;
    private String body;

    public static CommentDto createCommentDto(Comment comment) {  //static: 클래스 메소드에 선언될때 사용됨
        return new CommentDto(
                comment.getId(),
                comment.getArticle().getId(),   //게시글의 아이디도 같이 넣어줌
                comment.getNickname(),
                comment.getBody()
        );
    }
}
