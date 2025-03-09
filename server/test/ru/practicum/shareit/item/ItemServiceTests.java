package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.exception.DataValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.ItemRequestPatchDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {
    @Mock
    private UserStorage userService;
    @Mock
    private BookingStorage bookingService;
    @Mock
    private ItemStorage itemStorage;
    @InjectMocks
    private ItemServiceImpl itemService;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "user@mail.com");
        item = new Item();
        item.setId(1L);
        item.setOwner(user);
    }

    @Test
    void getItemWhenNotOwnerReturnsWithoutBookings() {
        when(itemStorage.getItem(1L)).thenReturn(Optional.of(item));
        var result = itemService.getItem(1L, 999L);
        assertNull(result.getLastBooking());
        assertNull(result.getNextBooking());
    }

    @Test
    void createItemWhenUserNotFoundThrowsException() {
        when(userService.getUser(1L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> itemService.createItem(new ItemRequestAddDto(), 1L));
    }

    @Test
    void updateItemWenNotOwnerThrowsException() {
        when(itemStorage.getItem(1L)).thenReturn(Optional.of(item));
        assertThrows(NotFoundException.class, () -> itemService.updateItem(new ItemRequestPatchDto(), 1L, 999L));
    }

    @Test
    void searchItemWhenEmptyTextReturnsEmptyList() {
        var result = itemService.searchItem("");
        assertTrue(result.isEmpty());
    }

    @Test
    void addCommentWhenUserDidNotBookThrowsException() {
        when(itemStorage.getItem(1L)).thenReturn(Optional.of(item));
        when(userService.getUser(1L)).thenReturn(user);
        when(bookingService.getFinishedBookings(any())).thenReturn(List.of());
        assertThrows(DataValidationException.class, () -> itemService.addComment(new AddCommentDto(), 1L, 1L));
    }
}
