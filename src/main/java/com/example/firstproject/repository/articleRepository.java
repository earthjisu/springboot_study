package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface articleRepository extends CrudRepository<Article,Long> {  //<관리 대상 entity,entity에서 id의 타입>


    @Override
    ArrayList<Article> findAll();
}
