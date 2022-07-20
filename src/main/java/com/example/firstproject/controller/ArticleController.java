package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.dto.CommentDto;
import com.example.firstproject.entity.Article;
import com.example.firstproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j  //로깅을 위한 어노테이션(System.out.println()대체)
public class ArticleController<articleEntity> {


    @Autowired  //스프링부트가 미리 생성해놓은 객체를 자동으로 연결해줌
    private com.example.firstproject.repository.articleRepository articleRepository;//객체를 사용하기 위해 필드에 ArticleRepositoty 선언

    @Autowired
    private CommentService commentService;

    @GetMapping("/articles/new")
    public String newArticleForm(){

        return("/articles/new");
    }

    //데이터 create
    @PostMapping("/articles/create")
    public String createArticle(ArticleForm form){   //데이터를 받아오려면 파라미터로 dto를 넣어줘야 함
//        System.out.println(form.toString());
        log.info(form.toString());  //로깅으로 대체

        //1.dto를 entity로 변환
        Article article = form.toEntity();  //form으로 toEntity 메소드를 호출->Article이라는 entity를 반환
//        System.out.println(article.toString());   //dto가 entity로 잘 변환 되었는지 확인
        log.info(article.toString());

        //2.Reposiotory에게 entity를 db에 저장하게 함
        Article saved = articleRepository.save(article); //repository가 article 데이터를 save하게 호출하고 save한 데이터를 Article entity로 반환
        System.out.println(saved.toString());   //article이 save되었는지 확인
        log.info(saved.toString());
       return "redirect:/articles/" + saved.getId();  //getter로 해당 id값을 가져와서 id값 변경시 해당 데이터를 볼
    }

    //상세페이지
    @GetMapping("/articles/{id}")  //id에 따른 url로 설정
    public String show(@PathVariable Long id, Model model) {    //URL요청을 파라미터로부터 받아올때는 @PathVariable사용. 입력이 되는 id라는 변수를 파라미터로 받음 (Type=Long)
        log.info("id = "+id);    //제대로 받아지는지 확인

        //1.repository를 통해 id로 데이터를 가져옴
        Article articleEntity = articleRepository.findById(id).orElse(null);
        //Article 타입의 articleEntity로 데이터를 받아옴
        //.orElse(null): 해당 id값이 없다면 null을 반환. 그래서 articleEntity라는 변수에는 값이 있거나 없으면 null이 담겨지게 됨
        List<CommentDto> commentDtos = commentService.comments(id);   //@Autowired commentService 등록 필요
        //2. 가져온 Entity 데이터를 Model에 등록 =>모델 사용하기위해 파라미터에 모델 등록.
        model.addAttribute("article",articleEntity); //article이라는 이름으로 articleEntity등록
        model.addAttribute("commentDtos",commentDtos);

        //3. 보여줄 페이지를 설정
        return "articles/show";
    }

    //데이터 목록
    @GetMapping("/articles")
    public String index(Model model) {
        //1. 모든 Article을 가져온다 => findAll():해당 Repository의 모든 데이터를 가져옴
        List<Article> articleEntityList = articleRepository.findAll(); //Article을 List에 담아서 가져옴

        //2. 가져온 Article 묶음을 뷰로 전달(Model 사용. 파라미터에 Model 추가)
        model.addAttribute("articleList",articleEntityList); //(사용할 이름, 던져줄 데이터)
        //3. 뷰 페이지 설정
        return "articles/index";
    }

    //데이터 수정 폼
    @GetMapping("/articles/{id}/edit")   //url요청 받아오기.
    public String edit(@PathVariable Long id, Model model){  //findById(id)의 변수를 parameter로 url path에서 가져온다
        //Repository를 통해 수정할 데이터를 articleEntity로 가져오기
        Article articleEntity = articleRepository.findById(id).orElse(null);   //id 변수 선언 안됨 @PathVariable로 에러 없어짐

        //모델에 데이터 등록 (parameter에 모델 등록)
        model.addAttribute("article", articleEntity); //article이름에 앞서 가져온 articleEntity 등록
        //뷰 페이지 설정
        return "articles/edit";   //수정페이지를 응답으로 반환
    }

    @PostMapping("/articles/update")  //수정시에는 @PatchMapping 사용하나 임시로 @PostMapping 사용
    public String update(ArticleForm form) {  //폼 데이터를 dto로 받음. mustache에서 id값을 추가했기때문에 dto에도 id값 추가
        log.info(form.toString());  //데이터 확인
        //데이터를 받아왔다면
        //1.DTO를 엔티티로 변환한다
        Article articleEntity = form.toEntity();  //toEntity: ArticleForm객체를 가지고 articleEntity를 리턴하는 메소드
        log.info(articleEntity.toString());

        //2.엔티티를 DB로 저장한다
        //2-1. DB에서 기존 데이터를 가져온다
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);//articleEntity의 getId로 호출해서 그 대상을 리턴받아옴
                //리턴받을 시 기본적으로 Optional type으로 반환.ex) Optional<Article> target =
                //익숙하지 않기때문에 일단 Aticle target = 사용
                //articleRepository가 id값을 통해 데이터를 가져오는데, 만약에 없다면 null을 리턴
                //target이라는 변수에는 데이터가 있으면 articleEntity가 연결됨. 없으면 target값은 null이 됨
        //2-2. 기존 데이터가 있다면 값을 갱신한다
        if (target != null) {
            articleRepository.save(articleEntity);   //save()해주면 엔티티가 db로 갱신됨
        }
        //3.수정 결과 페이지로 리다이렉트
        return "redirect:/articles/"+articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {  //id를 파라미터로 잡아줌
        log.info("삭제 요청이 들어왔습니다"); //확인용

        //1.삭제 대상을 가져온다
        Article target = articleRepository.findById(id).orElse(null);  //아이디 변수 선언 필요 @PathVariable
        log.info(target.toString());
        //2.대상을 삭제한다
        if (target != null) {
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제가 완료되었습니다.");
        }
        //3.삭제 완료시 결과 페이지로 리다이렉트
        return "redirect:/articles";
    }


}


