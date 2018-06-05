# Android-Lua
lua脚本在Android中运行

这是一个Android studio下运行的项目；
查看jniLibs下是否有libluajava.so文件，没有的话用命令进入jni文件夹：如果是Android studio的项目的话运行下面的语句：
先：cd app/src/main/jni
再：ndk-build
当运行完时，会发现在main下面会有obj文件夹和libs文件夹，这是运行ndk生成的libluajava.so文件，将libs下的文件夹拷贝到jniLibs下
主项目下的build.gradle中的配置需要加上：
1.在defaultConfig下加入
ndk{
            abiFilters "armeabi", "armeabi-v7a", "armeabi-v8a", "x86", "x86_64"
        }
        这些项可以选择使用
2.加入sourceSets {
        main {
            jni.srcDirs = []
        }
    }
3.重刷gradle即可

注意：
ndk-build可能运行失败，要配置环境
mac:
mac上的配置，先要打开.bash_profile文件
如果第一次打开，执行下面的命令：
1. touch .bash_profile
2. open -e .bash_profile

如果不是第一次，直接执行 open .bash_profile

在里面加入：
export NDK_ROOT="/Users/**/Library/Android/sdk/ndk-bundle"
export PATH=${PATH}:${NDK_ROOT}

保存关闭，重启终端，运行 ndk-build

widows：
 进入环境变量的高级配置

 找到ndk的位置，放入path中就ok
