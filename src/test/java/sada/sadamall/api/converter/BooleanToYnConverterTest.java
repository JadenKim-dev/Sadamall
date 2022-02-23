package sada.sadamall.api.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sada.sadamall.api.converter.BooleanToYNConverter;

import javax.persistence.AttributeConverter;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BooleanToYnConverterTest {
    private AttributeConverter<Boolean, String> converter;

    @BeforeEach
    public void beforeEach() {
        this.converter = new BooleanToYNConverter();
    }

    @Test
    public void convertToDatabaseColumn() {
        assertThat(converter.convertToDatabaseColumn(true)).isEqualTo("Y");
        assertThat(converter.convertToDatabaseColumn(false)).isEqualTo("N");
    }

    @Test
    public void convertToEntityAttribute() {
        assertThat(converter.convertToEntityAttribute("Y")).isEqualTo(true);
        assertThat(converter.convertToEntityAttribute("N")).isEqualTo(false);
    }
}
