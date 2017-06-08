package com.groupon.featureadapter.sample.state;

import android.util.Log;
import com.groupon.featureadapter.sample.DealDetailsModel;
import com.groupon.grox.Store;

public class LoggerMiddleWare implements Store.Middleware<DealDetailsModel> {
  @Override
  public void intercept(Chain<DealDetailsModel> chain) {
    Log.d("Action-Store", "Action: " + chain.action().getClass().getName());
    chain.proceed(chain.action());
  }
}
