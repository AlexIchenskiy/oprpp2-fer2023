package hr.fer.oprpp2.model;

import javax.persistence.*;
import java.util.List;

/**
 * Blog user model class.
 */
@Entity
@Table(name="blog_users")
public class BlogUser {

    private long id;

    private String firstName;

    private String lastName;

    private String nick;

    private String email;

    private String passwordHash;

    private List<BlogEntry> blogEntries;

    public BlogUser() {
    }

    public BlogUser(String firstName, String lastName, String nick, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nick = nick;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Id @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(nullable = false, unique = true)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Column(nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @OneToMany(mappedBy = "creator", fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
    public List<BlogEntry> getBlogEntries() {
        return blogEntries;
    }

    public void setBlogEntries(List<BlogEntry> blogEntries) {
        this.blogEntries = blogEntries;
    }

}
