package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import com.example.firstproject.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //JPA와 연동한 테스트
class CommentRepositoryTest {

    @Autowired CommentRepository commentRepository;

    @Test
    @DisplayName("특정 게시글의 모든 댓글 조회")
    void findByArticleId() {
        /* CASE1: 4번 게시글의 모든 댓글 조회*/
        {
            //입력 데이터 준비
            Long articleId = 4L;

            //실제 수행
            List<Comment> comments = commentRepository.findByArticleId(articleId);

            //예상
            Article article = new Article(4L, "인생영화는?", "댓글 ㄱ");
            Comment a = new Comment(1L, article, "Park", "굳 윌 헌팅");
            Comment b = new Comment(2L, article, "Kim", "아이 엠 샘");
            Comment c = new Comment(3L, article, "Choi", "쇼생크의 탈출");
            List <Comment> expected = Arrays.asList(a,b,c);


            //검증
            assertEquals(expected.toString(), comments.toString(),"4번 글의 모든 댓글을 출력");
        }
        /* Case 2: 1번 게시글의 모든 댓글 조회 */
        {
            // 준비
            Long articleId = 1L;
            // 수행
            List<Comment> comments = commentRepository.findByArticleId(articleId);
            // 예상
            Article article = new Article(1L, "가가가가", "1111");
            List<Comment> expected = Arrays.asList();
            // 검증
            assertEquals(expected.toString(), comments.toString(), "1번 글은 댓글이 없음");
        }

    }

    @Test
    @DisplayName("특정 닉네임의 모든 댓글을 조회")
    void findByNickname() {
        /* CASE1: "Park"의 모든 댓글 조회 */
        {
            //입력 데이터 준비
            String nickname = "Park";
            //실제 수행
            List<Comment> comments = commentRepository.findByNickname(nickname);
            //예상
            Comment a = new Comment(1L, new Article(4L, "인생영화는?", "댓글 ㄱ"), nickname, "굳 윌 헌팅");
            Comment b = new Comment(4L, new Article(5L, "소울 푸드는?", "댓글 ㄱㄱ"), nickname, "치킨");
            Comment c = new Comment(7L, new Article(6L, "취미는?", "댓글 ㄱㄱㄱ"), nickname, "조깅");
            List<Comment> expected = Arrays.asList(a, b, c);

            //검증
            assertEquals(expected.toString(), comments.toString(),"Park의 모든 댓글을 출력");
        }
    }
}