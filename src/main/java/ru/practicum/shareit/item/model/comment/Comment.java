package ru.practicum.shareit.item.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    private Long authorId;

    @Column(name = "text", nullable = false)
    private String text;

    @Formula("(SELECT u.name FROM users u WHERE u.id = user_id)")
    private String authorName;

    @Column(name = "created", nullable = false)
    private Timestamp created;
}
