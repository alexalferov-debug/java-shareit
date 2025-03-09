package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@JsonTest
public class CreateBookingDtoTest {

    private JacksonTester<CreateBookingDto> json;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
    }

    @Test
    void shouldSerializeDatesInIsoFormat() throws Exception {
        CreateBookingDto dto = new CreateBookingDto(
                LocalDateTime.of(2023, 12, 31, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0),
                1L
        );

        String jsonContent = json.write(dto).getJson();
        assertThat(jsonContent).contains("\"start\":\"2023-12-31T10:00:00\"");
        assertThat(jsonContent).contains("\"end\":\"2024-01-01T10:00:00\"");
    }

    @Test
    void shouldDeserializeFromIsoDateString() throws Exception {
        String jsonContent = "{\"start\":\"2023-12-31T10:00:00\", \"end\":\"2024-01-01T10:00:00\", \"itemId\":1}";

        CreateBookingDto dto = json.parse(jsonContent).getObject();

        assertThat(dto.getStart()).isEqualTo(LocalDateTime.parse("2023-12-31T10:00:00"));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.parse("2024-01-01T10:00:00"));
        assertThat(dto.getItemId()).isEqualTo(1L);
    }

    @Test
    void shouldSerializeCorrectly() throws Exception {
        CreateBookingDto dto = new CreateBookingDto(
                LocalDateTime.of(2023, 12, 31, 10, 0),
                LocalDateTime.of(2024, 1, 1, 10, 0),
                1L
        );
        String json = objectMapper.writeValueAsString(dto);
        assertThat(json).contains("\"start\":\"2023-12-31T10:00:00\"");
        assertThat(json).contains("\"end\":\"2024-01-01T10:00:00\"");
        assertThat(json).contains("\"itemId\":1");
    }

    @Test
    void shouldDeserializeCorrectly() throws Exception {
        String json = "{\"start\":\"2023-12-31T10:00:00\",\"end\":\"2024-01-01T10:00:00\",\"itemId\":1}";
        CreateBookingDto dto = objectMapper.readValue(json, CreateBookingDto.class);
        assertThat(dto.getStart()).isEqualTo(LocalDateTime.of(2023, 12, 31, 10, 0));
        assertThat(dto.getEnd()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(dto.getItemId()).isEqualTo(1L);
    }
}