package edu.fptu.thomas.dang.repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public abstract class DataMiner {

    protected Path filePath;

    public void openFile(String path) {
        this.filePath = Paths.get(path);
    }

    public abstract Map parseData();

}
