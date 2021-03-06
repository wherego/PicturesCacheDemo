package lvy.so.picturescachedemo.imagecacheutils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import lvy.so.picturescachedemo.utils.LoadImageUtils;

/**
 * @author gping  email: gping.vip@gmail.com
 * @date Created by 2016/8/24.15:33
 * @filename MemoryCacheUtils.class
 * @description 内存缓存
 * @TODO
 */
public class MemoryCacheUtils {
    //    private HashMap<String, Bitmap> memoryHashMap = new HashMap<>();  //强引用存储图片 浪费内存
//    private HashMap<String, SoftReference<Bitmap>> softReferenceHashMap = new HashMap<>();   //用图片存储采取软引用 系统内存紧张的时候会回收
    private LruCache<String, Bitmap> mMemoryLruCache;   // Android2.3+后，系统会优先考虑回收弱引用对象，官方提出使用LruCache  所以在用SoftReference不在靠谱

    public MemoryCacheUtils() {

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());  //最大可用虚拟内存，超过这一个数值将会抛出OutOfMemory / 1024
        int cacheSize = maxMemory / 8; //使用第1/8的可用内存为这个内存缓存。
        mMemoryLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount(); //缓存大小将以千字节为单位而不是项目数。  / 1024
            }
        };
    }

    /***
     * 读取缓存中的图片
     *
     * @param imgPath
     * @return
     */
    public Bitmap getMemoryBitMap(String imgPath) {
        String fileName = LoadImageUtils.getFileMD5(imgPath);
//        memoryHashMap.get(fileName);
//        SoftReference<Bitmap> softReference = softReferenceHashMap.get(fileName);
//        if (softReference != null) {
//            return softReference.get();
//        }
        Bitmap bitmap = mMemoryLruCache.get(fileName);
        if (bitmap != null) {
            return bitmap;
        }
        return null;
    }

    /**
     * 缓存中存储图片
     *
     * @param imgPath
     * @param bitmap
     */
    public void setMemoryBitMap(String imgPath, Bitmap bitmap) {
        String fileName = LoadImageUtils.getFileMD5(imgPath);
//        memoryHashMap.put(fileName, bitmap);
//        softReferenceHashMap.put(fileName, new SoftReference(bitmap));

        if (getMemoryBitMap(fileName) == null) {
            mMemoryLruCache.put(fileName, bitmap);
        }

    }

}
