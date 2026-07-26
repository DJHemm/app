package com.djhemm.loyaltycards.databinding

import android.view.View
import androidx.viewbinding.ViewBinding
import com.djhemm.loyaltycards.R

class ActivityMainBinding private constructor(val root: View) : ViewBinding {
    val toolbar = root.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
    val recyclerView = root.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
    val fab = root.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
}

object ActivityMainBindingInflater {
    fun inflate(inflater: android.view.LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding(inflater.inflate(R.layout.activity_main, null, false))
    }
}
