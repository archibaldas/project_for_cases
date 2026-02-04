package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final WeightPriceProvider weightPriceProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackages = shipment.volumeAllPackages();

        var minimalPrice = weightPriceProvider.minimalPrice();

        var priceByWeight = weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg);

        var priceByVolume = weightPriceProvider
                .costPerCubicMeter()
                .multiply(volumeAllPackages);

        return priceByWeight
                .max(priceByVolume)
                .max(minimalPrice);
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}
