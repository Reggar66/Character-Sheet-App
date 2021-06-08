package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket;

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
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Currency;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Experience;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.model.Level;
import com.awkwardlydevelopedapps.unicharsheet.inventory.pocket.viewModel.PocketViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PocketFragment extends Fragment
        implements PocketBottomSheetDialog.NoticeDialogListener {

    private View rootView;
    private int characterID;
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

    public PocketFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pocket, container, false);

        DataHolderViewModel dataHolderViewModel = new ViewModelProvider(requireActivity())
                .get(DataHolderViewModel.class);

        characterID = dataHolderViewModel.getCharacterID();

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
        ImageView imageViewAddLvl = rootView.findViewById(R.id.imageView_plus_lvl);
        imageViewAddLvl.setOnClickListener(new LevelOnClickListener(LevelOnClickListener.ADD));
        ImageView imageViewSubLvl = rootView.findViewById(R.id.imageView_minus_lvl);
        imageViewSubLvl.setOnClickListener(new LevelOnClickListener(LevelOnClickListener.SUB));

        viewModel = new ViewModelProvider(this,
                new PocketViewModel.PocketViewModelFactory(requireActivity().getApplication(), characterID))
                .get(PocketViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllCurrency().observe(getViewLifecycleOwner(), currencies -> {
            viewModel.setCurrencies(currencies);
            checkForCurrencies();

            updateCurrencyText(currencies);
            updateCurrencySeekBarProgress(currencies);

            for (Currency c : currencies) {
                switch (c.getType()) {
                    case Currency.TYPE_GOLD:
                        viewModel.setGold(c);
                        break;
                    case Currency.TYPE_SILVER:
                        viewModel.setSilver(c);
                        break;
                    case Currency.TYPE_COPPER:
                        viewModel.setCopper(c);
                        break;
                }
            }
        });

        viewModel.getExperienceLiveData().observe(getViewLifecycleOwner(), experience -> {
            checkForExperience(experience);

            updateExperienceText(experience);
            updateExperienceSeekBarProgress(experience);

            viewModel.setExperience(experience);
        });

        viewModel.getLevelLiveData().observe(getViewLifecycleOwner(), level -> {
            checkForLevel(level);

            viewModel.setLevel(level);
            updateLevelText(level);
        });
    }

    private void checkForCurrencies() {
        if (viewModel.getCurrencies().isEmpty()) {
            viewModel.insert(new Currency("0", "100", Currency.TYPE_GOLD, characterID));
            viewModel.insert(new Currency("0", "100", Currency.TYPE_SILVER, characterID));
            viewModel.insert(new Currency("0", "100", Currency.TYPE_COPPER, characterID));
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
            viewModel.insert(new Experience(0, 100, characterID));
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
            viewModel.insert(new Level(1, characterID));
        }
    }

    private void updateLevelText(Level level) {
        if (level == null)
            return;

        String levelValue = String.valueOf(level.getValue());
        textViewLvl.setText(levelValue);
    }

    @Override
    public void onPositiveClickListener(int option, @NotNull String updateValue, @NotNull String updateMaxValue, String currencyType) {
        switch (option) {
            case PocketBottomSheetDialog.CURRENCY:
                viewModel.updateCurrencyWithMaxValue(
                        updateValue,
                        updateMaxValue,
                        characterID,
                        currencyType
                );
                break;
            case PocketBottomSheetDialog.EXPERIENCE:
                viewModel.updateExperienceWithMaxValue(
                        Integer.parseInt(updateValue),
                        Integer.parseInt(updateMaxValue),
                        characterID
                );
                break;
            case PocketBottomSheetDialog.LEVEL:
                viewModel.updateLevel(Integer.parseInt(updateValue), characterID);
                break;
        }
    }

    /**
     * Inner classes
     */

    private class CurrencySeekBarListener implements SeekBar.OnSeekBarChangeListener {

        private final String type;

        CurrencySeekBarListener(String type) {
            this.type = type;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
            if (fromUser) {
                viewModel.updateCurrency(String.valueOf(i), characterID, type);
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
                viewModel.updateExperience(i, characterID);
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

        private final int option;
        private final String currencyType;

        private String oldValue = "0";
        private String oldMaxValue = "0";

        OnValueClickListener(int option, String currencyType) {
            this.option = option;
            this.currencyType = currencyType;
        }

        private void oldValues() {
            if (option == PocketBottomSheetDialog.EXPERIENCE) {
                oldValue = String.valueOf(viewModel.getExperience().getValue());
                oldMaxValue = String.valueOf(viewModel.getExperience().getMaxValue());
            } else if (option == PocketBottomSheetDialog.CURRENCY) {
                switch (currencyType) {
                    case Currency.TYPE_GOLD:
                        oldValue = String.valueOf(viewModel.getGold().getValue());
                        oldMaxValue = String.valueOf(viewModel.getGold().getMaxValue());
                        break;

                    case Currency.TYPE_SILVER:
                        oldValue = String.valueOf(viewModel.getSilver().getValue());
                        oldMaxValue = String.valueOf(viewModel.getSilver().getMaxValue());
                        break;

                    case Currency.TYPE_COPPER:
                        oldValue = String.valueOf(viewModel.getCopper().getValue());
                        oldMaxValue = String.valueOf(viewModel.getCopper().getMaxValue());
                        break;
                }
            }


            if (oldValue.isEmpty()) {
                oldValue = "0";
            }

            if ((oldMaxValue.isEmpty())) {
                oldMaxValue = "0";
            }
        }

        @Override
        public void onClick(View view) {
            oldValues();
            showBottomDialog();
        }

        private void showBottomDialog() {

            PocketBottomSheetDialog bottomSheetDialog =
                    PocketBottomSheetDialog.Companion.newInstance(
                            this.option,
                            this.currencyType,
                            oldValue,
                            oldMaxValue
                    );
            bottomSheetDialog.show(getChildFragmentManager(), "BOTTOM_DIALOG_POCKET");
        }
    }

    private class LevelOnClickListener implements View.OnClickListener {

        private final int option;

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
            viewModel.updateLevel(oldValue + 1, characterID);
        }

        private void subLevel() {
            int oldValue = viewModel.getLevel().getValue();
            viewModel.updateLevel(oldValue - 1, characterID);
        }

        private void showBottomDialog() {
            PocketBottomSheetDialog bottomSheetDialog =
                    PocketBottomSheetDialog.Companion.newInstance(
                            PocketBottomSheetDialog.LEVEL,
                            null,
                            String.valueOf(viewModel.getLevel().getValue()),
                            null
                    );
            bottomSheetDialog.show(getChildFragmentManager(), "BOTTOM_DIALOG_POCKET");
        }
    }
}
