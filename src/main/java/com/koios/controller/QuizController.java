package com.koios.controller;

import com.koios.model.QuestionForm;
import com.koios.model.Result;
import com.koios.service.QuizService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class QuizController {
    private final QuizService quizService;
    private boolean submitted;
    private Result result;
    private final QuestionForm questionFormForView;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
        submitted = false;
        questionFormForView = new QuestionForm();
    }

    @GetMapping("/")
    public String getHomePage() {
        return "HomePage";
    }

    @PostMapping("/quiz")
    public String quiz(@RequestParam String username, RedirectAttributes redirectAttributes, Model model) {
        if (username.isEmpty()) {
           redirectAttributes.addFlashAttribute("warning", "You must enter your name");
           return "redirect:/";
        }
        submitted = false;
        result = new Result();
        result.setUsername(username);
        QuestionForm questionForm = new QuestionForm();
        questionForm.setQuestions(quizService.getQuestions());
        model.addAttribute("questionForm", questionForm);
        return "QuizPage";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute QuestionForm questionForm, Model model) {
        if(!submitted) {
            questionFormForView.setQuestions(questionForm.getQuestions());
            int totalCorrect = quizService.getResult(questionForm.getQuestions());
            result.setTotalCorrect(totalCorrect);
            quizService.saveResult(result);
            submitted = true;
        }
        model.addAttribute("result", result);
        return "ResultPage";
    }

    @GetMapping("/result")
    public String getResultPage(Model model) {
        model.addAttribute("result", result);
        return "ResultPage";
    }

    @GetMapping("/answer")
    public String myResult(Model model) {
        model.addAttribute("questionForm", questionFormForView);
        return "AnswerPage";
    }

    @GetMapping("/score")
    public String Score(Model model) {
        List<Result> resultLists = quizService.getTopScore();
        model.addAttribute("resultLists", resultLists);
        return "ScorePage";
    }
}
