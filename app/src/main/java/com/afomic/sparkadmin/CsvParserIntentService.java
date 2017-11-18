package com.afomic.sparkadmin;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;

import com.afomic.sparkadmin.model.Constitution;
import com.afomic.sparkadmin.model.Course;
import com.afomic.sparkadmin.util.Constant;
import com.afomic.sparkadmin.util.CsvReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CsvParserIntentService extends IntentService {
    
    public static final String ACTION_GET_COURSE = "com.afomic.sparkadmin.action.GET_COURSE";
    public static final String ACTION_GET_CONSTITUTION = "com.afomic.sparkadmin.action.GET_CONSTITUTION";

    private static final String EXTRA_PATH = "com.afomic.sparkadmin.extra.PATH";

    public CsvParserIntentService() {
        super("CsvParserIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startGetConstitution(Context context, String path) {
        Intent intent = new Intent(context, CsvParserIntentService.class);
        intent.setAction(ACTION_GET_CONSTITUTION);
        intent.putExtra(EXTRA_PATH, path);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startGetCourse(Context context, String path) {
        Intent intent = new Intent(context, CsvParserIntentService.class);
        intent.setAction(ACTION_GET_COURSE);
        intent.putExtra(EXTRA_PATH, path);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_CONSTITUTION.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_PATH);
                handleGetCourse(path);
            } else if (ACTION_GET_COURSE.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_PATH);
                handleGetCourse(path);
            }
        }

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleGetCourse(String path) {
        ArrayList<Course> courses=null;
        String errorMessage=null;
        try{
           courses=new CsvReader().getCourses(path);
        }catch (NumberFormatException e){
            errorMessage="Wrong Column Format";
        }catch (FileNotFoundException e){
            errorMessage="Invalid File";
        }catch (InvalidPropertiesFormatException e){
            errorMessage="Wrong File Format";
        }
        Intent intent=new Intent();
        intent.setAction(ACTION_GET_COURSE);
        if(courses==null){
            intent.putExtra(Constant.EXTRA_ERROR,errorMessage);
        }else {
            intent.putExtra(Constant.EXTRA_COURSE_LIST,courses);
        }
        sendBroadcast(intent);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetConstitution(String path) {
        ArrayList<Constitution> constitutions=null;
        String errorMessage=null;
        try{
            constitutions=new CsvReader().getConstitution(path);
        }catch (NumberFormatException e){
            errorMessage="Wrong Column Format";
        }catch (FileNotFoundException e){
            errorMessage="Invalid File";
        }catch (InvalidPropertiesFormatException e){
            errorMessage="Wrong File Format";
        }
        Intent intent=new Intent();
        intent.setAction(ACTION_GET_CONSTITUTION);
        if(constitutions==null){
            intent.putExtra(Constant.EXTRA_ERROR,errorMessage);
        }else {
            intent.putExtra(Constant.EXTRA_COURSE_LIST,constitutions);
        }
        sendBroadcast(intent);
    }
}
