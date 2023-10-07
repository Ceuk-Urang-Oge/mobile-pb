package com.marqumil.peakyblinder.ui.home

import android.content.Context
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

class ImageAdapter(private val context: Context, private val images: List<Int>) : PagerAdapter() {

    private var viewPager: ViewPager? = null
    private val handler = Handler()
    private lateinit var timer: Timer
    private val delay: Long = 5000 // Delay in milliseconds
    private var currentPage = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setImageResource(images[position])
        container.addView(imageView)
        return imageView
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    fun setViewPager(viewPager: ViewPager) {
        this.viewPager = viewPager
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Not needed for this implementation
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not needed for this implementation
            }
        })
        startAutoScroll()
    }

    fun startAutoScroll() {
        val update = Runnable {
            if (currentPage == getCount()) {
                currentPage = 0
            }
            viewPager?.setCurrentItem(currentPage++, true)
        }

        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay, delay)
    }

    fun stopAutoScroll() {
        timer.cancel()
    }
}