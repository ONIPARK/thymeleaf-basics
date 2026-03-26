package com.example.thymeleaf_basics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
    TODO登録•編集用フォーム
*/
public class TodoForm {
    
    @NotBlank(message = "タイトルは必須です")
    @Size(max = 200, message = "タイトルは200文字以内で入力してください")
    private String title;

    @Size(max = 500, message = "説明は500文字以内で入力してください")
    private String description;

    //　デフォルトコンストラクト
    public TodoForm() {
    }

    // Getter/Setter
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
