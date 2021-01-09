package model;

public class Category {
    private String Id;
    private String categoryName;
    private double categoryQuizCount;

    public Category(String id, String categoryName, double categoryQuizCount) {
        Id = id;
        this.categoryName = categoryName;
        this.categoryQuizCount = categoryQuizCount;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryQuizCount(double categoryQuizCount) {
        this.categoryQuizCount = categoryQuizCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getCategoryQuizCount() {
        return categoryQuizCount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
