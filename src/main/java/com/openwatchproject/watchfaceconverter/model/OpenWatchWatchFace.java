package com.openwatchproject.watchfaceconverter.model;

import java.util.ArrayList;
import java.util.List;

public class OpenWatchWatchFace {
    /**
     * The height in pixels for which this WatchFace was designed for.
     */
    private int height = 400;

    /**
     * The width in pixels for which this WatchFace was designed for.
     */
    private int width = 400;

    /**
     * The items that form this WatchFace.
     */
    private ArrayList<Item> items;

    public OpenWatchWatchFace(int width, int height) {
        this.width = width;
        this.height = height;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public static class Item {
        /**
         * Indicates the type of the item.
         * 0 = analog
         * 1..* = digital
         *
         * Equivalent to ClockSkin's arraytype
         */
        private int type;

        /**
         * Indicates the (width) center for the item in pixels
         */
        private int centerX;

        /**
         * Indicates the (height) center for the item in pixels
         */
        private int centerY;

        /**
         * Indicates the direction for the analog hand.
         * 0 = Clockwise
         * 1 = Anti-Clockwise
         *
         * Only valid if type == 0!
         */
        private int direction;

        /**
         * Indicates the package containing the activity to be launched.
         *
         * Only valid if type == 100!
         *
         * Equivalent to ClockSkin's pkg
         */
        private String packageName;

        /**
         * Indicates the class for the activity to be launched.
         *
         * Only valid if type == 100 and packageName is valid!
         *
         * Equivalent to ClockSkin's cls
         */
        private String className;

        /**
         * Indicates the range (radius) for the invisible button
         * that this item represents.
         *
         * Only valid if type == 100, packageName is valid and className is valid!
         */
        private int range;

        /**
         * Indicates the factor for which the degrees will be multiplied/divided.
         * A value greater than 0 (rotationFactor > 0) will be multiplied.
         * A value less than 0 (rotationFactor < 0) will be divided.
         * A value equal to 0 will be ignored.
         *
         * Equivalent to ClockSkin's mulrotate
         *
         * Only valid if type == 0
         */
        private int rotationFactor;

        /**
         * An array of frames that need to be displayed
         * Frame count must be at least 1. If it's greater than 1, it's an animation.
         */
        private ArrayList<String> frames;

        private int color;
        private String colorArray;
        private String drawable;
        private float offsetAngle;
        private int rotate;
        private int startAngle;
        private int textSize;
        private int duration;
        private int count;
        private String valueType;
        private float progressDiliverArc;
        private int progressdiliverCount;
        private int progressRadius;
        private String progressStroken;
        private String pictureShadow;
        private int frameDuration;
        private String childFolder;
        private List<String> drawables;
        private float angle;
        private int width;
        private int radius;
        private int rotateMode;
        private int repeat;

        public Item() {
            this.frames = new ArrayList<String>();
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setCenterX(int centerX) {
            this.centerX = centerX;
        }

        public void setCenterY(int centerY) {
            this.centerY = centerY;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public void setRotationFactor(int rotationFactor) {
            this.rotationFactor = rotationFactor;
        }

        public void addFrame(String frame) {
            this.frames.add(frame);
        }

        public void addAllFrames(List<String> frames) {
            this.frames.addAll(frames);
        }

        public ArrayList<String> getFrames() {
            return frames;
        }
    }
}
