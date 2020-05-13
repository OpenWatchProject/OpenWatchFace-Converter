package com.openwatchproject.watchfaceconverter.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.openwatchproject.watchfaceconverter.model.ClockSkinConstants.CLOCK_SKIN_XML;

public class ClockSkin {
    private transient File folder;
    private List<ClockSkinItem> clockSkinItems;

    public ClockSkin(File folder) {
        this.folder = folder;
    }

    public InputStream getClockSkinFile(String name) {
        try {
            return new FileInputStream(new File(folder, name));
        } catch (FileNotFoundException e) {
            System.out.println("getClockSkinFile: folder not found: " + name);
        }

        return null;
    }

    public void addClockSkinItem(ClockSkinItem clockSkinItem) {
        if (clockSkinItems == null) {
            clockSkinItems = new ArrayList<>();
        }

        clockSkinItems.add(clockSkinItem);
    }

    public List<ClockSkinItem> getClockSkinItems() {
        return clockSkinItems;
    }


    public boolean isValid() {
        return folder.exists() && folder.isDirectory() && folder.listFiles().length > 2 && new File(folder, CLOCK_SKIN_XML).exists();
    }
}
