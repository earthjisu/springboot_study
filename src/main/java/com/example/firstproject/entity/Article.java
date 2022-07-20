package com.example.firstproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor  //lombok - 생성자 자동 생성
@NoArgsConstructor  //lombok - default생성자(파라미터가 없는 생성자)를 추가
@ToString    //lombok toString 메소드 생성
@Getter
@Entity        //db가 해당 객체를 인식 가능(해당 클래스로 테이블 생성함)
public class Article {

    @Id  //entity에 대표값을 넣어줌
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // DB가 id를 자동 생성 어노테이션
    private Long id;

    @Column     //db에서 이해할 수 있게 @Column 어노테이션 붙여줌
    private String title;
    @Column
    private String content;

    public void patch(Article article) {
        if (article.title != null)   //article의 title이 null이 아니면 this.title에 넣어줌 null값을 던져도 기존 데이터가 유지됨
            this.title = article.title;
        if (article.content != null)  //article의 content가 null이 아니면 this.content에 넣어줌
            this.content = article.content;
    }
//    public Long getId() {
//        return id;
//    }


//    public Article(Long id, String title, String content) {  //생성자 추가 =>lombok 대체
//        this.id = id;
//        this.title = title;
//        this.content = content;
//    }

//    @Override
//    public String toString() {   //데이터 확인용 =>lombok 대체
//        return "Article{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//    }

//    Article() {  //default생성자 추가 =>lombok 대체
//
//    }
}


