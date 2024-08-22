package com.urise.webapp.storage;

import java.io.File;

public class StrategyStreamStorageFactory {
    private static final String STORAGE_PATH = "basejava/storage";

    public static Storage getStrategy(StrategyType type) {
        return switch (type) {
            case StrategyType.FILE -> new ObjectStreamFileStorage(new File(STORAGE_PATH));
            case StrategyType.PATH -> new ObjectStreamPathStorage(STORAGE_PATH);
        };
    }
}
