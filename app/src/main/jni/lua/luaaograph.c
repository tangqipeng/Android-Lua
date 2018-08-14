//
// Created by 汤奇朋 on 2018/8/9.
//

#define luaaograph_c
#define LUA_LIB

#include "lua.h"

#include "lauxlib.h"
#include "lualib.h"

static int aograph_openSensor(lua_State *L) {
    int sensorType = lua_tointeger(L, -2);
    int intervalInMs = lua_tointeger(L, -1);

    return 0;
}

static int aograph_closeSensor(lua_State *L) {
    int sensorType = lua_tointeger(L, -1);

    return 0;
}

static const struct luaL_Reg aograph_libs[] = {
        {"openSensor",  aograph_openSensor},
        {"closeSensor", aograph_closeSensor},

        {NULL, NULL}  /*the end*/
};

LUALIB_API int luaopen_aograph(lua_State *L) {
    /*注册lib， 上面luaopen_名称 跟下面注册的名称要一致, 还要和编译的.so文件名一致*/
    //luaL_newlib(L, aograph_libs);
    luaL_register(L, LUA_AOGRAPHNAME, aograph_libs);
    return 1;
}
