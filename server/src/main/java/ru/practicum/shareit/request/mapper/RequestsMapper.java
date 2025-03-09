package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestAddDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper
public interface RequestsMapper {
    RequestsMapper INSTANCE = Mappers.getMapper(RequestsMapper.class);

    ItemRequest toEntity(ItemRequestDto itemRequestDto);

    ItemRequestDto toDto(ItemRequest itemRequest);

    ItemRequest toEntity(RequestAddDto requestAddDto);
}
