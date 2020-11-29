package dev.somnath.onlynews.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.somnath.onlynews.ui.articledetails.ArticlesFragment

class ViewPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 8

    override fun createFragment(position: Int): Fragment {
        return ArticlesFragment()
    }
}