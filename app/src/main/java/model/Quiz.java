package model;


import javax.annotation.Nullable;

public class Quiz {
    private String Id;
    private Double questionsCount;
    private String title;
    private String difficulty;
    // constructor for receiving a quiz
    public Quiz(String Id, String title, Double questionsCount, String difficulty) {
        this.Id = Id;
        this.questionsCount = questionsCount;
        this.title = title;
        this.difficulty = difficulty;
    }
    // constructor for creating a new quiz
    public Quiz( String title, Double questionsCount, String difficulty) {
        this.questionsCount = questionsCount;
        this.title = title;
        this.difficulty = difficulty;
    }

    public String getId() {
        return Id;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Double getQuestionsCount() {
        return questionsCount;
    }

    public String getTitle() {
        return title;
    }

}
