package ru.practicum.shareit.item.storage.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItemId(Long itemId);
}
