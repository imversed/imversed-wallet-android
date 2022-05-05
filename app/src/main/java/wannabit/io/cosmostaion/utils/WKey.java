package wannabit.io.cosmostaion.utils;

import static com.fulldive.wallet.models.BaseChain.getChain;

import android.util.Base64;

import com.fulldive.wallet.interactors.secret.MnemonicUtils;
import com.fulldive.wallet.interactors.secret.WalletUtils;
import com.fulldive.wallet.interactors.secret.utils.Bech32Utils;
import com.fulldive.wallet.models.BaseChain;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.google.protobuf2.Any;

import org.bitcoinj.core.Bech32;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

import cosmos.auth.v1beta1.QueryOuterClass;
import kotlin.Deprecated;
import wannabit.io.cosmostaion.crypto.Sha256;
import wannabit.io.cosmostaion.dao.Account;

public class WKey {

    public static byte[] getHDSeed(byte[] entropy) {
        try {
            return MnemonicCode.toSeed(MnemonicCode.INSTANCE.toMnemonic(entropy), "");
        } catch (Exception e) {
            return null;
        }
    }

    public static List<ChildNumber> getFetchParentPath2() {
        return ImmutableList.of(ChildNumber.ZERO);
    }

    //singer
    @Deprecated(message = "Alternative is MnemonicUtils.createKeyWithPathFromEntropy")
    public static DeterministicKey getKeyWithPathfromEntropy(Account account, String entropy) {
        DeterministicKey result;
        BaseChain chain = getChain(account.baseChain);
        DeterministicKey masterKey = HDKeyDerivation.createMasterPrivateKey(getHDSeed(MnemonicUtils.INSTANCE.hexStringToByteArray(entropy)));
        final List<ChildNumber> parentPath = chain.getPathProvider().getPathList(account.customPath);
        if (!chain.equals(BaseChain.FETCHAI_MAIN.INSTANCE) || account.customPath != 2) {
            result = new DeterministicHierarchy(masterKey).deriveChild(parentPath, true, true, new ChildNumber(account.path));
        } else {
            DeterministicKey targetKey = new DeterministicHierarchy(masterKey).deriveChild(parentPath, true, true, new ChildNumber(account.path, true));
            result = new DeterministicHierarchy(targetKey).deriveChild(WKey.getFetchParentPath2(), true, true, ChildNumber.ZERO);
        }
        return result;
    }

    public static String getPubKeyValue(ECKey key) {
        String result = "";
        try {
            byte[] data = key.getPubKey();
            result = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
            WLog.w("base64 : " + result);

        } catch (Exception e) {
            WLog.w("Exception");
        }
        return result;
    }

    // For gRpc Keys
    public static Any generateGrpcPubKeyFromPriv(QueryOuterClass.QueryAccountResponse auth, String privateKey) {
        ECKey ecKey = ECKey.fromPrivate(new BigInteger(privateKey, 16));
        if (auth.getAccount().getTypeUrl().contains("/injective.types.v1beta1.EthAccount")) {
            injective.crypto.v1beta1.ethsecp256k1.Keys.PubKey pubKey = injective.crypto.v1beta1.ethsecp256k1.Keys.PubKey.newBuilder().setKey(ByteString.copyFrom(ecKey.getPubKey())).build();
            return Any.newBuilder().setTypeUrl("/injective.crypto.v1beta1.ethsecp256k1.PubKey").setValue(pubKey.toByteString()).build();

        } else if (auth.getAccount().getTypeUrl().contains("/ethermint.types.v1.EthAccount")) {
            ethermint.crypto.v1.ethsecp256k1.Keys.PubKey pubKey = ethermint.crypto.v1.ethsecp256k1.Keys.PubKey.newBuilder().setKey(ByteString.copyFrom(ecKey.getPubKey())).build();
            return Any.newBuilder().setTypeUrl("/ethermint.crypto.v1.ethsecp256k1.PubKey").setValue(pubKey.toByteString()).build();

        } else {
            cosmos.crypto.secp256k1.Keys.PubKey pubKey = cosmos.crypto.secp256k1.Keys.PubKey.newBuilder().setKey(ByteString.copyFrom(ecKey.getPubKey())).build();
            return Any.newBuilder().setTypeUrl("/cosmos.crypto.secp256k1.PubKey").setValue(pubKey.toByteString()).build();
        }
    }

    public static String convertAddressEthToOkex(String esAddress) throws Exception {
        String cosmoTypeAddress = esAddress;
        if (cosmoTypeAddress.startsWith("0x")) {
            cosmoTypeAddress = cosmoTypeAddress.replace("0x", "");
        }
        byte[] pub = MnemonicUtils.INSTANCE.hexStringToByteArray(cosmoTypeAddress);
        String addressResult = null;
        try {
            byte[] bytes = WalletUtils.INSTANCE.convertBits(pub, 8, 5, true);
            addressResult = Bech32.encode("ex", bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return addressResult;
    }

    @Deprecated(message = "Use MnemonicUtils.hexPublicKeyFromPrivateKey")
    public static String generatePubKeyHexFromPriv(String privateKey) {
        ECKey k = ECKey.fromPrivate(new BigInteger(privateKey, 16));
        return k.getPublicKeyAsHex();
    }

    public static String getSwapId(byte[] randomNumberHash, String sender, String otherchainSender) throws Exception {
        byte[] s = WalletUtils.INSTANCE.convertBits(Bech32Utils.bech32Decode(sender), 5, 8, false);
        byte[] rhs = new byte[randomNumberHash.length + s.length];
        System.arraycopy(randomNumberHash, 0, rhs, 0, randomNumberHash.length);
        System.arraycopy(s, 0, rhs, randomNumberHash.length, s.length);

        byte[] o = otherchainSender.toLowerCase().getBytes(StandardCharsets.UTF_8);
        byte[] expectedSwapId = new byte[rhs.length + o.length];
        System.arraycopy(rhs, 0, expectedSwapId, 0, rhs.length);
        System.arraycopy(o, 0, expectedSwapId, rhs.length, o.length);

        WLog.w("expectedSwapId " + MnemonicUtils.INSTANCE.byteArrayToHexString(expectedSwapId));

        byte[] expectedSwapIdSha = Sha256.getSha256Digest().digest(expectedSwapId);
        return MnemonicUtils.INSTANCE.byteArrayToHexString(expectedSwapIdSha);
    }
}