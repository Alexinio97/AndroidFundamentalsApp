package model;


public class Quiz {
    private String Id;
    private Double questionsCount;
    private String title;
    private String difficulty;
    // the nickname of the user that created it
    private String madeBy;
    // user id from firestore
    private String userRef;

    // constructor for receiving with ID
    public Quiz(String Id, String title, Double questionsCount, String difficulty,String madeBy,String userRef) {
        this.Id = Id;
        this.questionsCount = questionsCount;
        this.title = title;
        this.difficulty = difficulty;
        this.madeBy = madeBy;
        this.userRef = userRef;
    }

    // constructor for creating a new quiz
    public Quiz( String title, Double questionsCount, String difficulty,String madeBy,String userRef) {
        this.questionsCount = questionsCount;
        this.title = title;
        this.difficulty = difficulty;
        this.madeBy = madeBy;
        this.userRef = userRef;
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

    public String getMadeBy() {
        return madeBy;
    }

    public String getUserRef() {
        return userRef;
    }
}
