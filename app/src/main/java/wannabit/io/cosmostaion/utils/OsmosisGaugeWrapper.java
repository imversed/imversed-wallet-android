package wannabit.io.cosmostaion.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import osmosis.incentives.GaugeOuterClass;

public class OsmosisGaugeWrapper implements Serializable {
    public List<GaugeOuterClass.Gauge> array;

    public OsmosisGaugeWrapper(List<GaugeOuterClass.Gauge> a) {
        array = a;
    }
}
