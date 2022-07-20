package com.example.firstproject.api;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.articleRepository;
import com.example.firstproject.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController  //RestaAPI용 컨트롤러. 데이터(JSON)를 반환
public class ArticleApiController {

//    @Autowired //DI(Dependency Injection)
//    private articleRepository articleRepository;

      @Autowired  //DI(Dependency Injection)
      private ArticleService articleService;
//
//    //GET
//    @GetMapping("/api/articles")
//    public List<Article> index() {
//        return articleRepository.findAll();
//    }

    //GET
    @GetMapping("/api/articles")
    public List<Article> index() {
        return articleService.index(); //기존에는 repository를 통해 데이터를 가져옴->service를 통해 가져오기
    }                                   //service에 index메소드 추가

//
//    @GetMapping("/api/articles/{id}") //단일 Article반환
//    public Article index(@PathVariable Long id) {  //url요청을 통해서 id값을 가져올 경우 @PathVariable 사용
//        return articleRepository.findById(id).orElse(null);
//    }

        @GetMapping("/api/articles/{id}") //단일 Article반환
    public Article show(@PathVariable Long id) {

        return articleService.show(id);  // service에 show 메소드 추가
    }

//    //POST
//    @PostMapping("/api/articles")
//    public @ResponseEntity<Article> create(@RequestBody ArticleForm dto){  //dto로 데이터 받아와줌.@RequestBody: JSON데이터 받기
//        log.info(dto.toString());
//        Article saved = articleRepository.save(dto.toEntity());
//        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//    }

    //POST
    @PostMapping("/api/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleForm dto){  //dto로 데이터 받아와줌.@RequestBody: JSON데이터 받기
        Article created = articleService.create(dto); //service를 통해 create
        return (created != null) ?  //삼항연산자
                ResponseEntity.status(HttpStatus.OK).body(created):   //created가 null이 아니면 body에 데이터를 넣어서 보내줌
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();  //그렇지 않으면 몸통 없이 빌드해서 보내줌
    }
//
//    //PATCH
//    @PatchMapping("/api/articles/{id}")
//    public ResponseEntity<Article> update(@PathVariable Long id,
//                                          @RequestBody ArticleForm dto) {
//        //1. 수정용 엔티티 생성
//        Article article = dto.toEntity();
//        log.info("id: {}, article: {}", id, article.toString()); //id는 첫번째 {}로, article.toString()는 두번째 {}로 들어감
//        //log오류는 @Slf4j로 해결
//
//        //2. 대상 엔티티 조회
//        Article target = articleRepository.findById(id).orElse(null);
//
//        //3. 잘못된 요청 처리(대상이 없거나, id가 다른 경우)
//        if (target == null || id != article.getId()) {
//            //400, 잘못된 요청 응답
//            log.info("잘못된 요청 id: {}, article: {}", id, article.toString()); //id와 article을 확인할 수 있게 해줌
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);        //reponse응답의 400번을 리턴해줘야 함  => ResponseEntity<Article> 상태코드를 같이 실어서 보낼 수 있음
//        }
//
//        //4. 업데이트 및 정상 응답(200)
//        target.patch(article);//데이터를 일부만 보낸경우 null로 날아감. 보강코드 추가작성.patch 메소드를 Article엔티티에 추가
//        Article update = articleRepository.save(target);
//        return ResponseEntity.status(HttpStatus.OK).body(update);  //ResponsEntity에 <Aricle>데이터가 담겨서 JSON으로 반환됨
//
//    }

    //PATCH
    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable Long id,
                                          @RequestBody ArticleForm dto) {
        Article updated = articleService.update(id,dto);  //업데이트시 파라미터로 받아왔던 id와 dto를 던져줌. Article을 업데이트 된것으로 받아옴
        return (updated != null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

//
//    //DELETE
//    @DeleteMapping("/api/articles/{id}")
//    public ResponseEntity<Article> delete(@PathVariable Long id) {
//        //1. 대상 찾기
//        Article target = articleRepository.findById(id).orElse(null);
//        //잘못된 요청 처리
//       if (target == null) {
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//       }
//
//       //2. 대상 삭제
//       articleRepository.delete(target);
//       return ResponseEntity.status(HttpStatus.OK).build();
//   }

    //DELETE
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable Long id) {
        Article deleted = articleService.delete(id);
       return (deleted != null) ?
               ResponseEntity.status(HttpStatus.OK).build() : //삭제된 것 잘 반환시
               ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //삭제가 잘못 되었을 때
   }
   //트랜잭션->실패->롤백
    @PostMapping("/api/transaction-test")
    public ResponseEntity<List<Article>> transactionTest(@RequestBody List<ArticleForm> dtos) {
        List<Article> createdList = articleService.createArticles(dtos);  //createArticles에 파라미터로 받아온 dtos를 던져줌.service에 createArticles 메소드를 생성해야함
        return (createdList != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdList) : //createdList가 잘 반환되었다면 body에 createList를 던져줌
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //아니라면 내용없이 build해서 보냄
    }
}
