package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.ownerId=?1 " +
            "ORDER BY b.start DESC "
    )
    List<Booking> findByItemOwner(Long itemOwnerId);

    boolean existsByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, Timestamp date);

}
