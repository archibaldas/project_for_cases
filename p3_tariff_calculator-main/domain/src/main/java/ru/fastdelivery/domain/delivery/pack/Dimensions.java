package ru.fastdelivery.domain.delivery.pack;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Габариты упаковки в миллиметрах.
 * Хранит уже нормализованные (округлённые до кратности 50) значения.
 */
public record Dimensions(
        int lengthMm,
        int widthMm,
        int heightMm
) {

    private static final int ROUND_BASE_MM = 50;
    private static final BigDecimal CUBIC_MM_IN_CUBIC_METER = BigDecimal.valueOf(1_000_000_000L);

    public Dimensions {
        lengthMm = normalize(lengthMm);
        widthMm = normalize(widthMm);
        heightMm = normalize(heightMm);
    }

    /**
     * Объём в кубических метрах с точностью до четвертого знака.
     */
    public BigDecimal volumeCubicMeters() {
        BigDecimal volumeMm3 = BigDecimal.valueOf(lengthMm)
                .multiply(BigDecimal.valueOf(widthMm))
                .multiply(BigDecimal.valueOf(heightMm));

        return volumeMm3.divide(CUBIC_MM_IN_CUBIC_METER, 4, RoundingMode.HALF_UP);
    }

    private static int normalize(int valueMm){
        if(valueMm == 0) {
            return 0;
        }

        return (int) (Math.round(valueMm / (double) ROUND_BASE_MM) * ROUND_BASE_MM);
    }

}
