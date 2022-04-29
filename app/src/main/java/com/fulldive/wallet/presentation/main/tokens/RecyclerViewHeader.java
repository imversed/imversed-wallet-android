package com.fulldive.wallet.presentation.main.tokens;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fulldive.wallet.models.BaseChain;

import wannabit.io.cosmostaion.R;

// Section Header
public class RecyclerViewHeader extends RecyclerView.ItemDecoration {
    private final int topPadding;

    BaseChain baseChain = null;

    private final boolean sticky;
    private final SectionCallback sectionCallback;

    private View headerView;
    private TextView headerTitleTextView;
    private TextView countTextView;

    public RecyclerViewHeader(Context context, boolean sticky, @NonNull SectionCallback sectionCallback) {
        this.sticky = sticky;
        this.sectionCallback = sectionCallback;

        topPadding = context.getResources().getDimensionPixelSize(R.dimen.space_32);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (headerView == null) {
            headerView = inflateHeaderView(parent);
            headerTitleTextView = headerView.findViewById(R.id.header_title);
            countTextView = headerView.findViewById(R.id.recycler_cnt);
            fixLayoutSize(headerView, parent);
        }

        int previousHeader = -1;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int position = parent.getChildAdapterPosition(child);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }

            int section = parent.getAdapter().getItemViewType(position);
            int title = sectionCallback.getSectionHeader(baseChain, section);
            countTextView.setText(String.valueOf(sectionCallback.getSectionCount(section)));
            headerTitleTextView.setText(title);
            if (previousHeader != title || sectionCallback.isSection(baseChain, position)) {
                drawHeader(c, child, headerView);
                previousHeader = title;
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (sectionCallback.isSection(baseChain, position)) {
            outRect.top = topPadding;
        }
    }

    private void drawHeader(Canvas c, View child, View headerView) {
        c.save();
        if (sticky) {
            c.translate(0, Math.max(0, child.getTop() - headerView.getHeight()));
        } else {
            c.translate(0, child.getTop() - headerView.getHeight());
        }
        headerView.draw(c);
        c.restore();
    }

    private View inflateHeaderView(RecyclerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticky_header, parent, false);
    }

    private void fixLayoutSize(View view, ViewGroup parent) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(),
                View.MeasureSpec.UNSPECIFIED);

        int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.getPaddingLeft() + parent.getPaddingRight(),
                view.getLayoutParams().width);
        int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.getPaddingTop() + parent.getPaddingBottom(),
                view.getLayoutParams().height);

        view.measure(childWidth, childHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }
}
