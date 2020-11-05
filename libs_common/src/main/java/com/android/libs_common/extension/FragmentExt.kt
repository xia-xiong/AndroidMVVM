package com.live.common.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by  on 2019/4/2.
 *
 */

fun Fragment.addFragment(layoutRes: Int, otherFragment: Fragment) {
    childFragmentManager.transact {
        add(layoutRes, otherFragment)
    }
}

fun Fragment.replaceShowInFragment(fragment: Fragment, viewId: Int) {
    childFragmentManager.transact {
        replace(viewId, fragment).show(fragment)
    }
}

fun Fragment.removeFragment(fragment: Fragment) {
    childFragmentManager.transact {
        remove(fragment)
    }
}

fun Fragment.hideFragment(fragment: Fragment) {
    childFragmentManager.transact {
        hide(fragment)
    }
}

fun Fragment.showFragment(fragment: Fragment) {
    childFragmentManager.transact {
        show(fragment)
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commitAllowingStateLoss()
}