package com.groupon.android.featureadapter.sample.events;

import com.groupon.android.featureadapter.sample.state.SampleModel;
import com.groupon.featureadapter.events.FeatureEvent;
import com.groupon.grox.commands.rxjava2.Command;
import com.groupon.grox.commands.rxjava2.SingleActionCommand;

public abstract class SingleActionBaseCommand extends SingleActionCommand<SampleModel> implements BaseCommand {
}
