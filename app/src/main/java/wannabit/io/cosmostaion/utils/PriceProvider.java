package wannabit.io.cosmostaion.utils;

import org.jetbrains.annotations.Nullable;

import wannabit.io.cosmostaion.dao.Price;

public interface PriceProvider {
    @Nullable
    Price getPrice(String denom);
}
