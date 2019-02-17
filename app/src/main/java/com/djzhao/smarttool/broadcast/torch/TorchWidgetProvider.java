package com.djzhao.smarttool.broadcast.torch;
import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.util.*;
import android.widget.*;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.util.torch.TorchUtils;

public class TorchWidgetProvider extends AppWidgetProvider
{
	public static final String CLICK_ACTION = "cc.trv.simpletorch.action.TORCH_WIDGET_CLICK";// 点击事件的广播ACTION
	private int appWidgetId = 0;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.torch_widget_layout);
		Intent intent = new Intent(CLICK_ACTION);
		intent.putExtra("appWidgetId", appWidgetIds[0]);
		appWidgetId = appWidgetIds[0];
		PendingIntent pi = PendingIntent.getBroadcast(context, R.id.torch_widget_icon, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.torch_widget_icon, pi);
		for (int appWidgetId : appWidgetIds)
		{
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		if (CLICK_ACTION.equals(intent.getAction()))
		{
			Log.i("dj", appWidgetId + "");
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.torch_widget_layout);
			try 
			{
				if (!TorchUtils.IS_TORCH_ON)
				{
					TorchUtils.openFlash(context, false);
					remoteViews.setImageViewResource(R.id.torch_widget_icon, R.drawable.button_on);
				}
				else
				{
					TorchUtils.closeFlash(false);
					remoteViews.setImageViewResource(R.id.torch_widget_icon, R.drawable.button_off);
				}
				TorchUtils.IS_TORCH_ON = !TorchUtils.IS_TORCH_ON;
				AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews);
			}
			catch (Exception e)
			{}
		}
	}

	@Override
	public void onEnabled(Context context)
	{
		super.onEnabled(context);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.torch_widget_layout);
		if (!TorchUtils.IS_TORCH_ON)
		{
			remoteViews.setImageViewResource(R.id.torch_widget_icon, R.drawable.button_on);
		}
		else
		{
			remoteViews.setImageViewResource(R.id.torch_widget_icon, R.drawable.button_off);
		}
	}



	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		super.onDeleted(context, appWidgetIds);
		TorchUtils.closeFlash(false);
	} 



}
