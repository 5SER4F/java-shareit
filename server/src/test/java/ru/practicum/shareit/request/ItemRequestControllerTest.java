package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ItemRequestController.class})
@ExtendWith(SpringExtension.class)
class ItemRequestControllerTest {
    @Autowired
    private ItemRequestController itemRequestController;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    public void testGetAllPageable() throws Exception {
        when(itemRequestService.getAllPageable((Long) any(), anyInt(), anyInt())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/requests/all");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetAllPageableDateTime() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(itemRequestService.getAllPageable((Long) any(), anyInt(), anyInt())).thenReturn(itemRequestList);
        System.out.println("sample = " + itemRequestList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/requests/all");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":1,\"description\":" +
                                        "\"something\"," +
                                        "\"created\":[1,1,1,1,1],"
                                        + "\"items\":[]}]"));
    }

    @Test
    public void testGetAllPageableManyObj() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);
        Timestamp timestamp1 = mock(Timestamp.class);
        when(timestamp1.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreated(timestamp1);
        itemRequest1.setDescription("?");
        itemRequest1.setId(2L);
        itemRequest1.setItems(new ArrayList<>());
        itemRequest1.setRequesterId(2L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest1);
        itemRequestList.add(itemRequest);
        when(itemRequestService.getAllPageable((Long) any(), anyInt(), anyInt())).thenReturn(itemRequestList);
        System.out.println("sample = " + itemRequestList);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/requests/all");
        MockHttpServletRequestBuilder paramResult = getResult.param("from", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("size", String.valueOf(1))
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":2,\"description\":\"?\",\"created\":[1,1,1,1,1]," +
                                        "\"items\":[]},{\"id\":1,\"description\":\""
                                        + "something\"," +
                                        "\"created\":[1,1,1,1,1],\"items\":[]}]"));
    }

    @Test
    public void testGetRequestById() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);
        when(itemRequestService.getById((Long) any(), (Long) any())).thenReturn(itemRequest);
        System.out.println("sample = " + itemRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests/{requestId}",
                        1L)
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"description\":\"something\"," +
                                        "\"created\":[1,1,1,1,1],"
                                        + "\"items\":[]}"));
    }

    @Test
    public void testGetUserRequests() throws Exception {
        when(itemRequestService.getAllUserRequests((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testGetUserRequestsManyObj() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(itemRequestService.getAllUserRequests((Long) any())).thenReturn(itemRequestList);
        System.out.println("sample = " + itemRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":1,\"description\":\"" +
                                        "something\",\"created\":[1,1,1,1,1],"
                                        + "\"items\":[]}]"));
    }

    @Test
    public void testGetUserRequests3Obj() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);
        Timestamp timestamp1 = mock(Timestamp.class);
        when(timestamp1.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest1 = new ItemRequest();
        itemRequest1.setCreated(timestamp1);
        itemRequest1.setDescription("?");
        itemRequest1.setId(2L);
        itemRequest1.setItems(new ArrayList<>());
        itemRequest1.setRequesterId(2L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest1);
        itemRequestList.add(itemRequest);
        when(itemRequestService.getAllUserRequests((Long) any())).thenReturn(itemRequestList);
        System.out.println("sample = " + itemRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42");
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":2,\"description\":\"?\"," +
                                        "\"created\":[1,1,1,1,1],\"items\":[]}," +
                                        "{\"id\":1,\"description\":" +
                                        "\"something\"," +
                                        "\"created\":[1,1,1,1,1],\"items\":[]}]"));
    }

    @Test
    public void testPostRequestEmpty() throws Exception {
        when(itemRequestService.getAllUserRequests((Long) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashMap<>()));
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    @Test
    public void testPostRequestWithObj() throws Exception {
        Timestamp timestamp = mock(Timestamp.class);
        when(timestamp.toLocalDateTime()).thenReturn(LocalDateTime.of(1, 1, 1, 1,
                1));

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(timestamp);
        itemRequest.setDescription("something");
        itemRequest.setId(1L);
        itemRequest.setItems(new ArrayList<>());
        itemRequest.setRequesterId(1L);

        ArrayList<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        when(itemRequestService.getAllUserRequests((Long) any())).thenReturn(itemRequestList);
        System.out.println("sample = " + itemRequestList);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/requests")
                .header("X-Sharer-User-Id", "42")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new HashMap<>()));
        MockMvcBuilders.standaloneSetup(itemRequestController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "[{\"id\":1,\"description\":\"something\"," +
                                        "\"created\":[1,1,1,1,1],"
                                        + "\"items\":[]}]"));
    }
}

