using UnityEngine;
using System.Collections;

public class NotificationUtil : Singleton<NotificationUtil> 
{
#if UNITY_ANDROID
	private static string notificationClassName = "com.maxwei.notification.AlarmReceiver";
#endif

	public void RegisterNotification()
	{
#if UNITY_IOS
		UnityEngine.iOS.NotificationServices.RegisterForNotifications(
			UnityEngine.iOS.NotificationType.Alert |
			UnityEngine.iOS.NotificationType.Badge |
			UnityEngine.iOS.NotificationType.Sound);
#endif
	}

	public void NotificationMessage(int index, string ticker, string title, string content, double sec)
	{
#if UNITY_IOS
		UnityEngine.iOS.LocalNotification notif = new UnityEngine.iOS.LocalNotification();
		if(!title.Equals(string.Empty)) notif.alertAction = title;
		notif.alertAction = title;
		notif.alertBody = content;
		notif.hasAction = true;
		notif.fireDate = System.DateTime.Now.AddSeconds(sec);
		UnityEngine.iOS.NotificationServices.ScheduleLocalNotification(notif);
#elif UNITY_ANDROID
		var nativeObj = new AndroidJavaObject(notificationClassName);
		if (null != nativeObj) nativeObj.CallStatic("startAlarm", index, ticker, title, content, (int)sec);
#endif
	}
	
	public void CleanNotification(int index)
	{
#if UNITY_IOS
		UnityEngine.iOS.NotificationServices.CancelAllLocalNotifications();
		UnityEngine.iOS.NotificationServices.ClearLocalNotifications();	
#elif UNITY_ANDROID
		var nativeObj = new AndroidJavaObject(notificationClassName);
		if (null != nativeObj) nativeObj.CallStatic("cleanAlarm", index);
#endif
	}
}
