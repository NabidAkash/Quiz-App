package com.koios.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QuestionForm {
    List<Question> questions;
}
