package cn.com.misakanetwork.enum

enum class OSSInfo(val bucket:String) {
    //网站静态文件地址
    STATIC("misaka-networks-static"),
    //图片收藏
    PICTURE("misaka-networks-picture"),
    //文章markdown存储
    MARKDOWN("misaka-networks-article"),
    //照片
    PHOTO("")
}
