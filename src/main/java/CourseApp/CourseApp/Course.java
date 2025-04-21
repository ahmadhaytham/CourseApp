package CourseApp.CourseApp;

import jakarta.persistence.*;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String description;
    private int credit;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL)
    private Rating rating;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id", referencedColumnName = "id")
    private Assessment assessment;

    public Course() {}

    public Course(int id,String name, String description, Rating rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rating = rating;

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

    public Rating getRating() {
        return rating;
    }



    public double getCredit() {
        return credit;
    }

    public void setCredit(int price) {
        this.credit = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
