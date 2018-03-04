package hala.com.salut.notifications;

/**
 * @author Anupam Singh
 * @version 1.0
 * @since 2017-08-20
 */


        import android.widget.Toast;

        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;


public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //todo: handle notification
        Toast.makeText(getApplicationContext(),remoteMessage.getData().toString(),Toast.LENGTH_SHORT).show();

    }

}