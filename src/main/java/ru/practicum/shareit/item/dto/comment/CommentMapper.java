package ru.practicum.shareit.item.dto.comment;

import ru.practicum.shareit.item.model.comment.Comment;

public class CommentMapper {
    private CommentMapper() {
        throw new IllegalStateException("Утилити класс не может иметь экземпляр");
    }

    public static CommentDto modelToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthorName())
                .created(comment.getCreated())
                .build();
    }
}
