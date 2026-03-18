package com.example.thymeleaf_basics;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;




@Controller
public class UserController {
    
    // Top Page 表示
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Thymeleaf 基礎編にようこそ！");
        return "index";
    }
    
    // User 一覧を表示
    @GetMapping("/users")
    public String listUsers(Model model) {
        // SampleData 作成
        List<User> users = Arrays.asList(
            new User(1L, "田中太郎", "tanaka@example.com"),
            new User(2L, "佐藤花子", "sato@example.com"),
            new User(3L, "鈴木一郎", "suzuki@example.com")
        );

        // Modelにデータを追加
        model.addAttribute("users", users);
        model.addAttribute("totalCount", users.size());

        return "users/list";
    }

    //チャレンジ１：ユーザー詳細ページの作成
    @GetMapping("/users/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        
        List<User> users = Arrays.asList(
            new User(1L, "田中太郎", "tanaka@example.com"),
            new User(2L, "佐藤花子", "sato@example.com"),
            new User(3L, "鈴木一郎", "suzuki@example.com")
        );

        User foundUser = null;

        for (User user : users) {
            if (user.getId().equals(id)) {
                foundUser = user;
                break;
            }
        }
        model.addAttribute("user", foundUser);
        return "users/detail";
    }
    
    
}
