package com.guillermobarreiro.healthdiary.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.guillermobarreiro.healthdiary.R


class MainActivity : AppCompatActivity() {

    private lateinit var tabsMain: TabLayout
    private lateinit var viewPagerMain: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializes the views
        setContentView(R.layout.activity_main)
        tabsMain = findViewById(R.id.tabs_main)
        viewPagerMain = findViewById(R.id.pager_main)

        // Configures the tabs
        val fragmentAdapter = PageAdapter(supportFragmentManager, baseContext)
        viewPagerMain.adapter = fragmentAdapter
        tabsMain.setupWithViewPager(viewPagerMain)
    }


}
