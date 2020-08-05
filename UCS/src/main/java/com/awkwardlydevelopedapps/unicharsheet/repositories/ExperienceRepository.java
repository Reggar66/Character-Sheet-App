package com.awkwardlydevelopedapps.unicharsheet.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.awkwardlydevelopedapps.unicharsheet.ExecSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.DbSingleton;
import com.awkwardlydevelopedapps.unicharsheet.data.ExperienceDao;
import com.awkwardlydevelopedapps.unicharsheet.models.Experience;

public class ExperienceRepository {

    private ExperienceDao experienceDao;
    private LiveData<Experience> experience;

    public ExperienceRepository(Application application, int charId) {
        experienceDao = DbSingleton.Instance(application).getExperienceDao();
        experience = experienceDao.getExperience(charId);
    }

    public LiveData<Experience> getExperience() {
        return experience;
    }

    public void insert(Experience experience) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                experienceDao.insert(experience);
            }
        });
    }

    public void delete(Experience experience) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                experienceDao.delete(experience);
            }
        });
    }

    public void update(int value, int charId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                experienceDao.update(value, charId);
            }
        });
    }

    public void updateWithMaxValue(int value, int maxValue, int charId) {
        ExecSingleton.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                experienceDao.updateWithMaxValue(value, maxValue, charId);
            }
        });
    }

}
