package sk.kottman.androlua;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Created by tangqipeng
 * 2018/8/8
 * email: tangqipeng@aograph.com
 */
public class TestActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {

    // public so we can play with these from Lua
    public Button execute;
    public EditText source;
    public TextView status;
    public LuaState L;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        execute = (Button) findViewById(R.id.executeBtn);
        execute.setOnClickListener(this);

        source = (EditText) findViewById(R.id.source);
        source.setOnLongClickListener(this);

        String s = "function string.gsplit(str)\n" +
                "\tlocal str_tb = {}\n" +
                "\tif string.len(str) ~= 0 then\n" +
                "\t\tfor i=1,string.len(str) do\n" +
                "\t\t\tnew_str= string.sub(str,i,i)\t\t\t\n" +
                "\t\t\tif (string.byte(new_str) >=48 and string.byte(new_str) <=57) or (string.byte(new_str)>=65 and string.byte(new_str)<=90) or (string.byte(new_str)>=97 and string.byte(new_str)<=122) then \t\t\t\t\n" +
                "\t\t\t\ttable.insert(str_tb,string.sub(str,i,i))\t\t\t\t\n" +
                "\t\t\telse\n" +
                "\t\t\t\treturn nil\n" +
                "\t\t\tend\n" +
                "\t\tend\n" +
                "\t\treturn str_tb\n" +
                "\telse\n" +
                "\t\treturn nil\n" +
                "\tend\n" +
                "end";
        source.setText(s);

        status = (TextView) findViewById(R.id.statusText);
        status.setMovementMethod(ScrollingMovementMethod.getInstance());

        String data = "{-67.979996, -67.979996, -67.979996, -67.979996, -67.979996, -67.86, -67.74, -67.92, -68.04, -68.46, -68.82, -68.82, -68.64, -68.52, -68.64, -68.52, -68.46, -68.46, -68.46, -68.46, -68.58, -68.939995, -68.82, -69.0, -68.939995, -69.119995, -69.06, -69.18, -69.299995, -69.42, -69.42, -69.78, -69.78, -69.6, -69.479996, -69.36, -69.36, -68.76, -68.1, -67.74, -67.56, -66.96, -66.36, -65.88, -65.46, -65.34, -65.7, -66.0, -66.119995, -66.9, -66.9, -66.299995, -66.299995, -65.7, -64.86, -64.38, -63.719997, -63.239998, -63.239998, -63.18, -62.94, -62.46, -62.699997, -62.879997, -63.059998, -63.0, -62.879997, -63.48, -63.48, -63.96, -64.02, -64.02, -64.2, -64.32, -64.2, -64.92, -65.04, -65.34, -65.88, -65.88, -66.119995, -65.939995, -65.46, -65.64, -65.64, -64.979996, -65.22, -65.28, -65.4, -65.4, -65.28, -65.4, -65.4, -65.1, -65.1, -64.56, -64.799995, -64.619995, -64.68, -64.32, -64.56, -64.86, -64.92, -65.22, -65.159996, -65.28, -65.46, -65.64, -65.46, -65.64, -65.76, -65.7, -65.76, -66.119995, -65.82, -65.82, -65.64, -65.52, -65.34, -64.86, -64.32, -63.6, -63.48, -63.6, -63.42, -63.3, -64.26, -64.619995, -65.52, -66.18, -66.6, -67.08, -67.38, -67.68, -67.799995, -67.619995, -67.619995, -67.439995, -67.5, -67.56, -67.68, -67.92, -67.86, -67.979996, -68.159996, -68.34, -68.28, -68.28, -68.34, -68.28, -68.46, -68.58, -68.46, -68.64, -68.58, -68.58, -68.76, -68.52, -68.7, -68.939995, -68.76, -68.82, -68.82, -68.82, -68.76, -69.0, -68.82, -68.64, -68.64, -68.58, -68.64, -68.7, -68.64, -68.7, -68.82, -68.939995, -69.18, -68.939995, -68.88, -68.82, -68.28, -67.86, -67.619995, -67.02, -66.54, -66.119995, -65.76, -65.1, -64.979996, -64.56, -63.899998, -63.539997, -63.059998, -62.82, -62.879997, -63.059998, -63.3, -63.78, -64.08, -64.38, -64.799995, -65.1, -65.22, -64.979996, -64.56, -64.38, -64.14, -64.14, -63.899998, -63.6, -63.539997, -63.48, -63.18, -63.18, -63.18, -63.18, -63.66, -63.899998, -64.08, -64.439995, -64.74, -64.92, -64.799995, -64.619995, -64.5, -64.26, -63.96, -63.78, -63.6, -63.719997, -63.96, -64.32, -64.5, -64.979996, -65.52, -65.64, -66.0, -65.64, -65.76, -65.76, -66.659996}";


        L = LuaStateFactory.newLuaState();
        L.openLibs();

        L.LdoString(s);
        LuaObject func = L.getLuaObject("string.gsplit");// 找到foo函数
        Object[] result = null;
        try {
            result = func.call(new Object[]{data, ","}, 1);
        } catch (LuaException e) {
            e.printStackTrace();
        }// 传递值过去，返回值有1个
        Log.i("haha", "test.lua--> testAdd返回值  : " + result[0]);

//        try {
//            L.pushJavaObject(this);
//            L.setGlobal("activity");
//
//            JavaFunction print = new JavaFunction(L) {
//                @Override
//                public int execute() throws LuaException {
//                    for (int i = 2; i <= L.getTop(); i++) {
//                        int type = L.type(i);
//                        String stype = L.typeName(type);
//                        String val = null;
//                        if (stype.equals("userdata")) {
//                            Object obj = L.toJavaObject(i);
//                            if (obj != null)
//                                val = obj.toString();
//                        } else if (stype.equals("boolean")) {
//                            val = L.toBoolean(i) ? "true" : "false";
//                        } else {
//                            val = L.toString(i);
//                        }
//                        if (val == null)
//                            val = stype;
//                        output.append(val);
//                        output.append("\t");
//                    }
//                    output.append("\n");
//                    return 0;
//                }
//            };
//            print.register("print");

//            JavaFunction assetLoader = new JavaFunction(L) {
//                @Override
//                public int execute() throws LuaException {
//                    String name = L.toString(-1);
//
//                    AssetManager am = getAssets();
//                    try {
//                        InputStream is = am.open(name + ".lua");
//                        byte[] bytes = readAll(is);
//                        L.LloadBuffer(bytes, name);
//                        return 1;
//                    } catch (Exception e) {
//                        ByteArrayOutputStream os = new ByteArrayOutputStream();
//                        e.printStackTrace(new PrintStream(os));
//                        L.pushString("Cannot load module "+name+":\n"+os.toString());
//                        return 1;
//                    }
//                }
//            };

//            L.getGlobal("package");            // package
//            L.getField(-1, "loaders");         // package loaders
//            int nLoaders = L.objLen(-1);       // package loaders

//            L.pushJavaFunction(assetLoader);   // package loaders loader
//            L.rawSetI(-2, nLoaders + 1);       // package loaders
//            L.pop(1);                          // package

//            L.getField(-1, "path");            // package path
//            String customPath = getFilesDir() + "/?.lua";
//            L.pushString(";" + customPath);    // package path custom
//            L.concat(2);                       // package pathCustom
//            L.setField(-2, "path");            // package
//            L.pop(1);

//            L.LdoString(source.getText().toString());
//
//            String text = L.toString(-1);
//            status.setText(text);
//            //调用lua中的函数
//            L.getField(LuaState.LUA_GLOBALSINDEX, "path");
//            L.pushString("in the pad!");   //传入参数
//            L.call(1,1);                    //调用函数指定参数和返回值个数
//
//            L.setField(LuaState.LUA_GLOBALSINDEX, "a"); //将返回值保存到参数a中
//            LuaObject obj = L.getLuaObject("a");        //获取参数a
//
//            status.setText(obj.toString());

//            L.nativeInit(this.getApplicationContext());
//            L.aographSensor();
//            L.openSensor(1);

            // 找到函数 sum
//            L.getField(LuaState.LUA_GLOBALSINDEX, "sum");
//
//            // 参数1压栈
//            L.pushNumber(100);
//
//            // 参数2压栈
//            L.pushNumber(50);
//
//            // 调用，共2个参数1个返回值
//            L.call(2, 1);

//            // 保存返回值到result中
//            L.setField(LuaState.LUA_GLOBALSINDEX, "result");
//
//            // 读入result
//            LuaObject lobj = L.getLuaObject("result");
//            // 打印结果
//            System.out.println(lobj.getNumber());
//
//        } catch (Exception e) {
//            status.setText("Cannot override print");
//        }

//        String temp = loadAssetsString("http://192.168.1.21:6014/appupdate/testLua.lua");

    }

    private static byte[] readAll(InputStream input) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    final StringBuilder output = new StringBuilder();

    String evalLua(String src) throws LuaException {
        L.setTop(0);
        int ok = L.LloadString(src);
        if (ok == 0) {
//            L.getField(LuaState.LUA_GLOBALSINDEX, "sum");
//
//            // 参数1压栈
//            L.pushNumber(100);
//
//            // 参数2压栈
//            L.pushNumber(50);
//
//            // 调用，共2个参数1个返回值
//            L.call(2, 1);

            L.getGlobal("debug");
            L.getField(-1, "traceback");
            L.remove(-2);
            L.insert(-2);
            ok = L.pcall(0, 0, -2);

            if (ok == 0) {
                String res = output.toString();
                output.setLength(0);
                return res;
            }
        }
        throw new LuaException(errorReason(ok) + ": " + L.toString(-1));
        //return null;

    }

    private String errorReason(int error) {
        switch (error) {
            case 4:
                return "Out of memory";
            case 3:
                return "Syntax error";
            case 2:
                return "Runtime error";
            case 1:
                return "Yield error";
        }
        return "Unknown error " + error;
    }

    @Override
    public void onClick(View view) {
        String src = source.getText().toString();
        status.setText("");
        try {
            String res = evalLua(src);
            status.append(res);
            status.append("Finished succesfully");
        } catch(LuaException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        source.setText("");
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        L.
    }
}
