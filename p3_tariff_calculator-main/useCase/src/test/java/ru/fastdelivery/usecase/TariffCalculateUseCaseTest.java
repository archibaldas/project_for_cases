package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Dimensions;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider);

    @Test
    @DisplayName("Расчет стоимости доставки с учетом веса и объема -> выбирается максимум")
    void whenCalculatePrice_thenChooseMaxOfWeightAndVolume() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerM3 = new Price(BigDecimal.valueOf(1000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerM3);

        var pack = new Pack(
                new Weight(BigInteger.valueOf(1200)),
                new Dimensions(350, 600, 250)
        );

        var shipment = new Shipment(List.of(pack),
                new CurrencyFactory(code -> true).create("RUB"));
        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Если минимальная цена больше расчетных по весу и объему -> возвращается минимальная цена")
    void whenMinimalPriceGreaterThanWeightAndVolume_thenReturnMinimal() {
        var minimalPrice = new Price(BigDecimal.valueOf(200), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(new Price(BigDecimal.TEN, currency));
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(new Price(BigDecimal.TEN, currency));

        var pack = new Pack(
                new Weight(BigInteger.valueOf(100)),
                new Dimensions(100, 100, 100)
        );
        var shipment = new Shipment(List.of(pack), currency);

        var actual = tariffCalculateUseCase.calc(shipment);

        assertThat(actual).isEqualTo(minimalPrice);
    }
}