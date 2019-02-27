package com.groupon.android.featureadapter.sample.events;

import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.commands.rxjava2.Command;

public interface BaseCommand extends Command<SampleModel>, FeatureEvent {
}
