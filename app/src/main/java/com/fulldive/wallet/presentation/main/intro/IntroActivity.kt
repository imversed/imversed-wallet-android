package com.fulldive.wallet.presentation.main.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityOptionsCompat
import com.fulldive.wallet.extensions.applyBottomSystemBarInset
import com.fulldive.wallet.presentation.base.BaseMvpActivity
import com.fulldive.wallet.presentation.main.MainActivity
import com.joom.lightsaber.getInstance
import moxy.ktx.moxyPresenter
import wannabit.io.cosmostaion.R
import wannabit.io.cosmostaion.activities.AppLockActivity
import wannabit.io.cosmostaion.base.ITimelessActivity
import wannabit.io.cosmostaion.databinding.ActivityIntroBinding

class IntroActivity : BaseMvpActivity<ActivityIntroBinding>(), IntroMoxyView, ITimelessActivity {

    private val presenter by moxyPresenter {
        appInjector.getInstance<IntroPresenter>()
            .also {
                it.intent = intent
            }
    }

    private val launcher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            showMainActivity(0)
        }
    }

    override fun getViewBinding() = ActivityIntroBinding.inflate(layoutInflater)

    // The splash artwork is meant to fill the screen, so only the buttons panel is inset.
    override val appliesSystemBarInsets = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding {
            bottomLayer2.applyBottomSystemBarInset()
            startButton.setOnClickListener {
                presenter.onStartButtonClicked()
            }
        }
    }

    override fun showButtonsPanel() {
        binding {
            AnimationUtils
                .loadAnimation(introBgGr.context, R.anim.fade_in5)
                .let(introBgGr::startAnimation)
            AnimationUtils
                .loadAnimation(introBg.context, R.anim.fade_out5)
                .let(introBg::startAnimation)
            // The "Powered by Fulldive" badge that used to fade out here is gone with the
            // rebranding, so the buttons panel just fades straight in.
            bottomLayer2.visibility = View.VISIBLE
            AnimationUtils
                .loadAnimation(bottomLayer2.context, R.anim.fade_in2)
                .let(bottomLayer2::startAnimation)
        }
    }

    override fun showLockScreen() {
        launcher.launch(
            Intent(this@IntroActivity, AppLockActivity::class.java),
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_bottom, R.anim.fade_out)
        )
    }

    override fun showMainActivity(tabIndex: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("page", tabIndex)
        startActivity(intent)
    }
}