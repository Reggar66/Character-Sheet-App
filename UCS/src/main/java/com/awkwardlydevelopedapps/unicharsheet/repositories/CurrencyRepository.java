package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.CurrencyDao;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.models.Currency;

import java.util.List;

public class CurrencyRepository {

    private CurrencyDao currencyDao;
    private LiveData<List<Currency>> allCurrency;

    public CurrencyRepository(Application application, int charId) {
        currencyDao = DbSingleton.Instance(application).getCurrencyDao();
        allCurrency = currencyDao.getLiveDataAllCurrency(charId);
    }

    public void insert(Currency currency) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.insert(currency);
            }
        });
    }

    public void updateCurrency(String value, int charId, String type) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.updateCurrency(value, charId, type);
            }
        });
    }

    public void updateCurrencyWithMaxValue(String value, String maxValue, int charId, String type) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                currencyDao.updateCurrencyWithMaxValue(value, maxValue, charId, type);
            }
        });
    }

    public LiveData<List<Currency>> getAllCurrency() {
        return allCurrency;
    }
}
