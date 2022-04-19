package wannabit.io.cosmostaion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import wannabit.io.cosmostaion.R;

public class AlphabetKeyBoardFragment extends KeyboardFragment implements View.OnClickListener {

    private View rootView;
    private final Button[] alphabetButtons = new Button[26];
    private ArrayList<String> mAlphabetArray = new ArrayList<>();

    public static AlphabetKeyBoardFragment newInstance() {
        return new AlphabetKeyBoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_keyboard_alphabet, container, false);
        mAlphabetArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.password_alphabet)));
        Collections.shuffle(mAlphabetArray, new Random(System.nanoTime()));
        final String packageName = rootView.getContext().getPackageName();
        for (int i = 0; i < alphabetButtons.length; i++) {
            alphabetButtons[i] = rootView.findViewById(getResources().getIdentifier("alphabetButton" + i, "id", packageName));
            alphabetButtons[i].setText(mAlphabetArray.get(i));
            alphabetButtons[i].setOnClickListener(this);
        }
        ImageButton backButton = rootView.findViewById(R.id.deleteButton);
        backButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void shuffleKeyboard() {
        final Animation fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeInAnimation.reset();

        Animation fadeOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out);
        fadeOutAnimation.reset();
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Collections.shuffle(mAlphabetArray, new Random(System.nanoTime()));
                for (int i = 0; i < alphabetButtons.length; i++) {
                    alphabetButtons[i].setText(mAlphabetArray.get(i));
                }
                rootView.startAnimation(fadeInAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rootView.startAnimation(fadeOutAnimation);
    }

    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            if (keyboardListener != null) {
                keyboardListener.userInsertKey(((Button) view).getText().toString().trim().toCharArray()[0]);
            }

        } else if (view instanceof ImageButton) {
            if (keyboardListener != null) {
                keyboardListener.userDeleteKey();
            }
        }
    }
}
