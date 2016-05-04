/**
 *  Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Internal SDK API for asynchronously fetching the Google Advertising ID
 */
class AdvertisingIdClient {
    private boolean adInfoHasBeenRequested = false;
    private AdInfo adInfo;
    private ArrayList<BVSDK.GetAdInfoCompleteAction> adInfoQueue = new ArrayList<>();
    //google play services no installed or limited tracking used
    static final String noAdIdDefault = "nontracking";

    AdvertisingIdClient(Context context) {
        requestAdvertisingIdInfo(context, null);
    }

    public void getAdInfo(BVSDK.GetAdInfoCompleteAction action)
    {
        if (!this.adInfoHasBeenRequested)
        {
            if(this.adInfo == null) {
                throw new AssertionError("Must call requestAdvertisingIdInfo(Context context) before getAdInfo()");
            }
        }else if (this.adInfo == null) {
            try {
                requestAdvertisingIdInfo(null, action);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            action.completionAction(adInfo);
        }
    }
    
    private void requestAdvertisingIdInfo(final Context context, BVSDK.GetAdInfoCompleteAction action) {

        if (!this.adInfoHasBeenRequested) {
            this.adInfoHasBeenRequested = true;
            if (action != null) {
                adInfoQueue.add(action);
            }

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    if (Looper.myLooper() == Looper.getMainLooper())
                        throw new IllegalStateException("Cannot be called from the main thread");

                    try {
                        PackageManager pm = context.getPackageManager();
                        pm.getPackageInfo("com.android.vending", 0);
                    } catch (Exception e) {
                         e.printStackTrace();
                    }

                    AdvertisingConnection connection = new AdvertisingConnection();
                    Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                    intent.setPackage("com.google.android.gms");

                    try {
                        if (context.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
                            AdvertisingInterface adInterface = new AdvertisingInterface(connection.getBinder());

                            boolean limitedTracking = adInterface.isLimitAdTrackingEnabled(true);

                            AdInfo info;
                            if (limitedTracking)
                            {
                                info = new AdInfo(noAdIdDefault, true);
                            }else {
                                info = new AdInfo(adInterface.getId(), false);
                            }
                            adInfo = info;
                            perfromInfoQueue();
                            return;
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        context.unbindService(connection);
                    }
                    //unable to get ad id; we will use nontracking
                    adInfo = new AdInfo(noAdIdDefault, true);
                    perfromInfoQueue();
                }
            });

        }else
        {
            if (action != null) {
                adInfoQueue.add(action);
            }
        }
    }

    private void perfromInfoQueue()
    {
        for (BVSDK.GetAdInfoCompleteAction action : adInfoQueue)
        {
            action.completionAction(adInfo);
        }

        adInfoQueue = null;
    }


    private static final class AdvertisingConnection implements ServiceConnection {
        boolean retrieved = false;
        private final LinkedBlockingQueue<IBinder> queue = new LinkedBlockingQueue<IBinder>(1);

        public void onServiceConnected(ComponentName name, IBinder service) {
            try { this.queue.put(service); }
            catch (InterruptedException localInterruptedException){}
        }

        public void onServiceDisconnected(ComponentName name){}

        public IBinder getBinder() throws InterruptedException {
            if (this.retrieved) throw new IllegalStateException();
            this.retrieved = true;
            return (IBinder)this.queue.take();
        }
    }

    private static final class AdvertisingInterface implements IInterface {
        private IBinder binder;

        public AdvertisingInterface(IBinder pBinder) {
            binder = pBinder;
        }

        public IBinder asBinder() {
            return binder;
        }

        public String getId() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            String id;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                binder.transact(1, data, reply, 0);
                reply.readException();
                id = reply.readString();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return id;
        }

        public boolean isLimitAdTrackingEnabled(boolean paramBoolean) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            boolean limitAdTracking;
            try {
                data.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
                data.writeInt(paramBoolean ? 1 : 0);
                binder.transact(2, data, reply, 0);
                reply.readException();
                limitAdTracking = 0 != reply.readInt();
            } finally {
                reply.recycle();
                data.recycle();
            }
            return limitAdTracking;
        }
    }
}