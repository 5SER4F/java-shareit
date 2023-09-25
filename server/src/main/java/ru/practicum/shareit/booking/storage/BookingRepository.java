package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    String QUERY_BY_ITEM_OWNER = "SELECT b " +
            "FROM Booking AS b " +
            "JOIN FETCH b.item " +
            "JOIN FETCH b.booker " +
            "WHERE b.item.ownerId=?1 " +
            "ORDER BY b.start DESC ";

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query(QUERY_BY_ITEM_OWNER)
    List<Booking> findByItemOwner(Long itemOwnerId);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long itemOwnerId, Pageable pageable);

    boolean existsByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, Timestamp date);

}
