package com.example.taha.dizidark01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.taha.dizidark01.fragment_class.FragmentHome;
import com.example.taha.dizidark01.fragment_class.FragmentProfile;
import com.example.taha.dizidark01.fragment_class.FragmentSearch;
import com.example.taha.dizidark01.fragment_class.FragmentSeries;
import com.example.taha.dizidark01.ui.main.SectionsPagerAdapter;

public class Home extends AppCompatActivity {
    SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        setupPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.srcicon);
    }

    public void loginBtn(View view){
        Intent intent = new Intent(Home.this,Login.class);
        Home.this.finish();
        startActivity(intent);
    }

    private void setupPager(ViewPager viewPager) {

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentSearch(),"");
        adapter.addFragment(new FragmentHome(),"ÖNE ÇIKANLAR");
        adapter.addFragment(new FragmentSeries(),"DİZİLER");
        adapter.addFragment(new FragmentProfile(), "PROFİLİM");
        viewPager.setAdapter(adapter);
    }
}