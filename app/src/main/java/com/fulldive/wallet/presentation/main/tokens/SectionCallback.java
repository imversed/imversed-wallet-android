package com.fulldive.wallet.presentation.main.tokens;

import com.fulldive.wallet.models.BaseChain;

public interface SectionCallback {
    boolean isSection(BaseChain baseChain, int position);

    int getSectionHeader(BaseChain baseChain, int section);
    int getSectionCount(int section);
}
