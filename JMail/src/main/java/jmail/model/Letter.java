package jmail.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Глеб on 11.11.2014.
 */
@Entity
@Table(name = "letters")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String body;

    @ManyToOne
    @JoinColumn(name = "to_user", referencedColumnName = "user_id")
    private User to;

    @ManyToOne
    @JoinColumn(name = "from_user", referencedColumnName = "user_id")
    private User from;

    private Date date;

    public Letter(){}

    public Letter(int id, String title, String body, User to, User from, Date date) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.to = to;
        this.from = from;
        this.date = date;
    }

    public Letter(String title, String body, User to, User from, Date date) {
        this.title = title;
        this.body = body;
        this.to = to;
        this.from = from;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Letter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", to=" + to +
                ", from=" + from +
                ", date=" + date +
                '}';
    }
}
