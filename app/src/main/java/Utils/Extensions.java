package Utils;


import android.content.Context;
import android.widget.Toast;

public class Extensions {
    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }
    public static boolean isEmpty(String text) {
        return text.isEmpty();
    }

    public static void ShowMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
