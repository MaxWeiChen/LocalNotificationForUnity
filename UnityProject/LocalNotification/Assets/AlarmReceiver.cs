using UnityEngine;
using System.Collections;

public class AlarmReceiver : MonoBehaviour {

	// Use this for initialization
	void Start () {
#if UNITY_IOS
		UnityEngine.iOS.NotificationServices.RegisterForNotifications(
			UnityEngine.iOS.NotificationType.Alert | 
			UnityEngine.iOS.NotificationType.Badge | 
			UnityEngine.iOS.NotificationType.Sound);
#endif
	}

	AndroidJavaObject nativeObj =null;
	
  	void OnGUI()
  	{
	    if (GUI.Button(new Rect(Screen.width*0.5f-90.0f, 100.0f, 180.0f, 100.0f), "Create Notification"))
	    {
#if UNITY_IOS
			NotificationMessage("BTN!", "HIHI", 5f);
#else
			NotificationUtil.Instance.NotificationMessage(0, "Ticket:Btn", "Title:Btn", "Content:Btn", 5);
#endif
	    }
	}
  
	void OnApplicationPause(bool isPause)
	{
#if UNITY_IOS
		if(isPause)
		{
			NotificationMessage("LISA", "HIHI", 5f);
		}
#else	
		if(isPause)
		{
			NotificationUtil.Instance.NotificationMessage(0, "Ticket:Pause", "Title:Pause", "Content:Pause", 5);
			NotificationUtil.Instance.NotificationMessage(1, "Ticket:Pause1", "Title:Pause1", "Content:Pause1", 5);
		}
		else
		{
			NotificationUtil.Instance.CleanNotification(0);
		}
#endif
	}
	
	public void NotificationMessage(string title, string message, double sec)
	{
		#if UNITY_IOS
		UnityEngine.iOS.LocalNotification notif = new UnityEngine.iOS.LocalNotification();
		notif.alertAction = title;
		notif.alertBody = message;
		notif.hasAction = true;
		notif.fireDate = System.DateTime.Now.AddSeconds(sec);
		UnityEngine.iOS.NotificationServices.ScheduleLocalNotification(notif);
		#endif
	}
	
	public void CleanNotification()
	{
		#if UNITY_IOS
		UnityEngine.iOS.NotificationServices.CancelAllLocalNotifications();
		UnityEngine.iOS.NotificationServices.ClearLocalNotifications();
		#endif
	}
}
