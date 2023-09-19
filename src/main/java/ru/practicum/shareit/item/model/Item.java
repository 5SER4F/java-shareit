package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.comment.Comment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "user_id", nullable = false)
    private Long ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "available")
    private Boolean available;

    @Transient
    private Booking lastBooking;

    @Transient
    private Booking nextBooking;

    @Transient
    private List<Comment> comments;

    public Boolean isAvailable() {
        return available;
    }
}
