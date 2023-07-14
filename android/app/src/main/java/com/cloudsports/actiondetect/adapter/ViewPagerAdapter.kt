package com.cloudsports.actiondetect.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cloudsports.actiondetect.fragment.YearFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // 返回显示的Fragment
        val year = 2023 - position
        return YearFragment.newInstance(year)
    }

    override fun getCount(): Int {
        // 返回总共的页面数量
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence {
        // 返回页面的标题，如果你的ViewPager使用了TabLayout，这个标题会显示在Tab上面
        val year = 2023 - position
        return year.toString()
    }
}
