package com.todense.TestUtil;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.RandomGeneratorViewModel;
import com.todense.viewmodel.random.generators.SimilarGenerator;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;

public class TestRandomGeneratorViewModelDummy extends RandomGeneratorViewModel {

    public void callGenerateAndPublishASimilarGraph(NotificationCenter notificationCenter,
                                                    SimilarGenerator similarGenerator, Graph currentgraph) {
        super.generateAndPublishASimilarGraph(notificationCenter, similarGenerator, currentgraph);
    }

}
