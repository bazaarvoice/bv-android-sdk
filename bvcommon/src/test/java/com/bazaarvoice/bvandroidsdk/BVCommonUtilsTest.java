package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;

import androidx.test.core.app.ApplicationProvider;

import com.bazaarvoice.bvandroidsdk.internal.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BVCommonUtilsTest {

    @Test
    public void shouldReturnCorrectBooleanForStagingEnvironment()
    {
        assertTrue(Utils.isStagingEnvironment(BazaarEnvironment.STAGING));
        assertFalse(Utils.isStagingEnvironment(BazaarEnvironment.PRODUCTION));
    }

    @Test
    public void shouldReturnURLValueFromString() {
        String url = "http://bazaarvoice.com";
        URL toUrl = Utils.toUrl(url);
        assertEquals(url, toUrl.toString());
    }

    @Test
    public void shouldReturn0IfIntegerIsNull() {
        Integer val = 0;
        assertEquals(val, Utils.getIntegerSafe(null));
    }

    @Test
    public void shouldReturn0IfFloatIsNull() {
        Float val = 0f;
        assertEquals(val, Utils.getFloatSafe(null));
    }

    @Test
    public void shouldNotAddToMapWithNullValues() {
        Map<String, String> data = new HashMap<>();
        String key = "key";
        String value = "value";
        Utils.mapPutSafe(data, key, value);
        assertEquals(1, data.size());
        Utils.mapPutSafe(null, key, value);
        Utils.mapPutSafe(data, null, value);
        Utils.mapPutSafe(data, key, null);
        assertEquals(1, data.size());
    }

    @Test
    public void shouldGetPackageName() {
        assertEquals("com.bazaarvoice.bvandroidsdk_common.test", Utils.getPackageName(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void shouldGetNonNullVersionName() {
        assertNotNull(Utils.getVersionName(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void shouldGetNonNullVersionCode() {
        assertNotNull(Utils.getVersionCode(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void shouldGenerateUUID() {
        assertNotNull(Utils.getUuid(ApplicationProvider.getApplicationContext()));
    }

    @Test
    public void shouldCreateBitmapFromBytes() throws IOException {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        Bitmap createdBitmap = Utils.decodeBitmapFromBytes(baos.toByteArray(), 10, 10);
        assertNotNull(createdBitmap);
    }

    @Test
    public void shouldEncodeBitmapToBase64() throws IOException {
        Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        String createdBitmapTOBase64 = Utils.toBase64(bitmap);
        assertNotNull(createdBitmapTOBase64);
    }

}
