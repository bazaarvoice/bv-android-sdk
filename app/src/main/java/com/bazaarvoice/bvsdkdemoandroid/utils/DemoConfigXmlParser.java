/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.content.Context;
import android.util.Log;

import com.bazaarvoice.bvsdkdemoandroid.DemoConstants;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import static android.content.ContentValues.TAG;

class DemoConfigXmlParser {

    private static List<DemoConfig> savedConfigList = null;

    private static final String DEMO_APP_CONFIG_FILE = "DemoAppConfigs.plist";
    private static final String demoApiKeyConversations = DemoConstants.PASSKEY_CONVERSATIONS;
    private static final String demoApiKeyConversationsStores = DemoConstants.PASSKEY_CONVERSATIONS_STORES;
    private static final String demoApiKeyCurations = DemoConstants.PASSKEY_CURATIONS;
    private static final String demoApiKeyShopperAdvertising = DemoConstants.PASSKEY_SHOPPER_AD;
    private static final String demoApiKeyLocation = DemoConstants.PASSKEY_LOCATION;
    private static final String demoApiKeyPin = DemoConstants.PASSKEY_PIN;
    private static final String demoClientId = DemoConstants.BV_CLIENT_ID;
    public static final String demoDisplayName = "demo";
    private static final DemoConfig demoConfig = new DemoConfig(demoApiKeyConversations, demoApiKeyConversationsStores, demoApiKeyCurations, demoApiKeyShopperAdvertising, demoApiKeyLocation, demoApiKeyPin, demoClientId, demoDisplayName);

    public static DemoConfig getConfigFromClientId(Context context, String clientId) {
        List<DemoConfig> configList = getConfigList(context);
        for (DemoConfig config : configList) {
            if (config.clientId.equals(clientId)) {
                return config;
            }
        }
        return getDefaultConfig(context);
    }

    public static DemoConfig getDefaultConfig(Context context) {
        List<DemoConfig> configList = getConfigList(context);
        return configList.get(0);
    }

    public static boolean configFileExists(Context context) {
        boolean exists = false;
        try {
            String[] assetFileNames = context.getAssets().list("");
            for (int i=0; i<assetFileNames.length; i++) {
                String assetFileName = assetFileNames[i];
                if (assetFileName.equals(DEMO_APP_CONFIG_FILE)) {
                    exists = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public static List<DemoConfig> getConfigList(Context context) {
        if (savedConfigList != null) {
            return savedConfigList;
        }

        List<DemoConfig> configList = new ArrayList<>();
        configList.add(demoConfig);

        try {
            InputStream inputStream = context.getAssets().open(DEMO_APP_CONFIG_FILE);
            NSArray nsArray = (NSArray) PropertyListParser.parse(inputStream);
            configList.addAll(nsArrayToConfigList(nsArray));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (PropertyListFormatException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        savedConfigList = configList;
        return configList;
    }

    private static List<DemoConfig> nsArrayToConfigList(NSArray nsArray) {
        List<DemoConfig> configList = new ArrayList<>();

        for (NSObject nsObject : nsArray.getArray()) {
            if (nsObject instanceof NSDictionary) {
                NSDictionary nsDictionary = (NSDictionary) nsObject;
                DemoConfig config = nsDictionaryToConfig(nsDictionary);
                if (config == null) {
                    continue;
                }
                configList.add(config);
            }
        }

        return configList;
    }

    private static DemoConfig nsDictionaryToConfig(NSDictionary nsDictionary) {
        if (nsDictionary == null) {
            return null;
        }

        if (!nsDictionary.containsKey("clientId")) {
            return null;
        }


        DemoConfig config = new DemoConfig();
        for (String key : nsDictionary.allKeys()) {
            Field field = null;
            try {
                field = config.getClass().getField(key);
                Object objectValue = nsDictionary.get(key).toJavaObject();
                if (objectValue instanceof String) {
                    field.set(config, objectValue);
                }
            } catch (NoSuchFieldException e) {
                Log.d(TAG, key + " does not exist as a field in our Android DemoConfig POJO, so will not be added");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

}
