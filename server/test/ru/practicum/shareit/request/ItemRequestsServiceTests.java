package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.repository.ItemRequestsStorage;
import ru.practicum.shareit.request.service.ItemRequestsService;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestsServiceTests {
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private ItemRequestsStorage itemRequestsStorage;
    @InjectMocks
    private ItemRequestsService itemRequestsService;

    @Test
    void addItemRequestWhenUserNotFoundThrowsException() {
        when(userStorage.getUser(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> itemRequestsService.addItemRequest(new RequestAddDto(), 1L));
    }

    @Test
    void getItemRequestWhenNotExistsThrowsException() {
        when(itemRequestsStorage.getItemRequest(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestsService.getItemRequest(1L));
    }

    @Test
    void getItemRequestsForCurrentUserWhenUserNotFoundThrowsException() {
        when(userStorage.getUser(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> itemRequestsService.getItemRequestsForCurrentUser(1L));
    }
}
