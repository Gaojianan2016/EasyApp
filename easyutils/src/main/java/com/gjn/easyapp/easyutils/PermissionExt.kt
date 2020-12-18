package com.gjn.easyapp.easyutils

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.SparseArray
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.util.*

class Permission(
    val name: String,
    var granted: Boolean,
    var shouldShowRequestPermissionRationale: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that: Permission = other as Permission
        if (granted != that.granted) return false
        if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) return false
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (granted) 1 else 0
        result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
        return result
    }

    override fun toString(): String = "Permission{" +
            "name='$name'\'', " +
            "granted=$granted, " +
            "shouldShowRequestPermissionRationale=$shouldShowRequestPermissionRationale" +
            "'}'"
}

interface IListener {
    interface IPermissionEvent {

        fun onAccepted(isGranted: Boolean)
        fun onException(throwable: Throwable)

    }

    interface IPermissionFragment {
        fun onAccepted(permission: Permission)

        fun onRefuseResults(
            refusePermissions: List<Permission>,
            allGranted: Boolean,
            hasShouldShowRequestPermissionRationale: Boolean
        )

        fun onException(throwable: Throwable)
    }
}

internal class PermissionFragment : Fragment() {

    private val mEventListeners: SparseArray<IListener.IPermissionEvent> = SparseArray()
    private val mFragmentListeners: SparseArray<IListener.IPermissionFragment> = SparseArray()
    private val mCodeGenerator = Random()

    init {
        retainInstance = true
    }

    fun requestPermissions(
        permissions: Array<String>,
        listener: IListener.IPermissionEvent?
    ) {
        val requestCode = randomRequestCode()
        mEventListeners.put(requestCode, listener)
        requestPermissions(permissions, requestCode)
    }

    fun requestFragmentPermissions(
        permissions: Array<String>,
        listener: IListener.IPermissionFragment?
    ) {
        val requestCode = randomRequestCode()
        mFragmentListeners.put(requestCode, listener)
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        handlePermissionListener(requestCode, permissions, grantResults)
        handleFragmentPermissionListener(requestCode, permissions, grantResults)
    }

    /**
     * 随机生成code
     *
     * @return requestCode
     */
    private fun randomRequestCode(): Int {
        var requestCode: Int
        var tryCount = 0
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF)
            tryCount++
        } while (mEventListeners.indexOfKey(requestCode) >= 0 && tryCount < MAX_TYR)
        return requestCode
    }

    private fun handlePermissionListener(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val listener = mEventListeners[requestCode]
        mEventListeners.remove(requestCode)
        if (listener == null) return
        try {
            var allGranted = false
            for (i in grantResults.indices) {
                val grantResult = grantResults[i]
                val name = permissions[i]
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
                allGranted = true
            }
            if (allGranted) {
                listener.onAccepted(true)
            } else {
                listener.onAccepted(false)
            }
        } catch (tr: Throwable) {
            listener.onException(tr)
        }
    }

    private fun handleFragmentPermissionListener(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val listener = mFragmentListeners[requestCode]
        mFragmentListeners.remove(requestCode)
        if (listener == null) return
        try {
            val refuseList = mutableListOf<Permission>()
            var hasShouldShowRequestPermissionRationale = false
            for (i in grantResults.indices) {
                var permission: Permission
                val grantResult = grantResults[i]
                val name = permissions[i]
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    permission = Permission(name, true)
                } else {
                    //是否选择了不再询问
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, name)) {
                        permission = Permission(
                            name,
                            granted = false,
                            shouldShowRequestPermissionRationale = true
                        )
                    } else {
                        hasShouldShowRequestPermissionRationale = true
                        permission = Permission(
                            name,
                            granted = false,
                            shouldShowRequestPermissionRationale = false
                        )
                    }
                }
                if (!permission.granted) {
                    refuseList.add(permission)
                }
                listener.onAccepted(permission)
            }
            listener.onRefuseResults(
                refuseList, refuseList.size == 0,
                hasShouldShowRequestPermissionRationale
            )
        } catch (tr: Throwable) {
            listener.onException(tr)
        }
    }

    companion object {
        private const val MAX_TYR = 10
    }
}

class PermissionHelper {
    private var activity: Activity? = null
    private var fm: FragmentManager? = null
    private val mPermissions = mutableListOf<String>()

    private constructor(activity: FragmentActivity) {
        this.activity = activity
        fm = activity.supportFragmentManager
    }

    private constructor(fragment: Fragment) {
        this.activity = fragment.activity
        fm = fragment.childFragmentManager
    }

    fun permissions(vararg permission: String): PermissionHelper {
        mPermissions.clear()
        mPermissions.addAll(permission)
        return this
    }

    fun add(vararg permission: String): PermissionHelper {
        mPermissions.addAll(permission)
        return this
    }

    fun request(listener: IListener.IPermissionEvent?) {
        requestPermissions(eventListener = listener)
    }

    fun request(listener: IListener.IPermissionFragment?) {
        requestPermissions(fragmentListener = listener)
    }

    private fun requestPermissions(
        eventListener: IListener.IPermissionEvent? = null,
        fragmentListener: IListener.IPermissionFragment? = null
    ) {
        if (mPermissions.size <= 0) return
        for (permission in mPermissions) {
            if (!activity!!.hasPermission(permission)) {
                "AndroidManifest.xml have not $permission".logE(TAG)
                return
            }
        }
        if (eventListener != null) {
            getPermissionFragment().requestPermissions(mPermissions.toTypedArray(), eventListener)
        }
        if (fragmentListener != null) {
            getPermissionFragment().requestFragmentPermissions(
                mPermissions.toTypedArray(),
                fragmentListener
            )
        }
    }

    private fun getPermissionFragment(): PermissionFragment {
        var fragment = fm!!.findFragmentByTag(TAG_PERMISSION)
        if (fragment == null) {
            fragment = PermissionFragment()
            fm!!.run {
                beginTransaction().add(fragment, TAG_PERMISSION).commitAllowingStateLoss()
                executePendingTransactions()
            }
        }
        return fragment as PermissionFragment
    }

    abstract class SimplePermissionFragmentListener :
        IListener.IPermissionFragment {

        override fun onAccepted(permission: Permission) {
        }

        override fun onRefuseResults(
            refusePermissions: List<Permission>,
            allGranted: Boolean,
            hasShouldShowRequestPermissionRationale: Boolean
        ) {
        }

        override fun onException(throwable: Throwable) {
        }
    }


    abstract class SimplePermissionEventListener : IListener.IPermissionEvent {
        override fun onAccepted(isGranted: Boolean) {
        }

        override fun onException(throwable: Throwable) {
        }
    }

    companion object {
        private const val TAG = "PermissionHelper"

        const val TAG_PERMISSION = "TAG_PERMISSION"

        const val CODE_SETTING = 0x777

        fun newInstance(activity: FragmentActivity): PermissionHelper = PermissionHelper(activity)

        fun newInstance(fragment: Fragment): PermissionHelper = PermissionHelper(fragment)

        fun requestDialogAgain(activity: Activity, refusePermissions: List<Permission>) {
            val message = StringBuilder()
            message.append(refusePermissions.size).append("个权限被拒绝\n")
            for (permission in refusePermissions) {
                message.append(permission.name.substring(permission.name.lastIndexOf(".") + 1))
                    .append("\n")
            }
            requestDialogAgain(activity, message = message.toString())
        }

        fun requestDialogAgain(
            activity: Activity, title: String? = "帮助", message: String?,
            confirm: String? = "前往设置", cancel: String? = "取消"
        ) {
            AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(confirm) { dialog: DialogInterface, _: Int ->
                    startAppSetting(activity)
                    dialog.dismiss()
                }
                .setNegativeButton(cancel) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        fun startAppSetting(activity: Activity) {
            try {
                Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", activity.packageName, null)
                }.startActivityForResult(activity, CODE_SETTING)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun FragmentActivity.simpleRequestPermissions(
    permissions: Array<String>,
    customDialogBlock: ((List<Permission>) -> Unit)? = null,
    block: () -> Unit
) {
    try {
        PermissionHelper.newInstance(this)
            .permissions(*permissions)
            .request(object : PermissionHelper.SimplePermissionFragmentListener() {
                override fun onRefuseResults(
                    refusePermissions: List<Permission>,
                    allGranted: Boolean,
                    hasShouldShowRequestPermissionRationale: Boolean
                ) {
                    if (allGranted) {
                        block.invoke()
                        return
                    }
                    if (customDialogBlock == null) {
                        if (hasShouldShowRequestPermissionRationale) {
                            PermissionHelper.requestDialogAgain(
                                this@simpleRequestPermissions,
                                refusePermissions
                            )
                        }
                    } else {
                        customDialogBlock.invoke(refusePermissions)
                    }
                }
            })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.simpleRequestPermissions(
    permissions: Array<String>,
    customDialogBlock: ((List<Permission>) -> Unit)? = null,
    block: () -> Unit
) {
    try {
        PermissionHelper.newInstance(this)
            .permissions(*permissions)
            .request(object : PermissionHelper.SimplePermissionFragmentListener() {
                override fun onRefuseResults(
                    refusePermissions: List<Permission>,
                    allGranted: Boolean,
                    hasShouldShowRequestPermissionRationale: Boolean
                ) {
                    if (allGranted) {
                        block.invoke()
                        return
                    }
                    if (customDialogBlock == null) {
                        if (hasShouldShowRequestPermissionRationale) {
                            this@simpleRequestPermissions.activity?.let {
                                PermissionHelper.requestDialogAgain(it, refusePermissions)
                            }
                        }
                    } else {
                        customDialogBlock.invoke(refusePermissions)
                    }
                }
            })
    } catch (e: Exception) {
        e.printStackTrace()
    }
}