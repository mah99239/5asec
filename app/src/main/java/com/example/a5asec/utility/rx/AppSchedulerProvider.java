package com.example.a5asec.utility.rx;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AppSchedulerProvider implements SchedulerProvider
{

  @Override
  public Scheduler computation()
  {
    return Schedulers.computation();
  }

  @Override
  public Scheduler io()
  {
    return Schedulers.io();
  }

  @Override
  public Scheduler ui()
  {
    return AndroidSchedulers.mainThread();
  }
}
