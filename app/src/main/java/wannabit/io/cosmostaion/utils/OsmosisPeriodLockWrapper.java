package wannabit.io.cosmostaion.utils;

import java.io.Serializable;
import java.util.List;

import osmosis.lockup.Lock;

public class OsmosisPeriodLockWrapper implements Serializable {
    public List<Lock.PeriodLock> array;

    public OsmosisPeriodLockWrapper(List<Lock.PeriodLock> a) {
        array = a;
    }
}
