package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigInteger;

public record CargoPackage(

        @Positive(message = "Вес упаковки должен быть положительным")
        @Schema(description = "Вес упаковки, граммы", example = "5667.45")
        BigInteger weight,

        @Schema(description = "Длинна упаковки, миллиметры (мм)", example = "345", defaultValue = "0")
        @PositiveOrZero(message = "Значение должно быть положительным")
        @Max(value = 1500, message = "Значение не может превышать 1500мм")
        Integer length,

        @PositiveOrZero(message = "Значение должно быть положительным")
        @Max(value = 1500, message = "Значение не может превышать 1500мм")
        @Schema(description = "Ширина упаковки, миллиметры (мм)", example = "589", defaultValue = "0")
        Integer width,

        @PositiveOrZero(message = "Значение должно быть положительным")
        @Max(value = 1500, message = "Значение не может превышать 1500мм")
        @Schema(description = "Высота упаковки, миллиметры (мм)", example = "234", defaultValue = "0")
        Integer height

) {
}
