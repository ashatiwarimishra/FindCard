package com.singtel.findcard;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FindCardViewModel extends ViewModel {

    private static final String TAG = FindCardViewModel.class.getSimpleName();

    private int numberOfGrid = 6;
    @VisibleForTesting Card selectedCard;
    @VisibleForTesting List<Integer> validSelectionList = new ArrayList<>();
    @VisibleForTesting int stepsCount = 0;

    private MutableLiveData<List<Integer>> mazeNumberListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> refreshMazeLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> gameCompletionLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> stepCountLiveData = new MutableLiveData<>();

    public LiveData<List<Integer>> observeRefreshMazeLiveData() {
        return refreshMazeLiveData;
    }

    public LiveData<List<Integer>> observeMazeNumberListLiveData() {
        return mazeNumberListLiveData;
    }

    public LiveData<Integer> observeGameCompletionLiveData() {
        return gameCompletionLiveData;
    }

    public LiveData<Integer> observeStepCountLiveData() {
        return stepCountLiveData;
    }

    public void prepareMazeGrid() {
        resetData();
        Set<Integer> randomNumberPair = pickRandomNumber(numberOfGrid);
        Log.e(TAG, "Random Number - "+randomNumberPair.toString());
        List<Integer> mazeNumberList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            mazeNumberList.addAll(randomNumberPair);
        }

        Collections.shuffle(mazeNumberList);
        Log.e(TAG, "Suffuled Number - "+mazeNumberList.toString());
        mazeNumberListLiveData.setValue(mazeNumberList);

    }

    public void resetData() {
        selectedCard = null;
        validSelectionList.clear();
        stepsCount = 0;
        stepCountLiveData.setValue(stepsCount);
    }

    public Set<Integer> pickRandomNumber(int numberOfGrid) {
        if (numberOfGrid % 2 != 0) {
            return null;
        }
        Random random = new Random();
        final Set<Integer> picked = new HashSet<>();
        while (picked.size() < numberOfGrid / 2) {
            picked.add(random.nextInt(99) + 1);
        }
        return picked;
    }

    public void onCardClicked(Card card) {
        stepCountLiveData.setValue(++stepsCount);
        if (selectedCard == null) {
            this.selectedCard = card;
            return;
        }

        if (selectedCard.getCardValue() == card.getCardValue()) {
            validSelectionList.add(selectedCard.getIndex());
            validSelectionList.add(card.getIndex());
            if (validSelectionList.size() == numberOfGrid) {
                gameCompletionLiveData.setValue(stepsCount);
            }
        } else {
            refreshMazeLiveData.setValue(validSelectionList);
        }
        selectedCard = null;
    }


}
