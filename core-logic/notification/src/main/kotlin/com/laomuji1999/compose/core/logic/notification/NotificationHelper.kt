package com.laomuji1999.compose.core.logic.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.getSystemService
import androidx.core.graphics.drawable.IconCompat
import com.laomuji1999.compose.core.logic.common.Log
import com.laomuji1999.compose.core.logic.common.cache.CacheUtil
import com.laomuji1999.compose.core.logic.model.entity.ContactInfoEntity
import com.laomuji1999.compose.core.logic.model.entity.MessageInfoEntity
import javax.inject.Singleton

@Singleton
class NotificationHelper(
    context: Context,
    cacheUtil: CacheUtil
) {
    companion object{
        const val ENABLE_NOTIFICATION = "ENABLE_NOTIFICATION"

        private const val CHANNEL_NAME_NEW_MESSAGES = "new_messages"
        private const val REQUEST_NEW_MESSAGES = 1
    }

    private val appContext = context
    private val notificationManager: NotificationManager = context.getSystemService()!!
    private val enableNotification by cacheUtil.cacheable(ENABLE_NOTIFICATION,false)

    init {
        setUpNotificationChannels()
    }

    private fun setUpNotificationChannels() {
        //低版本没有通知渠道
        if (Build.VERSION.SDK_INT < 26) {
            return
        }
        if (notificationManager.getNotificationChannel(CHANNEL_NAME_NEW_MESSAGES) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_NAME_NEW_MESSAGES,
                    appContext.getString(com.laomuji1999.compose.res.R.string.string_notification_new_messages_name),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = appContext.getString(com.laomuji1999.compose.res.R.string.string_notification_new_messages_description)
                },
            )
        }
    }


    fun showNotification(
        contactInfoEntity: ContactInfoEntity,
        messageInfoEntity: MessageInfoEntity
    ) {
        if(!enableNotification){
            return
        }

        //设置对话信息
        val icon = IconCompat.createWithAdaptiveBitmapContentUri(contactInfoEntity.avatarUri)

        val person = Person.Builder()
            .setName(contactInfoEntity.nickname)
            .setIcon(icon)
            .build()
        val messageStyle = NotificationCompat.MessagingStyle(person)
        messageStyle.addMessage(
            NotificationCompat.MessagingStyle.Message(
                messageInfoEntity.text,
                messageInfoEntity.timestamp,
                person,
            )
        )

        //deeplink跳转到聊天页面
        val intent = Intent()
        intent.setClassName(appContext, "com.laomuji1999.compose.MainActivity")
        intent.setAction(Intent.ACTION_VIEW)
        intent.setData(contactInfoEntity.contentUri)
        Log.debug("tag_uri", "${contactInfoEntity.contentUri}")
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            REQUEST_NEW_MESSAGES,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        //创建通知
        val builder = NotificationCompat.Builder(appContext, CHANNEL_NAME_NEW_MESSAGES)
            .setContentTitle(contactInfoEntity.nickname) //设置名称
            .setSmallIcon(com.laomuji1999.compose.res.R.mipmap.ic_launcher) //设置小图标,App的logo
            .setCategory(Notification.CATEGORY_MESSAGE) //设置分类
            .setShowWhen(true) //开启时间显示
            .setWhen(messageInfoEntity.timestamp) //使用收到消息的时间
            .addPerson(person) //添加对话人员
            .setStyle(messageStyle) //设置对话风格
            .setContentIntent(pendingIntent) //设置点击跳转
            .setDeleteIntent(null) //设置用户可以关闭左划关闭通知

        notificationManager.notify(
            contactInfoEntity.account.toNotificationId(),
            builder.build()
        )
    }

    fun dismissNotification(contactInfoEntity: ContactInfoEntity){
        notificationManager.cancel(contactInfoEntity.account.toNotificationId())
    }

    private fun Long.toNotificationId():Int = (this % Int.MAX_VALUE).toInt()
}