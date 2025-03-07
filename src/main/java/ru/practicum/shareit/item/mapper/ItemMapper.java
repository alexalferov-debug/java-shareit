package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

@Mapper(uses = CommentMapper.class)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemWithoutOwnerDto toItemWithoutOwnerDto(Item item);

    ItemWithoutOwnerAndCommentsDto toItemWithoutOwnerAndCommentsDto(Item item);

    ItemDto toItemDto(Item item);

    ItemWithoutOwnerDto toItemWithoutOwnerDto(ItemDto itemDto);

    Item toItem(ItemRequestAddDto itemRequestAddDto);

    Item toItem(ItemRequestPatchDto itemRequestPatchDto);

    Item toItem(ItemDto itemRequestPatchDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "available", source = "available")
    Item toEntity(ItemWithoutOwnerDto itemWithoutOwnerDto);
}
