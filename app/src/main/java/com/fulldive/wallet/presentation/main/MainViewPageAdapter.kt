package com.fulldive.wallet.presentation.main

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fulldive.wallet.presentation.main.history.MainHistoryFragment
import wannabit.io.cosmostaion.fragment.main.MainSendFragment
import com.fulldive.wallet.presentation.main.settings.MainSettingsFragment
import com.fulldive.wallet.presentation.main.tokens.MainTokensFragment

class MainViewPageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(
    fragmentManager
) {
    val fragments: List<Fragment> = listOf(
        MainSendFragment.newInstance(null),
        MainTokensFragment.newInstance(),
        MainHistoryFragment.newInstance(),
        MainSettingsFragment.newInstance()
    )

    var currentFragment: Fragment? = null
        private set

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        if (obj is Fragment && currentFragment != obj) {
            currentFragment = obj
        }
        super.setPrimaryItem(container, position, obj)
    }
}