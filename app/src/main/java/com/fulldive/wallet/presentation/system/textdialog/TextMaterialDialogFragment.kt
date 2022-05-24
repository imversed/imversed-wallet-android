package com.fulldive.wallet.presentation.system.textdialog

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import com.afollestad.materialdialogs.MaterialDialog
import com.fulldive.wallet.di.IEnrichableActivity
import com.fulldive.wallet.extensions.toast
import com.fulldive.wallet.extensions.unsafeLazy
import com.fulldive.wallet.presentation.base.BaseMoxyView
import com.joom.lightsaber.Injector
import com.joom.lightsaber.getInstance
import moxy.MvpAppCompatDialogFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import wannabit.io.cosmostaion.R

class TextMaterialDialogFragment : MvpAppCompatDialogFragment(), BaseMoxyView {

    private val resultCode by unsafeLazy {
        arguments?.getString(KEY_RESULT_CODE)
            ?: throw IllegalStateException("requestCode can't be null")
    }

    private val appInjector: Injector
        get() = (activity as IEnrichableActivity).appInjector

    @InjectPresenter
    lateinit var presenter: TextMaterialDialogPresenter

    @ProvidePresenter
    fun providePresenter() = appInjector.getInstance<TextMaterialDialogPresenter>().also {
        it.resultCode = resultCode
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialDialog.Builder(requireContext())
        arguments?.getInt(KEY_TITLE_RES)?.takeIf { it > 0 }?.let { builder.title(it) }
        arguments?.getCharSequence(KEY_TITLE_TEXT)?.takeIf { it.isNotEmpty() }
            ?.let { builder.title(it) }

        arguments?.getInt(KEY_MESSAGE_RES)?.takeIf { it > 0 }?.let { builder.content(it) }
        arguments?.getCharSequence(KEY_MESSAGE_TEXT)?.takeIf { it.isNotEmpty() }
            ?.let { builder.content(it) }

        arguments?.getInt(KEY_POSITIVE_TEXT_RES)?.takeIf { it > 0 }
            ?.let { builder.positiveText(it) }
        arguments?.getCharSequence(KEY_POSITIVE_TEXT)?.takeIf { it.isNotEmpty() }
            ?.let { builder.positiveText(it) }

        arguments?.getInt(KEY_NEGATIVE_TEXT_RES)?.takeIf { it > 0 }
            ?.let { builder.negativeText(it) }
        arguments?.getCharSequence(KEY_NEGATIVE_TEXT)?.takeIf { it.isNotEmpty() }
            ?.let { builder.negativeText(it) }

        arguments?.getInt(KEY_NEUTRAL_TEXT_RES)?.takeIf { it > 0 }?.let { builder.neutralText(it) }
        arguments?.getCharSequence(KEY_NEUTRAL_TEXT)?.takeIf { it.isNotEmpty() }
            ?.let { builder.neutralText(it) }

        arguments?.getInt(KEY_CHECKBOX_RES)?.takeIf { it > 0 }?.let { textRes ->
            val checkboxValue = arguments?.getBoolean(KEY_CHECKBOX_DEFAULT) ?: false
            builder.checkBoxPromptRes(
                textRes,
                checkboxValue
            ) { _, value -> presenter.onCheckBoxChanged(value) }
            presenter.onCheckBoxChanged(checkboxValue)
        }

        arguments?.getCharSequence(KEY_CHECKBOX_TEXT)?.takeIf { it.isNotEmpty() }?.let { text ->
            val checkboxValue = arguments?.getBoolean(KEY_CHECKBOX_DEFAULT) ?: false
            builder.checkBoxPrompt(
                text,
                checkboxValue
            ) { _, value -> presenter.onCheckBoxChanged(value) }
            presenter.onCheckBoxChanged(checkboxValue)
        }

        builder.titleColorRes(R.color.textColorPrimary)
            .backgroundColorRes(R.color.colorWhite)
            .positiveColorRes(R.color.textColorPrimary)
            .negativeColorRes(R.color.textColorSecondary)
            .neutralColorRes(R.color.textColorSecondary)
            .autoDismiss(true)
            .onPositive { _, _ -> presenter.onPositive() }
            .onNegative { _, _ -> presenter.onNegative() }
            .onNeutral { _, _ -> presenter.onNeutral() }

        return builder.build()
    }

    override fun showMessage(message: String) {
        context?.toast(message)
    }

    override fun showMessage(resourceId: Int) {
        context?.toast(resourceId)
    }

    companion object {
        private const val KEY_RESULT_CODE = "KEY_RESULT_CODE"
        private const val KEY_TITLE_RES = "KEY_TITLE_RES"
        private const val KEY_TITLE_TEXT = "KEY_TITLE_TEXT"
        private const val KEY_MESSAGE_RES = "KEY_MESSAGE_RES"
        private const val KEY_MESSAGE_TEXT = "KEY_MESSAGE_TEXT"
        private const val KEY_POSITIVE_TEXT_RES = "KEY_POSITIVE_TEXT_RES"
        private const val KEY_NEGATIVE_TEXT_RES = "KEY_NEGATIVE_TEXT_RES"
        private const val KEY_NEUTRAL_TEXT_RES = "KEY_NEUTRAL_TEXT_RES"
        private const val KEY_POSITIVE_TEXT = "KEY_POSITIVE_TEXT"
        private const val KEY_NEGATIVE_TEXT = "KEY_NEGATIVE_TEXT"
        private const val KEY_NEUTRAL_TEXT = "KEY_NEGATIVE_TEXT"
        private const val KEY_CHECKBOX_TEXT = "KEY_CHECKBOX_TEXT"
        private const val KEY_CHECKBOX_RES = "KEY_CHECKBOX_RES"
        private const val KEY_CHECKBOX_DEFAULT = "KEY_CHECKBOX_DEFAULT"

        fun newInstance(
            resultCode: String,
            @StringRes titleTextId: Int = 0,
            titleTextString: CharSequence = "",
            @StringRes messageTextId: Int = 0,
            messageTextString: CharSequence = "",
            @StringRes positiveTextId: Int = 0,
            positiveTextString: CharSequence = "",
            @StringRes negativeTextId: Int = 0,
            negativeTextString: CharSequence = "",
            @StringRes neutralTextId: Int = 0,
            neutralTextString: CharSequence = "",
            @StringRes checkboxTextId: Int = 0,
            checkboxTextString: CharSequence = "",
            checkboxDefault: Boolean = false
        ) = TextMaterialDialogFragment().apply {
            arguments = bundleOf(
                KEY_RESULT_CODE to resultCode,
                KEY_POSITIVE_TEXT_RES to positiveTextId,
                KEY_NEGATIVE_TEXT_RES to negativeTextId,
                KEY_NEUTRAL_TEXT_RES to neutralTextId,
                KEY_TITLE_RES to titleTextId,
                KEY_MESSAGE_RES to messageTextId,
                KEY_POSITIVE_TEXT to positiveTextString,
                KEY_NEGATIVE_TEXT to negativeTextString,
                KEY_NEUTRAL_TEXT to neutralTextString,
                KEY_TITLE_TEXT to titleTextString,
                KEY_MESSAGE_TEXT to messageTextString,
                KEY_CHECKBOX_TEXT to checkboxTextString,
                KEY_CHECKBOX_RES to checkboxTextId,
                KEY_CHECKBOX_DEFAULT to checkboxDefault
            )
        }
    }
}
