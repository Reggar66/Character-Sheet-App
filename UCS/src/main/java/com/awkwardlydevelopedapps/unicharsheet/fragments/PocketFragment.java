package com.awkwardlydevelopedapps.unicharsheet.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.awkwardlydevelopedapps.unicharsheet.R;
import com.awkwardlydevelopedapps.unicharsheet.MainActivity;
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.PocketBottomSheetDialog;
import com.awkwardlydevelopedapps.unicharsheet.models.Currency;
import com.awkwardlydevelopedapps.unicharsheet.models.Experience;
import com.awkwardlydevelopedapps.unicharsheet.models.Level;
import com.awkwardlydevelopedapps.unicharsheet.viewModels.PocketViewModel;

import java.util.List;

public class PocketFragment extends Fragment {

    private View rootView;
    private int charId;
    private PocketViewModel viewModel;

    private SeekBar seekBarGold;
    private SeekBar seekBarSilver;
    private SeekBar seekBarCopper;
    private boolean isGoldSeekBarTouched = false;

    private SeekBar seekBarExp;
    private boolean isExpSeekBarTouched = false;

    private TextView textViewGold;
    private TextView textViewSilver;
    private TextView textViewCopper;

    private TextView textViewExp;

    private TextView textViewLvl;
    private ImageView imageViewAddLvl;
    private ImageView imageViewSubLvl;

    public PocketFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pocket, container, false);

        charId = ((MainActivity) requireActivity()).characterId;

        textViewGold = rootView.findViewById(R.id.textView_gold);
        textViewGold.setOnClickListener(new OnValueClickListener(PocketBottomSheetDialog.CURRENCY, Currency.TYPE_GOLD));
        textViewSilver = rootView.findViewById(R.id.textView_silver);
        textViewSilver.setOnClickListener(new OnValueClickListener(PocketBottomSheetDialog.CURRENCY, Currency.TYPE_SILVER));
        textViewCopper = rootView.findViewById(R.id.textView_bronze);
        textViewCopper.setOnClickListener(new OnValueClickListener(PocketBottomSheetDialog.CURRENCY, Currency.TYPE_COPPER));

        seekBarGold = rootView.findViewById(R.id.seekBar_gold);
        seekBarGold.setOnSeekBarChangeListener(new CurrencySeekBarListener(Currency.TYPE_GOLD));

        seekBarSilver = rootView.findViewById(R.id.seekBar_silver);
        seekBarSilver.setOnSeekBarChangeListener(new CurrencySeekBarListener(Currency.TYPE_SILVER));

        seekBarCopper = rootView.findViewById(R.id.seekBar_bronze);
        seekBarCopper.setOnSeekBarChangeListener(new CurrencySeekBarListener(Currency.TYPE_COPPER));


        textViewExp = rootView.findViewById(R.id.textView_exp);
        textViewExp.setOnClickListener(new OnValueClickListener(PocketBottomSheetDialog.EXPERIENCE, null));
        seekBarExp = rootView.findViewById(R.id.seekBar_exp);
        seekBarExp.setOnSeekBarChangeListener(new ExpSeekBarListener());

        textViewLvl = rootView.findViewById(R.id.textView_value_lvl);
        textViewLvl.setOnClickListener(new LevelOnClickListener(LevelOnClickListener.CHANGE_VALUE));
        imageViewAddLvl = rootView.findViewById(R.id.imageView_plus_lvl);
        imageViewAddLvl.setOnClickListener(new LevelOnClickListener(LevelOnClickListener.ADD));
        imageViewSubLvl = rootView.findViewById(R.id.imageView_minus_lvl);
        imageViewSubLvl.setOnClickListener(new LevelOnClickListener(LevelOnClickListener.SUB));

        viewModel = new ViewModelProvider(this,
                new PocketViewModel.PocketViewModelFactory(requireActivity().getApplication(), charId))
                .get(PocketViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllCurrency().observe(getViewLifecycleOwner(), new Observer<List<Currency>>() {
            @Override
            public void onChanged(List<Currency> currencies) {
                viewModel.setCurrencies(currencies);
                checkForCurrencies();

                updateCurrencyText(currencies);
                updateCurrencySeekBarProgress(currencies);
            }
        });

        viewModel.getExperienceLiveData().observe(getViewLifecycleOwner(), new Observer<Experience>() {
            @Override
            public void onChanged(Experience experience) {
                checkForExperience(experience);

                updateExperienceText(experience);
                updateExperienceSeekBarProgress(experience);
            }
        });

        viewModel.getLevelLiveData().observe(getViewLifecycleOwner(), new Observer<Level>() {
            @Override
            public void onChanged(Level level) {
                checkForLevel(level);

                viewModel.setLevel(level);
                updateLevelText(level);
            }
        });
    }

    private void checkForCurrencies() {
        if (viewModel.getCurrencies().isEmpty()) {
            viewModel.insert(new Currency("0", "100", Currency.TYPE_GOLD, charId));
            viewModel.insert(new Currency("0", "100", Currency.TYPE_SILVER, charId));
            viewModel.insert(new Currency("0", "100", Currency.TYPE_COPPER, charId));
        }
    }

    private void updateCurrencyText(List<Currency> currencies) {
        for (Currency c : currencies) {

            String value;
            if (c.getValue() == null || c.getValue().isEmpty()) {
                value = "0";
            } else {
                value = c.getValue();
            }

            if (c.getType().equals(Currency.TYPE_GOLD)) {
                textViewGold.setText(value);
            }
            if (c.getType().equals(Currency.TYPE_SILVER)) {
                textViewSilver.setText(value);
            }
            if (c.getType().equals(Currency.TYPE_COPPER)) {
                textViewCopper.setText(value);
            }
        }
    }

    private void updateCurrencySeekBarProgress(List<Currency> currencies) {
        if (isGoldSeekBarTouched)
            return;

        int value;
        int maxValue;
        for (Currency c : currencies) {
            // Null and empty check for maxValue
            if (c.getMaxValue() == null || c.getMaxValue().isEmpty()) {
                maxValue = 100;
            } else {
                maxValue = Integer.parseInt(c.getMaxValue());
            }

            // Null and empty check for value
            if (c.getValue() == null || c.getValue().isEmpty()) {
                value = 0;
            } else {
                value = Integer.parseInt(c.getValue());
            }

            if (c.getType().equals(Currency.TYPE_GOLD)) {
                seekBarGold.setProgress(value);
                seekBarGold.setMax(maxValue);
            }
            if (c.getType().equals(Currency.TYPE_SILVER)) {
                seekBarSilver.setProgress(value);
                seekBarSilver.setMax(maxValue);
            }
            if (c.getType().equals(Currency.TYPE_COPPER)) {
                seekBarCopper.setProgress(value);
                seekBarCopper.setMax(maxValue);
            }
        }
    }

    // Experience
    private void checkForExperience(Experience experience) {
        if (experience == null) {
            viewModel.insert(new Experience(0, 100, charId));
        }
    }

    private void updateExperienceText(Experience experience) {
        if (experience == null)
            return;

        String expVal = experience.getValue() + "/" + experience.getMaxValue();
        textViewExp.setText(expVal);
    }

    private void updateExperienceSeekBarProgress(Experience experience) {
        if (isExpSeekBarTouched || experience == null)
            return;

        seekBarExp.setMax(experience.getMaxValue());
        seekBarExp.setProgress(experience.getValue());
    }

    // Level
    private void checkForLevel(Level level) {
        if (level == null) {
            viewModel.insert(new Level(1, charId));
        }
    }

    private void updateLevelText(Level level) {
        if (level == null)
            return;

        String levelValue = String.valueOf(level.getValue());
        textViewLvl.setText(levelValue);
    }

    /**
     * Inner classes
     */

    private class CurrencySeekBarListener implements SeekBar.OnSeekBarChangeListener {

        private String type;

        CurrencySeekBarListener(String type) {
            this.type = type;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            if (fromUser) {
                viewModel.updateCurrency(String.valueOf(i), charId, type);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isGoldSeekBarTouched = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isGoldSeekBarTouched = false;
        }
    }

    private class ExpSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            if (fromUser) {
                viewModel.updateExperience(i, charId);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            isExpSeekBarTouched = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            isExpSeekBarTouched = false;
        }
    }

    private class OnValueClickListener implements View.OnClickListener {

        private int option;
        private String currencyType;

        OnValueClickListener(int option, String currencyType) {
            this.option = option;
            this.currencyType = currencyType;
        }

        @Override
        public void onClick(View view) {
            showBottomDialog();
        }

        private void showBottomDialog() {
            PocketBottomSheetDialog bottomSheetDialog =
                    new PocketBottomSheetDialog(viewModel, charId);
            bottomSheetDialog.setOption(this.option);
            bottomSheetDialog.setCurrencyType(this.currencyType);
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_POCKET");
        }
    }

    private class LevelOnClickListener implements View.OnClickListener {

        private int option;

        public static final int ADD = 0;
        public static final int SUB = 1;
        public static final int CHANGE_VALUE = 2;

        LevelOnClickListener(int option) {
            this.option = option;
        }

        @Override
        public void onClick(View view) {
            switch (option) {
                case ADD:
                    addLevel();
                    break;
                case SUB:
                    subLevel();
                    break;
                case CHANGE_VALUE:
                    showBottomDialog();
                    break;
            }
        }

        private void addLevel() {
            int oldValue = viewModel.getLevel().getValue();
            viewModel.updateLevel(oldValue + 1, charId);
        }

        private void subLevel() {
            int oldValue = viewModel.getLevel().getValue();
            viewModel.updateLevel(oldValue - 1, charId);
        }

        private void showBottomDialog() {
            PocketBottomSheetDialog bottomSheetDialog =
                    new PocketBottomSheetDialog(viewModel, charId);
            bottomSheetDialog.setOption(PocketBottomSheetDialog.LEVEL);
            bottomSheetDialog.show(getParentFragmentManager(), "BOTTOM_DIALOG_POCKET");
        }
    }
}
