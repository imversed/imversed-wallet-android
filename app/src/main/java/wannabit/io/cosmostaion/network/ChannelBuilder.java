package wannabit.io.cosmostaion.network;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fulldive.wallet.models.BaseChain;

import java.util.HashMap;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ChannelBuilder {

    public final static long TIME_OUT = 8L;
    private final static HashMap<String, ManagedChannel> channelHashMap = new HashMap<>();

    @Nullable
    public static ManagedChannel getChain(BaseChain chain) {
        ManagedChannel result;
        result = channelHashMap.get(chain.getChainName());
        if (result == null) {
            synchronized (ChannelBuilder.class) {
                final String grpcApiUrl = chain.getGrpcApiHost().getUrl();
                final int grpcApiPort = chain.getGrpcApiHost().getPort();
                if (!TextUtils.isEmpty(grpcApiUrl)) {
                    result = ManagedChannelBuilder.forAddress(grpcApiUrl, grpcApiPort)
                            .usePlaintext()
                            .build();
                    channelHashMap.put(chain.getChainName(), result);
                }
            }
        }
        return result;
    }
}
