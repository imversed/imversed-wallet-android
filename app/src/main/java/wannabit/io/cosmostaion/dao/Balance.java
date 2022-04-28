package wannabit.io.cosmostaion.dao;

import android.text.TextUtils;

import java.math.BigDecimal;

public class Balance {
    public Long accountId;
    public String symbol;
    public Long fetchTime;
    public BigDecimal balance;
    public BigDecimal frozen;
    public BigDecimal locked;

    public Balance() {
    }

    public Balance(Long accountId, String symbol, String balance, Long fetchTime, String frozen, String locked) {
        this.accountId = accountId;
        this.symbol = symbol;
        this.fetchTime = fetchTime;
        this.balance = BigDecimal.ZERO;
        this.frozen = BigDecimal.ZERO;
        this.locked = BigDecimal.ZERO;

        if (balance != null && !TextUtils.isEmpty(balance)) {
            try {
                this.balance = new BigDecimal(balance);
            } catch (Exception ignore) {
            }
        }
        if (frozen != null && !TextUtils.isEmpty(frozen)) {
            try {
                this.frozen = new BigDecimal(frozen);
            } catch (Exception ignore) {
            }
        }
        if (locked != null && !TextUtils.isEmpty(locked)) {
            try {
                this.locked = new BigDecimal(locked);
            } catch (Exception ignore) {
            }
        }
    }

    public BigDecimal getTotalAmount() {
        return balance.add(locked).add(frozen);
    }

    public BigDecimal getDelegatableAmount() {
        return balance.add(locked);
    }

    public boolean isIbc() {
        return symbol.startsWith("ibc/");
    }

    public String getIbcHash() {
        if (!isIbc()) {
            return null;
        }
        return symbol.replaceAll("ibc/", "");
    }

    public boolean osmosisAmm() {
        return symbol.startsWith("gamm/pool/");
    }

    public String osmosisAmmDpDenom() {
        String[] split = symbol.split("/");
        return "GAMM-" + split[split.length - 1];
    }

    public long osmosisAmmPoolId() {
        String id = symbol.replace("gamm/pool/", "");
        return Long.parseLong(id);
    }

    public long injectivePoolId() {
        String id = symbol.replace("share", "");
        return Long.parseLong(id);
    }

}
