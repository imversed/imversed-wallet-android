package com.fulldive.wallet.presentation.main.tokens;

import com.fulldive.wallet.models.BaseChain;

public interface SectionCallback {
    boolean isSection(BaseChain baseChain, int position);

    int getSectionHeader(int section);
    int getSectionCount(int section);
}
