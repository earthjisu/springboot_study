package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  //해당 클래스는 스프링부트와 연결되어 테스팅
class ArticleServiceTest {

    @Autowired ArticleService articleService;

    @Test
    void index() {
        //예상
        Article a =  new Article(1L,"가가가가","1111");
        Article b =  new Article(2L,"나나나나","2222");
        Article c =  new Article(3L,"다다다다","3333");
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a, b, c));

        //실제
        List<Article> articles = articleService.index();

        //비교
        assertEquals(expected.toString(), articles.toString());  //예상 데이터와 실제를 비교
    }

    @Test
    void show_성공___존재하는_id_입력() {
        //예상
        Long id = 1L;
        Article expected = new Article(id, "가가가가", "1111");
        //실제
        Article article = articleService.show(id);
        //비교
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    void show_실패__존재하지_않는_id_입력() {
        //예상
        Long id = -1L;
        Article expected = null;
        //실제
        Article article = articleService.show(id);
        //비교
        assertEquals(expected, article);  //null은 toString()메소드 사용안됨

    }

    @Transactional
    @Test
    void create__성공__title과_content만_있는_dto_입력() {
        //예상
        String title = "라라라라";
        String content = "44444";
        ArticleForm dto = new ArticleForm(null, title, content);
        Article expected = new Article(4L, title, content);
        //실제
        Article article = articleService.create(dto);
        //비교
        assertEquals(expected.toString(), article.toString());  //null은 toString()메소드 사용안됨
    }

    @Test
    @Transactional  //**데이터가 조회가 아닌 생성, 변경, 삭제될 수 있는 경우에는 트랜잭션으로 묶어서 롤백할 수 있게 해줌
    void create__실패___id가_포함된_dto_입력() {
        //예상
        String title = "라라라라";
        String content = "사사사사";
        ArticleForm dto = new ArticleForm(4L, title, content);
        Article expected = null;
        //실제
        Article article = articleService.create(dto);
        //비교
        assertEquals(expected, article);  //null은 toString()메소드 사용안됨
    }
}