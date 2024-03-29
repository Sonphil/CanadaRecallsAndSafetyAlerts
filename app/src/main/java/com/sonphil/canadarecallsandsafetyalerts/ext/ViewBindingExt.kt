package com.sonphil.canadarecallsandsafetyalerts.ext

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <T : ViewBinding> Fragment.viewLifecycle(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        private var viewLifecycleOwner: LifecycleOwner? = null

        init {
            this@viewLifecycle
                .viewLifecycleOwnerLiveData
                .observe(
                    this@viewLifecycle,
                    Observer { newLifecycleOwner ->
                        viewLifecycleOwner?.lifecycle?.removeObserver(this)
                        newLifecycleOwner.lifecycle.addObserver(this)
                        viewLifecycleOwner = newLifecycleOwner
                    }
                )
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return this.binding!!
        }

        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
            this.binding = value
        }
    }
