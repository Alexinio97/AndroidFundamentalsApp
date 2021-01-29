package model;

// quiz model for quizzes that have been done by the user
public class MyQuiz {
    private String Id;
    private String m_categoryId;
    private String m_title;
    private Double m_score;
    private Double m_questionsCount;
    private String m_userRef;

    public MyQuiz(String id,String title,Double score, Double questionsCount,String categoryId,String userRef)
    {
        Id = id;
        m_title = title;
        m_score = score;
        m_questionsCount = questionsCount;
        m_categoryId = categoryId;
        m_userRef = userRef;
    }

    public String get_Title() {
        return m_title;
    }

    public Double get_Score() {
        return m_score;
    }

    public Double get_QuestionsCount() {
        return m_questionsCount;
    }

    public String getM_categoryId() {
        return m_categoryId;
    }

    public String getM_userRef() {
        return m_userRef;
    }

    public String getId() {
        return Id;
    }
}
