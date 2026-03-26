package com.example.thymeleaf_basics;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/todos")
public class TodoController {
    
    private final TodoRepository todoReponsitory;

    //コンストラクトインジェクトション
    public TodoController(TodoRepository todoResponsitory) {
        this.todoReponsitory = todoResponsitory;
    }

    /**
     * TODO一覧を表示
     */
    // @GetMapping
    // public String list(Model model) {
    //     List<Todo> todos = todoReponsitory.findAllByOrderByCreatedAtDesc();
    //     long completedCount = todos.stream().filter(Todo::isCompleted).count();
    //     long incompleteCount = todos.size() - completedCount;

    //     model.addAttribute("todos", todos);
    //     model.addAttribute("totalCount", todos.size());
    //     model.addAttribute("completedCount", completedCount);
    //     model.addAttribute("incompleteCount", incompleteCount);

    //     return "todos/list";
    // }

    // チャレンジ 2: フィルタリング機能
    @GetMapping
    public String list(@RequestParam(name = "filter", required = false, defaultValue = "all") String filter,
                       Model model) {
        List<Todo> todos;
        
        if ("incomplete".equals(filter)) {
            todos = todoReponsitory.findByCompletedFalseOrderByCreatedAtDesc();
        } else if ("complete".equals(filter)) {
            todos = todoReponsitory.findByCompletedTrueOrderByCreatedAtDesc();
        } else {
            todos = todoReponsitory.findAllByOrderByCreatedAtDesc();
        }
        
        long completedCount = todos.stream().filter(Todo::isCompleted).count();
        long incompleteCount = todos.size() - completedCount;

        model.addAttribute("todos", todos);
        model.addAttribute("totalCount", todos.size());
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("incompleteCount", incompleteCount);

        return "todos/list";
    }


    /*
     * TODO新規作成フォームを表示 
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("todoForm", new TodoForm());
        return "todos/form";
    }

    /*
     * TODOを新規作成 
     */
    @PostMapping
    public String create(
        @Valid @ModelAttribute("todoForm") TodoForm form,
        BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            return "todos/form";
        }

        Todo todo = new Todo();
        todo.setTitle(form.getTitle());
        todo.setDescription(form.getDescription());
        todoReponsitory.save(todo);

        return "redirect:/todos";
    }

    // チャレンジ 1: 編集機能の追加
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Todo todo = todoReponsitory.findById(id).orElse(null);

        TodoForm form = new TodoForm();
        form.setTitle(todo.getTitle());
        form.setDescription(todo.getDescription());

        model.addAttribute("todoForm", form);
        model.addAttribute("todoId", id);

        return "todos/edit";
        
    }
    
    @PostMapping("/{id}")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("todoForm") TodoForm form,
        BindingResult bindingResult,
        Model model) {
            
            if (bindingResult.hasErrors()) {
                model.addAttribute("todoId", id);
                return "todos/edit";
            }

            Todo todo = todoReponsitory.findById(id).orElse(null);

            todo.setTitle(form.getTitle());
            todo.setDescription(form.getDescription());

            todoReponsitory.save(todo);
        return "redirect:/todos";
    }
    
    
    /*
     * TODOの完了状態を切り替え 
     */
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        todoReponsitory.findById(id).ifPresent(todo -> {
            todo.setCompleted(!todo.isCompleted());
            todoReponsitory.save(todo);
        });
        return "redirect:/todos";
    }

    /*
     * TODOを削除 
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        todoReponsitory.deleteById(id);
        return "redirect:/todos";
    }

}
