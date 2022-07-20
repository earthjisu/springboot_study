package com.example.firstproject.dto;

import com.example.firstproject.entity.Article;
import lombok.*;

@AllArgsConstructor  //lombok-생성자 호출
@NoArgsConstructor
@Setter
@ToString     //lombok-toString 메소드 자동 작성
public class ArticleForm {

    private Long id;  //id 필드 추가
    public String title;  //html 태그 내의 name과 동일하게
    public String content;

//    public ArticleForm(String title, String content) {    //생성자 추가
//        this.title = title;
//        this.content = content;
//    }

//    @Override
//    public String toString() {     //데이터가 잘 받아졌는지 확인하기 위함함
//       return "ArticleForm{" +
//                "title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//    }

    public Article toEntity() {  //toEntity 메소드를 만들어줌
        return new Article(id,title,content); //새롭게 Article을 만들어서 리턴.Entity class에 객체 생성을 위해 생성자를 호출
    }                                         //원래는 id 필드가 없어 생성자를 null로 호출했으나 id필드의 추가로 toEntity()메소드의 코드 변경
}
