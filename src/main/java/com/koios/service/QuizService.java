package com.koios.service;

import com.koios.model.Question;
import com.koios.model.Result;
import com.koios.repository.QuestionRepo;
import com.koios.repository.ResultRepo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuizService {
    private final QuestionRepo questionRepo;
    private final ResultRepo resultRepo;

    public QuizService(QuestionRepo questionRepo, ResultRepo resultRepo) {
        this.questionRepo = questionRepo;
        this.resultRepo = resultRepo;
    }

    Random random = new Random();

    public List<Question> getQuestions() {
        List<Question> allQuestions = questionRepo.findAll();
        List<Question> questionList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(allQuestions.size());
            questionList.add(allQuestions.get(randomNumber));
            allQuestions.remove(randomNumber);
        }

        return questionList;
    }

    public int getResult(List<Question> questionList) {
        int correct = 0;

        for (Question question : questionList) {
            if (question.getAns() == question.getChose()) {
                correct++;
            }
        }

        return correct;
    }

    public void saveResult(Result result) {
        resultRepo.save(result);
    }

    public List<Result> getTopScore() {
        return resultRepo.findAll(Sort.by("totalCorrect").descending());
    }
}
