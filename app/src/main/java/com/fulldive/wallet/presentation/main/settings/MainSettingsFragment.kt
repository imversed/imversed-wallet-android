package com.fulldive.wallet.presentation.main.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.fulldive.wallet.models.Currency
import com.fulldive.wallet.presentation.base.BaseMvpFragment
import com.fulldive.wallet.presentation.chains.choicenet.ChoiceChainDialogFragment
import com.fulldive.wallet.presentation.main.currency.CurrencyDialogFragment
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.BuildConfig
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.activities.AccountListActivity
import wannabit.io.cosmostaion.activities.AppLockSetActivity
import wannabit.io.cosmostaion.activities.chains.starname.StarNameWalletConnectActivity
import wannabit.io.cosmostaion.databinding.FragmentMainSettingBinding

class MainSettingsFragment : BaseMvpFragment<FragmentMainSettingBinding>(), MainSettingsMoxyView {

    private val presenter by moxyPresenter {
        getInjector().getInstance<MainSettingsPresenter>()
    }

    override fun getViewBinding() = FragmentMainSettingBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding {
            addWalletButton.setOnClickListener {
                showDialog(ChoiceChainDialogFragment.newInstance())
            }
            walletButton.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), AccountListActivity::class.java)
                )
            }
            adblockButton.setOnClickListener {
                startActivity(
                    Intent(requireActivity(), AppLockSetActivity::class.java)
                )
            }
            currencyButton.setOnClickListener {
                showDialog(CurrencyDialogFragment.newInstance())
            }
            discordButton.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://discord.gg/BW2unf5s8X")
                    )
                )
            }
            guideButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fulldive.com/faq")))
            }
            termsButton.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.fulldive.com/terms-of-use")
                    )
                )
            }
            githubButton.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/imversed/imversed-wallet-android")
                    )
                )
            }
            versionButton.setOnClickListener { v: View? ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=" + requireContext().packageName)
                startActivity(intent)
            }
            versionText.text = getString(R.string.str_version_short, BuildConfig.VERSION_NAME)
        }
    }

    override fun setCurrency(currency: Currency) {
        binding {
            currencyTextView.text = currency.title
        }
    }

    override fun setAppLockEnabled(enabled: Boolean) {
        binding {
            appLockTextView.setText(
                if (enabled) {
                    R.string.str_app_applock_enabled
                } else {
                    R.string.str_app_applock_diabeld
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_STARNAME_WALLET_CONNECT && resultCode == Activity.RESULT_OK) {
            TedPermission(requireContext()).setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    val integrator = IntentIntegrator.forSupportFragment(this@MainSettingsFragment)
                    integrator.setOrientationLocked(true)
                    integrator.initiateScan()
                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                    showMessage(R.string.error_permission)
                }
            })
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setRationaleMessage(getString(R.string.str_permission_qr))
                .check()
        } else {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null && result.contents != null && result.contents.trim { it <= ' ' }
                    .contains("bridge.walletconnect.org")) {
                val wcIntent = Intent(requireActivity(), StarNameWalletConnectActivity::class.java)
                wcIntent.putExtra("wcUrl", result.contents.trim { it <= ' ' })
                startActivity(wcIntent)
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        const val SELECT_STARNAME_WALLET_CONNECT = 9036
        fun newInstance() = MainSettingsFragment()
    }
}