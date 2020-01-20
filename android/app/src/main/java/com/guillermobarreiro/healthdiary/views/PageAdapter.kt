package com.guillermobarreiro.healthdiary.views

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.guillermobarreiro.healthdiary.R

/**
 * Class for managing the two Fragments shown in the MainActivity.
 * In tab 0: BloodPressureFragment
 * In tab 1: BodyMeasureFragment
 */
class PageAdapter(fm: FragmentManager, val context: Context): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        // Returns the desired fragment for each tab
        return when (position){
            0 -> {BloodPressureFragment()}
            1 -> {BodyMeasuresFragment()}
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return 2 // returns 2, the number of tabs we have
    }

    override fun getPageTitle(position: Int): CharSequence? {
        // Returns the name of the fragment corresponding to each tab
        return when (position){
            0 -> context.resources.getString(R.string.blood_tab)
            1 -> context.resources.getString(R.string.measures_tab)
            else -> null
        }
    }


}