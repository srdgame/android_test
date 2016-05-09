package minie.irpc;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import Ice.InitializationData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by fjw on 2016/2/20.
 */
public class minie_app_client {
    static Ice.Communicator s_ic;
    public static String s_cafile = "cacert.bks";
    public static String s_certfile = "client.bks";
    static Object s_obj = new Object();
    static Context s_ctx = null;

    private static void cache_cert_file(String filename)throws IOException
    {
        AssetManager am = null;
        InputStream ca_stream = null;
        try
        {
            am =  s_ctx.getResources().getAssets();
            ca_stream = am.open(filename);
            int len = ca_stream.available();
            byte[] ca_data = new byte[len];
            ca_stream.read(ca_data);
            File dest = new File(s_ctx.getCacheDir().getAbsoluteFile() + File.separator + filename);
            FileOutputStream fo = new FileOutputStream(dest);
            if(fo!=null)
            {
                fo.write(ca_data);
                fo.close();
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
        finally
        {
            if(ca_stream!=null)
            {
                ca_stream.close();
            }
        }
    }
    //sta
    public static boolean init(android.content.Context ctx)
    {
        if(s_ctx == null)
        {
            synchronized (s_obj)
            {
                if(s_ctx==null)
                {
                    s_ctx = ctx;
                    try
                    {
                        cache_cert_file(s_cafile);
                        cache_cert_file(s_certfile);
                        String[] args = new String[] {
                                "--Ice.ThreadPool.Server.SizeMax=4",
                                "--Ice.ThreadPool.Client.SizeMax=8",
                                "--Ice.MessageSizeMax=10485760",
                                "--Ice.Override.ConnectTimeout=5000",
                                "--Ice.Override.Timeout=8000",
                                "--Ice.Trace.Network=1",
                                "--Ice.Plugin.IceSSL=IceSSL.PluginFactory",
                                //"--IceSSL.DefaultDir=./assets",
                                "--IceSSL.DefaultDir=" + s_ctx.getCacheDir(),
                                "--IceSSL.Keystore=" + s_certfile,
                                "--IceSSL.Truststore="+ s_cafile,
                                //"--IceSSL.Password=123456",
                                "--IceSSL.VerifyPeer=0",
                                //"--IceSSL.Ciphers=ALL !kEDH!ADH:RC4+RSA:+HIGH:+MEDIUM:+LOW:+SSLv2:+EXP:@STRENGTH",
                        };
                        s_ic = Ice.Util.initialize(args);
                    }
                    catch(Exception ex)
                    {
                        s_ctx = null;
                    }
                }
            }
        }
        ctx.getAssets();
//        Ice.Properties props = Ice.Util.createProperties();
//        props.setProperty("Ice.ThreadPool.Server.SizeMax","4");
//        Ice.InitializationData initdata = new InitializationData();
//        initdata.properties =props;
        return s_ic != null;
    }
    protected String _proxy_str = "";
    protected AppAuthPrx _prx_base = null;
    protected AppServicePrx _prx_app = null;
    protected boolean _is_logged_in = false;

    public minie_app_client(String proxyStr)
    {
        _proxy_str = proxyStr;
        _prx_base = AppAuthPrxHelper.uncheckedCast(s_ic.stringToProxy(_proxy_str));
    }
    public minie_app_client(String host, int port)
    {
        this("minie_app_auth_service", "ssl", host, port, 5000);
    }
    public minie_app_client(String proxyName, String mode, String host, int port, int timeout)
    {
        this( String.format("%s:%s -h %s -p %d -t %d", proxyName, mode, host, port, timeout ));
    }

    public boolean isLogin()
    {
        return _is_logged_in;
    }

    public AppServicePrx getProxy()
    {
        return _is_logged_in? _prx_app:null;
    }

    public int Login(String un, String pw)
    {
        try
        {
            AppServicePrx prx = _prx_base.login_app(un, pw);
            if(prx!=null)
            {
                _prx_app = AppServicePrxHelper.checkedCast(prx.ice_endpoints(_prx_base.ice_getEndpoints()));
                if (_prx_app != null)
                {
                    _is_logged_in = true;
                    return 0;
                }
            }
            return 3;
        }
        catch(AuthError ae)
        {
            return 1;
        }
        catch(Ice.Exception iex)
        {
            return 2;
        }
    }

    public void Logout()
    {
        if(_prx_app != null)
        {
            _prx_app.logout();
            _prx_app = null;
            _is_logged_in = false;
        }
    }
}