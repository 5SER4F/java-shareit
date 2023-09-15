package ru.practicum.shareit.item.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private Timestamp created = Timestamp.from(Instant.now());

    public CommentDto() {
    }
}
