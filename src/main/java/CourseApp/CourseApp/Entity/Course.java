package CourseApp.CourseApp.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private int credit;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Assessment assessment;

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Course() {}

    public Course(Long id,String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }





    public double getCredit() {
        return credit;
    }

    public void setCredit(int price) {
        this.credit = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
