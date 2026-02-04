package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 * @param dimensions габариты упаковки
 */
public record Pack(Weight weight,
                   Dimensions dimensions) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));

    public Pack(Weight weight){
        this(weight, null);
    }

    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package can't be more than " + maxWeight);
        }
    }

    /**
     * Объём упаковки в м3 (0, если габариты не заданы).
     */
    public BigDecimal volumeCubicMeters() {
        if(dimensions == null) {
            return BigDecimal.ZERO;
        }
        return dimensions.volumeCubicMeters();
    }
}
