package com.afomic.sparkadmin;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.afomic.sparkadmin.adapter.navAdapter;
import com.afomic.sparkadmin.fragment.ConstitutionFragment;
import com.afomic.sparkadmin.fragment.CourseFragment;
import com.afomic.sparkadmin.fragment.PostFragment;
import com.afomic.sparkadmin.fragment.PostMangerFragment;
import com.afomic.sparkadmin.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {


    Toolbar mToolbar;

    DrawerLayout homeDrawerLayout;

    ListView navBar;

    FragmentManager fm;
    navAdapter adapter;

    int currentPosition=0;
    int previousPosition=0;
    boolean firstRun=true;

    LinearLayout navItemLayout;

    int[] navIcon={R.drawable.feedback, R.drawable.feedback,R.drawable.feedback,R.drawable.feedback,R.drawable.feedback};

    int[] selectedNavIcon={R.drawable.feedback, R.drawable.feedback,R.drawable.feedback,R.drawable.feedback,R.drawable.feedback};

    public final String BUNDLE_POSITION="position";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        setSupportActionBar(mToolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setTitle("Home");
        }
        Typeface roboto = Typeface.createFromAsset(getAssets(),"font/Lato-Regular.ttf");

        if(savedInstanceState!=null){
            currentPosition=savedInstanceState.getInt(BUNDLE_POSITION);
        }
        fm=getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.main_container);
        if(fragment==null){
            PostFragment frag=PostFragment.newInstance();
            fm.beginTransaction().add(R.id.main_container,frag).commit();
        }


        adapter=new navAdapter(this);
        navBar.setAdapter(adapter);

        navBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v;
                ImageView icon;
                TextView navText;
                int firstCompletelyVisiblePos = navBar.getFirstVisiblePosition();
                if(firstRun){
                    navItemLayout=(LinearLayout)navBar.getChildAt(firstCompletelyVisiblePos);
                    firstRun=false;
                }

                if (navItemLayout != null) {
                    v =navItemLayout.getChildAt(0);
                    icon = (ImageView) navItemLayout.getChildAt(1);
                    navText = (TextView) navItemLayout.getChildAt(2);
                    v.setBackgroundColor(Color.WHITE);
                    icon.setImageResource(navIcon[previousPosition]);
                    navText.setTextColor(Color.GRAY);
                }
                previousPosition = position;
                navItemLayout = (LinearLayout) view;
                v = navItemLayout.getChildAt(0);
                icon = (ImageView) navItemLayout.getChildAt(1);
                navText = (TextView) navItemLayout.getChildAt(2);
                v.setBackgroundColor(Color.argb(255,3, 169,244));
                icon.setImageResource(selectedNavIcon[position]);
                navText.setTextColor(Color.argb(255, 3,169, 244));
                supportInvalidateOptionsMenu();
                currentPosition=position;
                switch (position) {
                    case 0:
                        PostFragment postfragment = PostFragment.newInstance();
                        displayFragment(postfragment);
                        break;
                    case 1:
                        PostMangerFragment postMangerFragment=PostMangerFragment.newInstance();
                        displayFragment(postMangerFragment);
                        break;
                    case 2:
                        ProfileFragment profileFragment=ProfileFragment.newInstance();
                        displayFragment(profileFragment);
                        break;
                    case 3:
                        ConstitutionFragment constitutionFragment=ConstitutionFragment.newInstance();
                        displayFragment(constitutionFragment);
                        break;
                    case 4:
                        CourseFragment courseFragment=CourseFragment.newInstance();
                        displayFragment(courseFragment);
                        break;

                }
            }
        });
        TextView team= findViewById(R.id.team);
        team.setTypeface(roboto);
    }
    public void initializeView(){
        mToolbar=findViewById(R.id.toolbar);
        homeDrawerLayout=findViewById(R.id.home_drawer);
        navBar=findViewById(R.id.nav_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            homeDrawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }
    public void displayFragment(Fragment frag){
        homeDrawerLayout.closeDrawers();
        fm.beginTransaction().replace(R.id.main_container,frag).commit();

    }
}
