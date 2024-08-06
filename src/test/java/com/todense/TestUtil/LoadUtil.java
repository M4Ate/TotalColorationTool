package com.todense.TestUtil;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.file.GraphReader;
import com.todense.viewmodel.file.format.ogr.OgrReader;

import java.io.File;

public class LoadUtil {

    public static Graph loadGraphFile(String path){
        File file = new File(path);
        GraphReader graphReader = new OgrReader();
        return graphReader.readGraph(file);
    }


}
