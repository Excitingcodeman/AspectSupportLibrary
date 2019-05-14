package com.gs.apectsupport.aspect;

import android.util.Log;
import android.view.View;
import com.gs.apectsupport.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author husky
 * create on 2019-04-30-16:38
 */
@Aspect
public class SingleClickAspect {

    private static final String TAG = SingleClickAspect.class.getSimpleName();
    private static final int MIN_CLICK_DELAY_TIME = 600;
    private static int TIME_TAG = R.id.click_time;

    @Pointcut("execution(@com.gs.apectsupport.annotation.SingleClick * *(..))")
    public void methodAnnotated() {

    }

    @Around("methodAnnotated()")//在连接点进行方法替换
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        View view = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof View) view = ((View) arg);
        }
        if (view != null) {
            Object tag = view.getTag(TIME_TAG);
            long lastClickTime = (tag != null) ? (long) tag : 0;
            Log.d(TAG, "lastClickTime:" + lastClickTime);
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {//过滤掉600毫秒内的连续点击
                view.setTag(TIME_TAG, currentTime);
                Log.d(TAG, "currentTime:" + currentTime);
                joinPoint.proceed();//执行原方法
            }
        }
    }
}
