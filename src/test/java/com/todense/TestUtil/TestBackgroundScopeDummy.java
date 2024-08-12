package com.todense.TestUtil;

import com.todense.viewmodel.scope.BackgroundScope;
import javafx.scene.paint.Color;

public class TestBackgroundScopeDummy extends BackgroundScope {
    @Override
    public Color getBackgroundColor() {
        return Color.WHITE;
    }
}
