package sk.kottman.androlua;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tangqipeng
 * 2018/8/14
 * email: tangqipeng@aograph.com
 */
public class Test2Activity extends Activity {

    public TextView status;
    public LuaState L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        status = (TextView) findViewById(R.id.statusText);

        String s = "win_len = 120\n" +
                "function getEntropy(param)\n" +
                "\tlocal s = #param\n" +
                "\tlocal t = {}\n" +
                "\tfor i=1,s do\n" +
                "\t\tkey = param[i]\n" +
                "\t\tif (t[key]) then\n" +
                "\t\t\tt[key]=t[key]+1\n" +
                "\t\telse\n" +
                "\t\t\tt[key]=1\n" +
                "\t\tend\n" +
                "\tend\n" +
                "\tlocal ts = getCount(t)\n" +
                "\tlocal entropy = 0\n" +
                "\tfor k,v in pairs(t) do\n" +
                "\t\tlocal p = v / ts\n" +
                "\t\tentropy = entropy - p * math.log(p)\n" +
                "\tend\n" +
                "\tprint(entropy)\n" +
                "\treturn entropy\n" +
                "end\n" +
                "function getCount( param )\n" +
                "\tlocal size = 0;\n" +
                "\tfor k,v in pairs(param) do\n" +
                "\t\tsize = size + 1\n" +
                "\tend\n" +
                "\treturn size\n" +
                "end\n" +
                "function getMagneticResult( param )\n" +
                "\tlocal magArray = StringToArray(param)\n" +
                "\tentropy_limit = 4.0\n" +
                "\tmove_count_limit = 50\n" +
                "\tlocal moving_count = 0\n" +
                "\tlocal result = 2\n" +
                "\tlocal arraySize = #magArray\n" +
                "\tprint(\"arraySize:\"..arraySize)\n" +
                "\tif arraySize < win_len then\n" +
                "\t\tresult = 3\n" +
                "\t\treturn result\n" +
                "\tend\n" +
                "\tfor i=1,arraySize do\n" +
                "\t\tif i > win_len then\n" +
                "\t\t\tlocal enarray = getArray(i, magArray)\n" +
                "\t\t\tlocal entropy = getEntropy(enarray)\n" +
                "\t\t\tif entropy > entropy_limit then\n" +
                "\t\t\t\tmoving_count = moving_count + 1\n" +
                "\t\t\tend\n" +
                "\t\tend\n" +
                "\tend\n" +
                "\tif moving_count > move_count_limit then\n" +
                "\t\tresult = -1\n" +
                "\t\treturn result\n" +
                "\tend\n" +
                "\treturn result\n" +
                "end\n" +
                "function getArray( num, param )\n" +
                "\tlocal array = {}\n" +
                "\tlocal size = #param\n" +
                "\tfor i=num-win_len,num do\n" +
                "\t\tarray[i+win_len-num+1] = param[i]\n" +
                "\tend\n" +
                "\treturn array\n" +
                "end\n" +
                "function StringToArray( param )\n" +
                "\tlocal index = {}\n" +
                "\tlocal oid = {}\n" +
                "\tprint(\"array lenth:\"..string.len(param))\n" +
                "\tfor k=1,string.len(param) do\n" +
                "\t\tif string.sub(param,k,k) == ',' then\n" +
                "    \t\ttable.insert(index,k)\n" +
                "  \t\tend\n" +
                "\tend\n" +
                "\ttable.insert(oid,string.sub(param,1,index[1]-1))\n" +
                "\tfor k=1,#index-1 do\n" +
                "\t\ttable.insert(oid,string.sub(param,index[k]+1,index[k+1]-1))\n" +
                "\tend\n" +
                "\ttable.insert(oid,string.sub(param,index[#index]+1,string.len(param)))\n" +
                "\treturn oid\n" +
                "end";

//        Double[] data = {-67.979996, -67.979996, -67.979996, -67.979996, -67.979996, -67.86, -67.74, -67.92, -68.04, -68.46, -68.82, -68.82, -68.64, -68.52, -68.64, -68.52, -68.46, -68.46, -68.46, -68.46, -68.58, -68.939995, -68.82, -69.0, -68.939995, -69.119995, -69.06, -69.18, -69.299995, -69.42, -69.42, -69.78, -69.78, -69.6, -69.479996, -69.36, -69.36, -68.76, -68.1, -67.74, -67.56, -66.96, -66.36, -65.88, -65.46, -65.34, -65.7, -66.0, -66.119995, -66.9, -66.9, -66.299995, -66.299995, -65.7, -64.86, -64.38, -63.719997, -63.239998, -63.239998, -63.18, -62.94, -62.46, -62.699997, -62.879997, -63.059998, -63.0, -62.879997, -63.48, -63.48, -63.96, -64.02, -64.02, -64.2, -64.32, -64.2, -64.92, -65.04, -65.34, -65.88, -65.88, -66.119995, -65.939995, -65.46, -65.64, -65.64, -64.979996, -65.22, -65.28, -65.4, -65.4, -65.28, -65.4, -65.4, -65.1, -65.1, -64.56, -64.799995, -64.619995, -64.68, -64.32, -64.56, -64.86, -64.92, -65.22, -65.159996, -65.28, -65.46, -65.64, -65.46, -65.64, -65.76, -65.7, -65.76, -66.119995, -65.82, -65.82, -65.64, -65.52, -65.34, -64.86, -64.32, -63.6, -63.48, -63.6, -63.42, -63.3, -64.26, -64.619995, -65.52, -66.18, -66.6, -67.08, -67.38, -67.68, -67.799995, -67.619995, -67.619995, -67.439995, -67.5, -67.56, -67.68, -67.92, -67.86, -67.979996, -68.159996, -68.34, -68.28, -68.28, -68.34, -68.28, -68.46, -68.58, -68.46, -68.64, -68.58, -68.58, -68.76, -68.52, -68.7, -68.939995, -68.76, -68.82, -68.82, -68.82, -68.76, -69.0, -68.82, -68.64, -68.64, -68.58, -68.64, -68.7, -68.64, -68.7, -68.82, -68.939995, -69.18, -68.939995, -68.88, -68.82, -68.28, -67.86, -67.619995, -67.02, -66.54, -66.119995, -65.76, -65.1, -64.979996, -64.56, -63.899998, -63.539997, -63.059998, -62.82, -62.879997, -63.059998, -63.3, -63.78, -64.08, -64.38, -64.799995, -65.1, -65.22, -64.979996, -64.56, -64.38, -64.14, -64.14, -63.899998, -63.6, -63.539997, -63.48, -63.18, -63.18, -63.18, -63.18, -63.66, -63.899998, -64.08, -64.439995, -64.74, -64.92, -64.799995, -64.619995, -64.5, -64.26, -63.96, -63.78, -63.6, -63.719997, -63.96, -64.32, -64.5, -64.979996, -65.52, -65.64, -66.0, -65.64, -65.76, -65.76, -66.659996};

        String data = "-67.979996, -67.979996, -67.979996, -67.979996, -67.979996, -67.86, -67.74, -67.92, -68.04, -68.46, -68.82, -68.82, -68.64, -68.52, -68.64, -68.52, -68.46, -68.46, -68.46, -68.46, -68.58, -68.939995, -68.82, -69.0, -68.939995, -69.119995, -69.06, -69.18, -69.299995, -69.42, -69.42, -69.78, -69.78, -69.6, -69.479996, -69.36, -69.36, -68.76, -68.1, -67.74, -67.56, -66.96, -66.36, -65.88, -65.46, -65.34, -65.7, -66.0, -66.119995, -66.9, -66.9, -66.299995, -66.299995, -65.7, -64.86, -64.38, -63.719997, -63.239998, -63.239998, -63.18, -62.94, -62.46, -62.699997, -62.879997, -63.059998, -63.0, -62.879997, -63.48, -63.48, -63.96, -64.02, -64.02, -64.2, -64.32, -64.2, -64.92, -65.04, -65.34, -65.88, -65.88, -66.119995, -65.939995, -65.46, -65.64, -65.64, -64.979996, -65.22, -65.28, -65.4, -65.4, -65.28, -65.4, -65.4, -65.1, -65.1, -64.56, -64.799995, -64.619995, -64.68, -64.32, -64.56, -64.86, -64.92, -65.22, -65.159996, -65.28, -65.46, -65.64, -65.46, -65.64, -65.76, -65.7, -65.76, -66.119995, -65.82, -65.82, -65.64, -65.52, -65.34, -64.86, -64.32, -63.6, -63.48, -63.6, -63.42, -63.3, -64.26, -64.619995, -65.52, -66.18, -66.6, -67.08, -67.38, -67.68, -67.799995, -67.619995, -67.619995, -67.439995, -67.5, -67.56, -67.68, -67.92, -67.86, -67.979996, -68.159996, -68.34, -68.28, -68.28, -68.34, -68.28, -68.46, -68.58, -68.46, -68.64, -68.58, -68.58, -68.76, -68.52, -68.7, -68.939995, -68.76, -68.82, -68.82, -68.82, -68.76, -69.0, -68.82, -68.64, -68.64, -68.58, -68.64, -68.7, -68.64, -68.7, -68.82, -68.939995, -69.18, -68.939995, -68.88, -68.82, -68.28, -67.86, -67.619995, -67.02, -66.54, -66.119995, -65.76, -65.1, -64.979996, -64.56, -63.899998, -63.539997, -63.059998, -62.82, -62.879997, -63.059998, -63.3, -63.78, -64.08, -64.38, -64.799995, -65.1, -65.22, -64.979996, -64.56, -64.38, -64.14, -64.14, -63.899998, -63.6, -63.539997, -63.48, -63.18, -63.18, -63.18, -63.18, -63.66, -63.899998, -64.08, -64.439995, -64.74, -64.92, -64.799995, -64.619995, -64.5, -64.26, -63.96, -63.78, -63.6, -63.719997, -63.96, -64.32, -64.5, -64.979996, -65.52, -65.64, -66.0, -65.64, -65.76, -65.76, -66.659996";
        L = LuaStateFactory.newLuaState();
        L.openLibs();

        L.LdoString(s);
        LuaObject func = L.getLuaObject("getMagneticResult");// 找到foo函数
        Object[] result = null;
        try {
            result = func.call(new Object[]{data}, 1);
        } catch (LuaException e) {
            e.printStackTrace();
        }// 传递值过去，返回值有1个
        Log.i("haha", "test.lua--> testAdd返回值  : " + result[0]);

    }

    /**
     * android解析lua
     * lua脚本执行java代码
     */
    private void executeLua2Java(){
        LuaState mLuaState = LuaStateFactory.newLuaState();
        mLuaState.openLibs();

        String fileName = "test.json";

        try {
            InputStream in = this.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
//            String res = EncodingUtils.getString(buffer, "ANSI");

//            ArrayList<Object> b=new ArrayList<Object>();
//            b.add(new SimpleClassData(4));

//            mLuaState.LdoString(res);
//            mLuaState.getField(LuaState.LUA_GLOBALSINDEX, "callJavaMethod");
//            for(Object o : b){
//                mLuaState.pushJavaObject(o);
//            }
//            mLuaState.call(b.size(), 1);
//            mLuaState.setField(LuaState.LUA_GLOBALSINDEX, "resultKey");
//            LuaObject result = mLuaState.getLuaObject("resultKey");

//            Log.i("haha", "test.lua-->testObject 返回值 ： " + result.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
