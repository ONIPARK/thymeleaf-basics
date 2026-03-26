package com.example.thymeleaf_basics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;



/*
    TODOリポジトリ
    Spirng Data JPAによりCRUD操作が自動実施される
*/
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>{

    // 完了／未完了でフィルタリング
    List<Todo> findByCompleted(boolean completed);

    // 作成日時の降順で全件取得
    List<Todo> findAllByOrderByCreatedAtDesc();
    
    //　未完了のTODOを作成日時の昇順で取得
    List<Todo> findByCompletedFalseOrderByCreatedAtAsc();

    /*
     * チャレンジ 2: フィルタリング機能
     */
    // 未完成データの検索
    List<Todo> findByCompletedFalseOrderByCreatedAtDesc();
    // 完成データの検索
    List<Todo> findByCompletedTrueOrderByCreatedAtDesc();
}
