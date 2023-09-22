package ru.practicum.shareit.item.dto;

//Этот класс используется в ItemRequestSendDto
public interface ItemInRequestProjection {

    long getId();

    Long getOwnerId();

    String getName();

    String getDescription();

    Long getRequestId();

    Boolean getAvailable();

}
