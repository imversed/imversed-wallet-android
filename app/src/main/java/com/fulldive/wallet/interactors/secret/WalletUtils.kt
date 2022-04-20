package com.fulldive.wallet.interactors.secret

import android.text.TextUtils
import com.fulldive.wallet.interactors.secret.MnemonicUtils.hexStringToByteArray
import com.fulldive.wallet.interactors.secret.utils.Bech32Utils
import com.fulldive.wallet.models.BaseChain
import org.bouncycastle.crypto.digests.RIPEMD160Digest
import org.web3j.crypto.WalletUtils
import wannabit.io.cosmostaion.crypto.Sha256
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

object WalletUtils {

    fun isValidStringPrivateKey(text: String): Boolean {
        return Pattern
            .compile("^(0x|0X)?[a-fA-F0-9]{64}")
            .matcher(text)
            .matches()
    }

    fun isValidEthAddress(address: String): Boolean {
        return WalletUtils.isValidAddress(address)
    }

    fun isValidBech32(address: String): Boolean {
        var result = false
        try {
            Bech32Utils.bech32Decode(address)
            result = true
        } catch (ignore: Exception) {
        }
        return result
    }

    fun getDpAddress(chain: BaseChain, pubHex: String): String {
        val digest = Sha256.getSha256Digest()
        val hash = digest.digest(
            hexStringToByteArray(pubHex)
        )
        val digest2 = RIPEMD160Digest()
        digest2.update(hash, 0, hash.size)
        val hash3 = ByteArray(digest2.digestSize)
        digest2.doFinal(hash3, 0)
        val converted = convertBits(hash3, 8, 5, true)
        return Bech32Utils.bech32Encode(chain.chainAddressPrefix.toByteArray(), converted)
    }

    fun convertDpOpAddressToDpAddress(dpOpAddress: String, chain: BaseChain): String {
        var result = ""
        val prefix = chain.chainAddressPrefix
        if (!TextUtils.isEmpty(prefix)) {
            result = Bech32Utils.bech32Encode(
                prefix.toByteArray(),
                Bech32Utils.bech32Decode(dpOpAddress)
            )
        }
        return result
    }


    @Throws(java.lang.Exception::class)
    fun convertAddressOkexToEth(exAddress: String): String {
        val pub = convertBits(Bech32Utils.bech32Decode(exAddress), 5, 8, false)
        return "0x" + MnemonicUtils.byteArrayToHexString(pub)
    }


    fun upgradeOKAddress(oldAddress: String): String {
        return Bech32Utils.bech32Encode("ex".toByteArray(), Bech32Utils.bech32Decode(oldAddress))
    }

    @Throws(Exception::class)
    fun convertBits(data: ByteArray, frombits: Int, tobits: Int, pad: Boolean): ByteArray {
        var acc = 0
        var bits = 0
        val baos = ByteArrayOutputStream()
        val maxv = (1 shl tobits) - 1
        for (i in data.indices) {
            val value: Int = data[i].toInt() and 0xff
            if (value ushr frombits != 0) {
                throw Exception("invalid data range: data[$i]=$value (frombits=$frombits)")
            }
            acc = acc shl frombits or value
            bits += frombits
            while (bits >= tobits) {
                bits -= tobits
                baos.write(acc ushr bits and maxv)
            }
        }
        if (pad) {
            if (bits > 0) {
                baos.write(acc shl tobits - bits and maxv)
            }
        } else if (bits >= frombits) {
            throw Exception("illegal zero padding")
        } else if (acc shl tobits - bits and maxv != 0) {
            throw Exception("non-zero padding")
        }
        return baos.toByteArray()
    }


}