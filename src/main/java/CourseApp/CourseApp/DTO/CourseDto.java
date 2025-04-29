package CourseApp.CourseApp.DTO;

public class CourseDto {
    private Long id;
    private String name;
    private String description;
    private int credit;
    private AuthorDto author;

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    // Constructors
    public CourseDto() {}

    // Getters & Setters (you can use Lombok if you prefer)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }


}
