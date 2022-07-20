package com.example.firstproject.service;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.articleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service        //서비스 선언.(서비스 객체를 스프링 부트에 생성(등록)
public class ArticleService {
    @Autowired   //DI, 생성 객체를 가져와 연결
    private articleRepository articleRepository;

    public List<Article> index() {            //index 메소드 생성
        return articleRepository.findAll();   //데이터는 repository에서 가져와서 리턴
    }

    public Article show(Long id) {
       return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleForm dto) {
        Article article = dto.toEntity();  //dto를 엔티티로 바꿔서 Article로 받아옴
        if (article.getId() != null) {   //원래 있던 id에 create을 할 때 데이터가 수정되는것 방지
            return null;
        }
        return articleRepository.save(article);       //article받은 것을 DB에 저장
    }

    public Article update(Long id, ArticleForm dto) {
        //1. 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info("id: {}, article: {}", id, article.toString()); //log 에러: @Slf4j 애노테이션 추가
        //log오류는 @Slf4j로 해결

        //2. 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리(대상이 없거나, id가 다른 경우)
        if (target == null || id != article.getId()) {
            //400, 잘못된 요청 응답
            log.info("잘못된 요청 id: {}, article: {}", id, article.toString()); //id와 article을 확인할 수 있게 해줌
            return null;
        }

        //4. 업데이트 (응답 필요없이 업데이트만)
        target.patch(article);
        Article updated = articleRepository.save(target);  //patch된 데이터를 save해서 반환
        return updated;
    }

    public Article delete(Long id) {
        //1. 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);

        //잘못된 요청 처리
       if (target == null) {
           return null;    //잘못된 요청일 경우 ResponseEntity를 반환하지 않음-> 서비스가 컨트롤러의 역할을 할 필요 없음. null을 반환
       }

       //2. 대상 삭제
       articleRepository.delete(target);
       return target;   //null이 아니면 삭제하면서 삭제된 대상을 리턴
    }

    @Transactional //해당 메소드를 트랜잭션으로 묶음. 메소드 실패시 이전상태로 롤백
    public List<Article> createArticles(List<ArticleForm> dtos) {
        //1. dto묶음을 엔티티묶음으로 변환
        List<Article> articleList = dtos.stream()
                .map(dto -> dto.toEntity())//맵으로 하나하나의 dto가 올때마다 dto.toEntity수행->매핑시켜줌
                .collect(Collectors.toList()); //매핑된것을 리스트로 변환

//            //for문으로 작성시
//                List<Article> articleList = new ArrayList<>();
//                fot (int i = 0; i < dtos.size(); i++){
//                    ArticleForm dto = dtos.get(i);
//                    Article entity = dto.toEntity();
//                    articleList.add(entity);
//                 }

        //2. 엔티티 묶음을 db로 저장
        articleList.stream()
                .forEach(article -> articleRepository.save(article)); //하나의 article이 올때마다 리파지토리를 통해 save

//            //for문으로 작성시
//            for (int i = 0; i < articleRepository.size(); i++)
//            {Article article = articleList.get(i);
//             articleRepository.save(article);
//            }
        //3. 저장된 상태에서 강제로 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패")
        );

        //결과값 반환
        return articleList;
    }
}
