package daniel.com.br.crud;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting up the adapter that is going to control the fragments
        //adding the book fragment to it
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(new BookFragment(),"Books");
        pageAdapter.addFragment(new BookFragment(),"Books");
        mViewPager = (ViewPager)findViewById(R.id.container);
        mViewPager.setAdapter(pageAdapter);

        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    class PageAdapter extends FragmentStatePagerAdapter{

        private final List<Fragment> mFragmentsList;
        private final List<String> mFragmentsTitle;

        public PageAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            mFragmentsList = new ArrayList<>();
            mFragmentsTitle = new ArrayList<>();
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentsList.add(fragment);
            mFragmentsTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentsTitle.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }


    }

}