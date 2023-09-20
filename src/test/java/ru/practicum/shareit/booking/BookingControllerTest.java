package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {BookingController.class})
@ExtendWith(SpringExtension.class)
class BookingControllerTest {
    @Autowired
    private BookingController bookingController;

    @MockBean
    private BookingService bookingService;

    @Test
    void testApproveBooking() throws Exception {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setName("Name");
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setId(1L);
        user1.setName("Name");

        Booking booking = new Booking();
        booking.setBooker(new User());
        booking.setEnd(mock(Timestamp.class));
        booking.setId(1L);
        booking.setItem(new Item());
        booking.setStart(mock(Timestamp.class));
        booking.setStatus(Status.WAITING);

        Booking booking1 = new Booking();
        booking1.setBooker(new User());
        booking1.setEnd(mock(Timestamp.class));
        booking1.setId(1L);
        booking1.setItem(new Item());
        booking1.setStart(mock(Timestamp.class));
        booking1.setStatus(Status.WAITING);

        Item item = new Item();
        item.setAvailable(true);
        item.setComments(new ArrayList<>());
        item.setDescription("something");
        item.setId(1L);
        item.setLastBooking(booking);
        item.setName("Name");
        item.setNextBooking(booking1);
        item.setOwnerId(1L);
        item.setRequestId(1L);

        Booking booking2 = new Booking();
        booking2.setBooker(user1);
        booking2.setEnd(mock(Timestamp.class));
        booking2.setId(1L);
        booking2.setItem(item);
        booking2.setStart(mock(Timestamp.class));
        booking2.setStatus(Status.WAITING);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setName("Name");

        Booking booking3 = new Booking();
        booking3.setBooker(new User());
        booking3.setEnd(mock(Timestamp.class));
        booking3.setId(1L);
        booking3.setItem(new Item());
        booking3.setStart(mock(Timestamp.class));
        booking3.setStatus(Status.WAITING);

        Booking booking4 = new Booking();
        booking4.setBooker(new User());
        booking4.setEnd(mock(Timestamp.class));
        booking4.setId(1L);
        booking4.setItem(new Item());
        booking4.setStart(mock(Timestamp.class));
        booking4.setStatus(Status.WAITING);

        Item item1 = new Item();
        item1.setAvailable(true);
        item1.setComments(new ArrayList<>());
        item1.setDescription("something");
        item1.setId(1L);
        item1.setLastBooking(booking3);
        item1.setName("Name");
        item1.setNextBooking(booking4);
        item1.setOwnerId(1L);
        item1.setRequestId(1L);

        Booking booking5 = new Booking();
        booking5.setBooker(user2);
        booking5.setEnd(mock(Timestamp.class));
        booking5.setId(1L);
        booking5.setItem(item1);
        booking5.setStart(mock(Timestamp.class));
        booking5.setStatus(Status.WAITING);

        Item item2 = new Item();
        item2.setAvailable(true);
        item2.setComments(new ArrayList<>());
        item2.setDescription("something");
        item2.setId(1L);
        item2.setLastBooking(booking2);
        item2.setName("Name");
        item2.setNextBooking(booking5);
        item2.setOwnerId(1L);
        item2.setRequestId(1L);
        Timestamp timestamp1 = mock(Timestamp.class);
        when(timestamp1.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));

        Booking booking6 = new Booking();
        booking6.setBooker(user);
        booking6.setEnd(timestamp);
        booking6.setId(1L);
        booking6.setItem(item2);
        booking6.setStart(timestamp1);
        booking6.setStatus(Status.WAITING);
        when(bookingService.approveBooking((Long) any(), (Long) any(), (Boolean) any())).thenReturn(booking6);
        MockHttpServletRequestBuilder patchResult = MockMvcRequestBuilders.patch("/bookings/{bookingId}", 1L);
        MockHttpServletRequestBuilder requestBuilder = patchResult.param("approved", String.valueOf(true))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"start\":[1,1,1,1,1],\"end\":" +
                                        "[1,1,1,1,1],\"status\":\"WAITING\"," +
                                        "\"booker\":{\"id\":1,\"name\":\"Name\",\"email"
                                        + "\":\"jane.doe@example.org\"},\"bookerId\":null,\"item\":" +
                                        "{\"id\":1,\"name\":\"Name\",\"description\":\""
                                        + "something\",\"available\":true,\"lastBooking\":null," +
                                        "\"nextBooking\":null,\"comments\":null,"
                                        + "\"requestId\":1}}"));
    }

    @Test
    void testGetAllByBookerWithState() throws Exception {
        when(bookingService.getAllByBookerWithState((Long) any(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .param("state", "foo")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }


    @Test
    void testGetAllByItemOwnerWithState() throws Exception {
        when(bookingService.getAllByItemOwnerWithState((Long) any(), (String) any(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/bookings/owner");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .param("state", "foo")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

}

