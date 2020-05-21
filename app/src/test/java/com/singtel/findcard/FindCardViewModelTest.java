package com.singtel.findcard;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

@RunWith(PowerMockRunner.class)
public class FindCardViewModelTest {

    @Rule
    public TestRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    FindCardViewModel viewModel;

    MutableLiveData<Integer> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Integer>> listMutableLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        viewModel = new FindCardViewModel();
    }

    @Test
    public void testObserveRefreshMazeLiveData() {
        Assert.assertNotNull(viewModel.observeRefreshMazeLiveData());

    }

    @Test
    public void testObserveMazeNumberListLiveData() {
        Assert.assertNotNull(viewModel.observeMazeNumberListLiveData());
    }

    @Test
    public void pickRandomNumber() {
        Assert.assertEquals(3, viewModel.pickRandomNumber(6).size());
        Assert.assertNull(viewModel.pickRandomNumber(7));
    }


    @Test
    public void testObserveGameCompletionLiveData() {
        Assert.assertNotNull(viewModel.observeGameCompletionLiveData());
    }

    @Test
    public void testObserveStepCountLiveData() {
        Assert.assertNotNull(viewModel.observeStepCountLiveData());
    }

    @Test
    public void testResetData() {
        viewModel.resetData();
        Assert.assertNull(viewModel.selectedCard);
        Assert.assertEquals(0, viewModel.stepsCount);
        Assert.assertEquals(0, viewModel.validSelectionList.size());
    }

    @Test
    public void testOnCardClicked_1Click() {
        Card card = new Card();
        card.setCardValue(10);

        viewModel.onCardClicked(card);
        Assert.assertNotNull(viewModel.selectedCard);
        Assert.assertEquals(1, viewModel.stepsCount);

        Card card1 = new Card();
        card1.setCardValue(10);
        viewModel.onCardClicked(card1);

        Assert.assertNull(viewModel.selectedCard);
        Assert.assertEquals(2, viewModel.validSelectionList.size());
        Assert.assertEquals(2, viewModel.stepsCount);



    }
}

