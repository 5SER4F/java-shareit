package ru.practicum.shareit.item.dto.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.comment.Comment;

@UtilityClass
public class CommentMapper {

    public static CommentDto modelToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthorName())
                .created(comment.getCreated())
                .build();
    }
}
