package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start", nullable = false)
    private Timestamp start;

    @Column(name = "ending", nullable = false)
    private Timestamp end;

    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    public Booking() {
    }

}
