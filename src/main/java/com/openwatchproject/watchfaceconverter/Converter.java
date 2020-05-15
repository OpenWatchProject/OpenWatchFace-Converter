package com.openwatchproject.watchfaceconverter;

import com.google.gson.Gson;
import com.openwatchproject.watchfaceconverter.model.*;
import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.openwatchproject.watchfaceconverter.JavaFXUtils.log;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Converter {
    private static final Logger LOGGER = Logger.getLogger(Converter.class.getName());
    private final static String EXTENSION = ".owf";

    private final TextArea logTextArea;
    private final ClockSkinParser clockSkinParser;

    public Converter(TextArea logTextArea) {
        this.logTextArea = logTextArea;
        this.clockSkinParser = new ClockSkinParser(logTextArea);
    }

    public String convert(File inputFolder, File outputFolder, ClockSkinType type, int width, int height, OpenWatchWatchFaceMetadata metadata) {
        String filename = inputFolder.getName() + EXTENSION;
        File outputFile = new File(outputFolder, filename);

        log(LOGGER, logTextArea, Level.INFO, "Starting the conversion of \"" + filename + "\"...");
        log(LOGGER, logTextArea, Level.INFO, "Using width = " + width + "px, height = " + height + "px");

        ClockSkin clockSkin = new ClockSkin(inputFolder);
        if (!clockSkin.isValid()) {
            return null;
        }

        clockSkinParser.parseClockSkin(clockSkin);

        OpenWatchWatchFace owwf = new OpenWatchWatchFace(width, height);
        try {
            switch (type) {
                case STOCK:
                    log(LOGGER, logTextArea, Level.INFO, "Using stock type...");
                    convertStock(inputFolder, outputFile, metadata, clockSkin, owwf);
                    break;
                case ERICS:
                    log(LOGGER, logTextArea, Level.INFO, "Using Eric's type...");
                    convertErics(inputFolder, outputFile, metadata, clockSkin, owwf);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile.getAbsolutePath();
    }

    private void convertStock(File inputFolder, File outputFile, OpenWatchWatchFaceMetadata metadata, ClockSkin clockSkin, OpenWatchWatchFace owwf) throws IOException {
        int direction = 0;
        for (ClockSkinItem csItem : clockSkin.getClockSkinItems()) {
            OpenWatchWatchFace.Item item = new OpenWatchWatchFace.Item();

            if (csItem.drawables != null && csItem.drawables.size() > 0) {
                item.addAllFrames(csItem.drawables);
            } else if (csItem.drawable != null) {
                item.addFrame(csItem.drawable);
            } else {
                System.out.println("Invalid item! skipping...");
                continue;
            }

            if (csItem.arrayType == 0 && csItem.rotate == 0) {
                item.setType(-1);
            } else {
                item.setType(csItem.arrayType);
            }
            item.setCenterX(csItem.centerX);
            item.setCenterY(csItem.centerY);
            if (csItem.direction == ClockSkinConstants.DIRECTION_REVERSE) {
                if (direction == 0) {
                    direction = 1;
                } else {
                    direction = 0;
                }
            }
            item.setDirection(direction);
            item.setPackageName(csItem.packageName);
            item.setClassName(csItem.className);
            item.setRange(csItem.range);
            item.setRotationFactor(csItem.mulRotate);
            item.setRotatableType(csItem.rotate);

            owwf.addItem(item);
        }

        exportWatchFace(inputFolder, outputFile, metadata, owwf);
    }

    private void convertErics(File inputFolder, File outputFile, OpenWatchWatchFaceMetadata metadata, ClockSkin clockSkin, OpenWatchWatchFace owwf) throws IOException {
        for (ClockSkinItem csItem : clockSkin.getClockSkinItems()) {
            OpenWatchWatchFace.Item item = new OpenWatchWatchFace.Item();

            if (csItem.drawables != null && csItem.drawables.size() > 0) {
                item.addAllFrames(csItem.drawables);
            } else if (csItem.drawable != null) {
                item.addFrame(csItem.drawable);
            } else {
                System.out.println("Invalid item! skipping...");
                continue;
            }

            if (csItem.arrayType == 0 && csItem.rotate == 0) {
                item.setType(-1);
            } else {
                item.setType(csItem.arrayType);
            }
            item.setCenterX(csItem.centerX);
            item.setCenterY(csItem.centerY);
            item.setDirection(csItem.direction - 1);
            item.setPackageName(csItem.packageName);
            item.setClassName(csItem.className);
            item.setRange(csItem.range);
            item.setRotationFactor(csItem.mulRotate);
            item.setRotatableType(csItem.rotate);

            owwf.addItem(item);
        }

        exportWatchFace(inputFolder, outputFile, metadata, owwf);
    }

    private boolean exportWatchFace(File inputFolder, File outputFile, OpenWatchWatchFaceMetadata metadata, OpenWatchWatchFace owwf) throws IOException {
        File tmpDir = Files.createTempDirectory(null).toFile();

        writeToFile(new File(tmpDir, "watchface.json"), new Gson().toJson(owwf));
        writeToFile(new File(tmpDir, "metadata.json"), new Gson().toJson(metadata));
        Files.copy(new File(inputFolder, "clock_skin_model.png").toPath(), new File(tmpDir, "preview.png").toPath(), REPLACE_EXISTING);

        for (OpenWatchWatchFace.Item item : owwf.getItems()) {
            for (String frame : item.getFrames()) {
                Files.copy(new File(inputFolder, frame).toPath(), new File(tmpDir, frame).toPath(), REPLACE_EXISTING);
            }
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFile))) {
            zipDirectory(tmpDir, null, zos);
        }

        deleteDirectory(tmpDir);
        return true;
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private static void zipDirectory(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {
            if (fileName != null) {
                if (fileName.endsWith("/")) {
                    zipOut.putNextEntry(new ZipEntry(fileName));
                    zipOut.closeEntry();
                } else {
                    zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                    zipOut.closeEntry();
                }
                File[] children = fileToZip.listFiles();
                for (File childFile : children) {
                    zipDirectory(childFile, fileName + "/" + childFile.getName(), zipOut);
                }
            } else {
                File[] children = fileToZip.listFiles();
                for (File childFile : children) {
                    zipDirectory(childFile, childFile.getName(), zipOut);
                }
            }

            return;
        }

        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private static void writeToFile(File file, String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
