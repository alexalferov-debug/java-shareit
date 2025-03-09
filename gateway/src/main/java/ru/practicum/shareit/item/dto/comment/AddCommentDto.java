package ru.practicum.shareit.item.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDto {
    @NotBlank
    @Size(max = 255)
    private String text;
}
