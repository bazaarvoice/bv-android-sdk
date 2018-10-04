package com.bazaarvoice.bvsdkdemoandroid.utils;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

public class DemoAssetsUtil {
  private Context context;
  private Gson gson;

  public DemoAssetsUtil(Context context, Gson gson) {
    this.context = context.getApplicationContext();
    this.gson = gson;
  }

  @NonNull
  public <ResponseType> ResponseType parseJsonFileFromAssets(String fileName, Class<ResponseType> responseTypeClass) {
    ResponseType response = null;
    try {
      Reader reader = new InputStreamReader(context.getAssets().open(fileName));
      response = gson.fromJson(reader, responseTypeClass);
    } catch (JsonSyntaxException | JsonIOException e) {
      throw new IllegalStateException("Failed to parse " + fileName + " in the assets directory", e);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to find " + fileName + " in the assets directory", e);
    }
    return response;
  }

  public boolean assetFileExists(String fileName) {
    boolean exists = false;
    try {
      String[] assetFileNames = context.getAssets().list("");
      for (int i=0; i<assetFileNames.length; i++) {
        String assetFileName = assetFileNames[i];
        if (assetFileName.equals(fileName)) {
          exists = true;
          break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return exists;
  }

  public File parseImageFileFromAssets(String fileName) {
    File localImageFile = null;

    try {
      InputStream stream = context.getAssets().open(fileName);
      localImageFile = createFileFromInputStream(stream);

    } catch (IOException e) {
      e.printStackTrace();
    }

    return localImageFile;
  }

  private File createFileFromInputStream(InputStream inputStream) {
    try{
      File f = File.createTempFile("tmp", "png");
      OutputStream outputStream = new FileOutputStream(f);
      byte buffer[] = new byte[1024];
      int length = 0;

      while((length=inputStream.read(buffer)) > 0) {
        outputStream.write(buffer,0,length);
      }

      outputStream.close();
      inputStream.close();

      return f;

    }catch (IOException e) {
      //Logging exception
      e.printStackTrace();
    }

    return null;
  }
}
