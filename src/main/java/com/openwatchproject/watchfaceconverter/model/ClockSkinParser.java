package com.openwatchproject.watchfaceconverter.model;

import javafx.scene.control.TextArea;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.openwatchproject.watchfaceconverter.JavaFXUtils.log;

public class ClockSkinParser {
    private static final Logger LOGGER = Logger.getLogger(ClockSkinParser.class.getName());
    private final TextArea textArea;

    public ClockSkinParser(TextArea textArea) {
        this.textArea = textArea;
    }

    public void parseClockSkin(ClockSkin clockSkin) {
        try (InputStream is = clockSkin.getClockSkinFile(ClockSkinConstants.CLOCK_SKIN_XML)) {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "clockskin");

            boolean invertDirection = false;
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals(ClockSkinConstants.TAG_DRAWABLE)) {
                    //log(LOGGER, textArea, Level.INFO, "parseClockSkin: new drawable!");
                    ClockSkinItem clockSkinItem = parseDrawableTag(parser, clockSkin);
                    if (clockSkinItem.getDirection() == ClockSkinConstants.DIRECTION_REVERSE) {
                        if (invertDirection) {
                            //clockSkinItem.setDirection(ClockSkinConstants.DIRECTION_NORMAL);
                            invertDirection = false;
                        } else {
                            invertDirection = true;
                        }
                    } else if (invertDirection) {
                        //clockSkinItem.setDirection(ClockSkinConstants.DIRECTION_REVERSE);
                    }
                    //clockSkinItem.setTimeZone(calendar.getTimeZone());
                    clockSkin.addClockSkinItem(clockSkinItem);
                    //log(LOGGER, textArea, Level.INFO, "parseClockSkin: finished parsing drawable");
                }
            }
        } catch (XmlPullParserException | IOException e) {
            log(LOGGER, textArea, Level.INFO, "parseClockSkin: error while parsing the ClockSkin!");
            e.printStackTrace();
        }
        log(LOGGER, textArea, Level.INFO, "parseClockSkin: finished parsing!");
    }

    private void parseDrawableArray(ClockSkin clockSkin, ClockSkinItem clockSkinItem, String name) {
        try (InputStream is = clockSkin.getClockSkinFile(name)) {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, ClockSkinConstants.TAG_DRAWABLES);
            List<String> drawables = new ArrayList<>();
            while (parser.next() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals(ClockSkinConstants.TAG_IMAGE)) {
                    String image = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableArray: image = " + image);
                    drawables.add(image);
                }
            }
            clockSkinItem.setDrawables(drawables);
        } catch (IOException | XmlPullParserException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private ClockSkinItem parseDrawableTag(XmlPullParser parser, ClockSkin clockSkin) throws IOException, XmlPullParserException {
        ClockSkinItem clockSkinItem = new ClockSkinItem();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            switch (parser.getName()) {
                case ClockSkinConstants.TAG_NAME:
                    String repeat = parser.getAttributeValue(null, "repeat");
                    if (repeat != null) {
                        log(LOGGER, textArea, Level.INFO, "parseDrawableTag: repeat = " + repeat);
                        clockSkinItem.setRepeat(Integer.parseInt(repeat));
                    }
                    String name = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: name = " + name);
                    clockSkinItem.setName(name);
                    if (name.endsWith(".xml")) {
                        parseDrawableArray(clockSkin, clockSkinItem, name);
                    } else {
                        clockSkinItem.setDrawable(name);
                    }
                    break;
                case ClockSkinConstants.TAG_ARRAY_TYPE:
                    String arrayType = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: arrayType = " + arrayType);
                    clockSkinItem.setArrayType(Integer.parseInt(arrayType));
                    break;
                case ClockSkinConstants.TAG_CENTERX:
                    String centerX = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: centerX = " + centerX);
                    clockSkinItem.setCenterX(Integer.parseInt(centerX));
                    break;
                case ClockSkinConstants.TAG_CENTERY:
                    String centerY = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: centerY = " + centerY);
                    clockSkinItem.setCenterY(Integer.parseInt(centerY));
                    break;
                case ClockSkinConstants.TAG_ROTATE:
                    String rotate = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: rotate = " + rotate);
                    clockSkinItem.setRotate(Integer.parseInt(rotate));
                    break;
                case ClockSkinConstants.TAG_OFFSET_ANGLE:
                    String offsetAngle = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: offsetAngle = " + offsetAngle);
                    clockSkinItem.setOffsetAngle(Float.parseFloat(offsetAngle));
                    break;
                case ClockSkinConstants.TAG_MUL_ROTATE:
                    String mulRotate = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: mulRotate = " + mulRotate);
                    clockSkinItem.setMulRotate(Integer.parseInt(mulRotate));
                    break;
                case ClockSkinConstants.TAG_START_ANGLE:
                    String startAngle = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: startAngle = " + startAngle);
                    clockSkinItem.setStartAngle(Integer.parseInt(startAngle));
                    break;
                case ClockSkinConstants.TAG_DIRECTION:
                    String direction = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: direction = " + direction);
                    clockSkinItem.setDirection(Integer.parseInt(direction));
                    break;
                case ClockSkinConstants.TAG_TEXT_SIZE:
                    String textSize = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: textSize = " + textSize);
                    clockSkinItem.setTextSize(Integer.parseInt(textSize));
                    break;
                case ClockSkinConstants.TAG_TEXT_COLOR:
                    String textColor = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: textColor = " + textColor);
                    //clockSkinItem.setTextColor(textColor);
                    break;
                case ClockSkinConstants.TAG_COLOR_ARRAY:
                    String colorArray = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: colorArray = " + colorArray);
                    clockSkinItem.setColorArray(colorArray);
                    break;
                case ClockSkinConstants.TAG_COLOR:
                    String color = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: color = " + color);
                    clockSkinItem.setColor(Integer.parseInt(color));
                    break;
                case ClockSkinConstants.TAG_WIDTH:
                    String width = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: width = " + width);
                    //clockSkinItem.setWidth(Integer.valueOf(width));
                    break;
                case ClockSkinConstants.TAG_RADIUS:
                    String radius = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: radius = " + radius);
                    //clockSkinItem.setRadius(radius);
                    break;
                case ClockSkinConstants.TAG_ROTATE_MODE:
                    String rotateMode = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: rotateMode = " + rotateMode);
                    //clockSkinItem.setRotateMode(Integer.valueOf(rotateMode));
                    break;
                case ClockSkinConstants.TAG_CLASS_NAME:
                    String className = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: className = " + className);
                    clockSkinItem.setClassName(className);
                    break;
                case ClockSkinConstants.TAG_PACKAGE_NAME:
                    String packageName = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: packageName = " + packageName);
                    clockSkinItem.setPackageName(packageName);
                    break;
                case ClockSkinConstants.TAG_RANGE:
                    String range = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: range = " + range);
                    clockSkinItem.setRange(Integer.parseInt(range));
                    break;
                case ClockSkinConstants.TAG_FRAMERATE:
                    String framerate = parser.nextText();
                    log(LOGGER, textArea, Level.INFO, "parseDrawableTag: framerate = " + framerate);
                    clockSkinItem.setFramerate(Double.parseDouble(framerate));
                default:
                    parser.nextText();
            }
        }

        return clockSkinItem;
    }
}
