package com.todense.TestUtil;

import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.GraphScope;

public class TestGraphScopeDummy extends GraphScope {

    @Override
    public GraphManager getGraphManager() {
        return new TestGraphManagerDummy();
    }
}
