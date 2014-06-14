package tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetImage {
	
    public static Bitmap get(String path){    
    	InputStream inputStream = null;  
        try {  
            inputStream = new FileInputStream(path);  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        }  
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);  
        return bitmap;
    }
}
