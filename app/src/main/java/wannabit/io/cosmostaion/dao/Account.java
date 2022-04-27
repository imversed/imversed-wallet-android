package wannabit.io.cosmostaion.dao;

import static com.fulldive.wallet.models.BaseChain.BNB_MAIN;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;

import com.fulldive.wallet.models.BaseChain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import wannabit.io.cosmostaion.R;
import wannabit.io.cosmostaion.utils.WDp;

public class Account {
    public Long id = 0L;
    public String uuid;
    public String nickName = "";
    public String address;
    public String baseChain;

    public Boolean hasPrivateKey;
    public String resource;
    public String spec;
    public Boolean fromMnemonic;
    public Integer path;

    public Integer sequenceNumber;
    public Integer accountNumber;
    public int msize;
    public Long importTime;

    public Integer customPath;

    public List<Balance> balances;

    public static Account getNewInstance() {
        Account result = new Account();
        result.uuid = UUID.randomUUID().toString();
        return result;
    }

    public Account() {
    }

    public Account(
            String uuid,
            String address,
            String baseChain,
            boolean hasPrivateKey,
            String resource,
            String spec,
            boolean fromMnemonic,
            int msize,
            long importTime,
            int path,
            int customPath
    ) {
        this.uuid = uuid;
        this.address = address;
        this.baseChain = baseChain;
        this.hasPrivateKey = hasPrivateKey;
        this.resource = resource;
        this.spec = spec;
        this.fromMnemonic = fromMnemonic;
        this.msize = msize;
        this.importTime = importTime;
        this.path = path;
        this.customPath = customPath;
    }

    public Account(
            String uuid,
            String address,
            String baseChain,
            long importTime
    ) {
        this.uuid = uuid;
        this.address = address;
        this.baseChain = baseChain;
        this.hasPrivateKey = false;
        this.fromMnemonic = false;
        this.importTime = importTime;
        this.resource = "";
        this.spec = "";
        this.path = 0;
        this.customPath = 0;
    }

    public Account(
            Long id, String uuid, String nickName, String address,
            String baseChain, boolean hasPrivateKey, String resource, String spec,
            boolean fromMnemonic, int path, int sequenceNumber,
            int accountNumber, int msize, long importTime, int customPath
    ) {
        this.id = id;
        this.uuid = uuid;
        this.nickName = nickName;
        this.address = address;
        this.baseChain = baseChain;
        this.hasPrivateKey = hasPrivateKey;
        this.resource = resource;
        this.spec = spec;
        this.fromMnemonic = fromMnemonic;
        this.path = path;
        this.sequenceNumber = sequenceNumber;
        this.accountNumber = accountNumber;
        this.msize = msize;
        this.importTime = importTime;
        this.customPath = customPath;
    }

    public String getAccountTitle(Context context) {
        String result;
        if (TextUtils.isEmpty(nickName)) {
            result = context.getString(R.string.str_my_wallet) + id;
        } else {
            result = nickName;
        }
        return result;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public BigDecimal getBnbBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BNB_MAIN.INSTANCE.getMainDenom())) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getBnbBalanceScale() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BNB_MAIN.INSTANCE.getMainDenom())) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getBnbTokenBalance(String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(symbol)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getTokenBalance(String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equalsIgnoreCase(symbol)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public SpannableString getLastTotal(BaseChain chain, String lastTotal) {
        SpannableString result = SpannableString.valueOf("--");
        try {
            if (!TextUtils.isEmpty(lastTotal)) {
                result = WDp.getDpAmount2(new BigDecimal(lastTotal), chain.getDivideDecimal(), chain.getDisplayDecimal());
            }
        } catch (Exception ignore) {
        }
        return result;
    }
}
