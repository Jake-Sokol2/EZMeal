package navigationFragments.FindRecipes;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public class Utils
{
    static String getJsonFromAssets(Context context, String fileName)
    {
        String jsonString;
        try
        {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}