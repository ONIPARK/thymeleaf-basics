package com.example.thymeleaf_basics;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {
    
    // 登録されたユーザーを保持（DB...）
    private List<User> userList = new ArrayList<>(Arrays.asList(
        new User(1L, "田中太郎", "tanaka@example.com", "sales"),
            new User(2L, "佐藤花子", "sato@example.com", "sales"),
            new User(3L, "鈴木一郎", "suzuki@example.com", "sales")
    ));
    private Long nextId = 4L;

    // Top Page 表示
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Thymeleaf 基礎編にようこそ！");
        return "index";
    }
    
    // User 一覧を表示
    @GetMapping("/users")
    public String listUsers(Model model) {
        // Modelにデータを追加
        model.addAttribute("users", userList);
        model.addAttribute("totalCount", userList.size());

        return "users/list";
    }

    // User 登録フォームを表示
    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        // 空のフォームオブジェクトをModelに追加
        model.addAttribute("userForm", new UserForm());
        return "users/form";
    }

    // User 登録処理
    @PostMapping("/users")
    public String createUser(
        @Valid @ModelAttribute("userForm") UserForm form,
        BindingResult bindingResult,
        Model model) {
        
        // チャレンジ 1: パスワードフィールドの追加
        if (!form.getPassword().equals(form.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "パスワードが一致しません");
        }

            // バリデーションエラーがある場合、フォームを再表示
        if (bindingResult.hasErrors()) {
            return "users/form";
        }

        // 新しいユーザーを作成して保存
        User newUser = new User(nextId++, form.getName(), form.getEmail(), form.getDepartment());
        userList.add(newUser);

        // 一覧ページにリダイレクト
        return "redirect:/users";
    }

    //チャレンジ１：ユーザー詳細ページの作成
    @GetMapping("/users/{id}")
    public String showUser(@PathVariable Long id, Model model) {
        
        List<User> users = Arrays.asList(
            new User(1L, "田中太郎", "tanaka@example.com", "営業部"),
            new User(2L, "佐藤花子", "sato@example.com", "営業部"),
            new User(3L, "鈴木一郎", "suzuki@example.com", "営業部")
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
    
    /**
     * ユーティリティデモページを表示
     */
    @GetMapping("/demo")
    public String showDemo(Model model) {
        //現在日時
        LocalDateTime now = LocalDateTime.now();
        model.addAttribute("now", now);

        //チャレンジ 1: 相対時間表示
        LocalDateTime createAt = now.minusHours(3);
        model.addAttribute("createAt", createAt);

        // long hours = Duration.between(createAt, now).toHours();

        // String relativeTime;
        // if (hours < 24) {
        //     relativeTime = hours + "時間前";
        // } else if (hours < 48) {
        //     relativeTime = "昨日";
        // } else {
        //     relativeTime = createAt.toLocalDate().toString();
        // }

        // model.addAttribute("relativeTime", relativeTime);

        //誕生日
        model.addAttribute("birthday", LocalDate.of(1990, 5, 15));
        //サンプル文字例
        model.addAttribute("longText", "これは非常に長いテキストのサンプルです。Thymeleafのユーティリティ機能を使って切り詰めることができます。");
        //サンプル数値
        model.addAttribute("price", 1234567);
        model.addAttribute("rate", 0.1234);
        //サンプルリスト
        model.addAttribute("items", Arrays.asList("りんご", "みかん", "バナナ"));

        //チャレンジ 3: 動的CSSクラス
        model.addAttribute("status", "success");
        return "demo";
    }
    
    
}
