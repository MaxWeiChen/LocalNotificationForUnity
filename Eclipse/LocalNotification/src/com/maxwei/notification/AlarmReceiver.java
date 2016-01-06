package com.maxwei.notification;



import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.unity3d.player.UnityPlayer;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;

//import java.util.Calendar;
import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
//import com.unity3d.player.UnityPlayer;

public class AlarmReceiver extends BroadcastReceiver 
{	
    public static void startAlarm(int requestCode, String name, String title, String label, int secondsFromNow)
    {
        Activity unityAct = UnityPlayer.currentActivity;
        Log.i("Unity", "startAlarm...");
        
        // 取得定時呼叫管理器
        AlarmManager alarmManager = (AlarmManager)unityAct.getSystemService(Context.ALARM_SERVICE);
        
        // 封裝訊息
        Intent ii = new Intent(unityAct, AlarmReceiver.class);
        ii.putExtra("name", name);
        ii.putExtra("title", title);
        ii.putExtra("label", label);
        
        // 整理定時需要的資訊
        PendingIntent broadcast = PendingIntent.getBroadcast(unityAct, requestCode, ii, PendingIntent.FLAG_UPDATE_CURRENT);

        // 設定時間
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, secondsFromNow);
        long alarmTime = calendar.getTimeInMillis();
        Log.i("Unity", "alarm time +"+secondsFromNow);
        
        // 加入定時
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, broadcast);
    }
    
    public static void cleanAlarm(int requestCode)
    {
    	Activity unityAct = UnityPlayer.currentActivity;
    	 Log.i("Unity", "cleanAlarm...");
    	 
		Intent ii = new Intent(unityAct, AlarmReceiver.class);
		PendingIntent pp = PendingIntent.getBroadcast(unityAct, requestCode, ii, PendingIntent.FLAG_NO_CREATE);
		if( pp != null )
		{
			AlarmManager alarmManager = (AlarmManager)unityAct.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pp);
		}
    }
  
    
    //<receiver android:process=":remote" android:name="com.maxwei.notification.AlarmReceiver"></receiver>
    @Override
    public void onReceive(Context context, Intent intent) 
    {
        Log.d("Unity", "Alarm Recieved!");
                
        // 取得PackageManager
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try 
        {
        	applicationInfo = pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
        } 
        catch (NameNotFoundException e)
        {
        	e.printStackTrace();
        	return;
        }
        // Get Default Icon
        final int appIconResId = applicationInfo.icon;
        
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
//        String iconSmall = intent.getStringExtra("iconSmall");
  
        Intent newIntent = pm.getLaunchIntentForPackage(context.getPackageName());

        // 取得PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, newIntent, PendingIntent.FLAG_CANCEL_CURRENT); // 取得PendingIntent

        // 自訂Icon
//        int smallIcon = context.getResources().getIdentifier(iconSmall, "drawable", context.getPackageName());

        // 建立推播內容
        Notification.Builder builder = new Notification.Builder(context.getApplicationContext());

//        builder.setSmallIcon(smallIcon);    // setSmallIcon 尺寸建議 32 * 32
        builder.setSmallIcon(appIconResId);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel( true );
        Notification notification = builder.build();

        // 取得推播管理器, 執行推播
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(100, notification);
    }
}
