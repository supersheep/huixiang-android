package in.spud.huixiang;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.app.ActionBar.Tab;

public class PieceActivity extends ActionBarActivity
{

    public static final String TAG = "in.spud.huixiang.PieceActivity";

    private GestureDetectorCompat mDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        super.onCreate(savedInstanceState);
        mDetector = new GestureDetectorCompat(this, new PieceGestureListener());
        setContentView(R.layout.activity_piece);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        actionBar.addTab(
                actionBar.newTab()
                .setText(R.string.tab_piece)
                .setTabListener(new TabListener<PieceFragment>(this, getString(R.string.tab_piece), PieceFragment.class))
        );

        actionBar.addTab(
                actionBar.newTab()
                        .setText(R.string.tab_setting)
                        .setTabListener(new TabListener<PieceFragment>(this, getString(R.string.tab_setting), SettingFragment.class))
        );
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_piece, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState " + (savedInstanceState == null ? "null" : savedInstanceState.toString()));
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("key1", "value1");
        outState.putString("key2", "value2");
        Log.d(TAG, "onSaveInstanceState " + (outState == null ? "null" : outState.toString()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Log.d("sss", "setting");
                return true;
            case R.id.action_search:
                Log.d("sss", "Search");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class PieceGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(TAG, "onFling: " + event1.toString()+event2.toString());
            return true;
        }
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener{

        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public TabListener(Activity activity, String tag, Class clz){
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(mFragment == null){
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(R.id.content, mFragment, mTag);
            }else{
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(mFragment != null){
                ft.detach(mFragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }


}
