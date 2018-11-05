import com.alibaba.fastjson.JSONObject;
import config.ConfigAnalyzer;
import org.apache.catalina.Host;
import org.apache.catalina.Server;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * created by huimeng.li on 2018/11/5 19:36
 * description:
 **/


public class WebServce {


    //设置主机或ip
    private String hostname = ConfigAnalyzer.webConfig.get("hostname").toString();
    //设置端口,默认的端口，主要看配置属性
    private int port = Integer.parseInt(ConfigAnalyzer.webConfig.get("port").toString());
    //
    private String webappDir = ConfigAnalyzer.webConfig.get("webappDir").toString();

    //设置 连接时的一些参数
    private int maxPostSize = Integer.parseInt(ConfigAnalyzer.webConfig.get("maxPostSize").toString());
    private int maxThreads = Integer.parseInt(ConfigAnalyzer.webConfig.get("maxThreads").toString());
    private int acceptCount = Integer.parseInt(ConfigAnalyzer.webConfig.get("acceptCount").toString());

    //tomcat引用
    private Tomcat tomcat;

    public WebServce() {
    }

    //启动
    public void start() {
        try {
            //tomcat实例
            this.tomcat = new Tomcat();
            this.tomcat.setPort(this.port);
            this.tomcat.setHostname(hostname);
            //tomcat存储自身信息，保存在项目目录下
            this.tomcat.setBaseDir(".");

            this.configServer(this.tomcat.getServer());
            this.tomcat.getEngine();
            this.configHost(this.tomcat.getHost());
            this.configConnector(this.tomcat.getConnector());
            //第一个参数上下文路径contextPath,第二个参数docBase
            this.tomcat.addWebapp("", System.getProperty("user.dir") + File.separator + this.webappDir);

            //这种方式也行
            //  this.tomcat.getHost().setAppBase(System.getProperty("user.dir")+ File.separator+".");
            //  this.tomcat.addWebapp("",this.webappDir);

            this.tomcat.start();
//            this.tomcat.getServer().await();

        } catch (Exception e) {

        }
    }

    private void configHost(Host host) {
        //user.dir  用户的当前工作目录
        host.setAppBase(System.getProperty("user.dir"));
    }

    private void configServer(Server server) {
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
    }

    //设置连接属性
    private void configConnector(Connector connector) {
        connector.setURIEncoding("UTF-8");
        connector.setMaxPostSize(this.maxPostSize);
        connector.setAttribute("maxThreads", Integer.valueOf(this.maxThreads));
        connector.setAttribute("acceptCount", Integer.valueOf(this.acceptCount));
        connector.setAttribute("disableUploadTimeout", Boolean.valueOf(true));
        connector.setAttribute("enableLookups", Boolean.valueOf(false));
    }


    public static WebServce getWebserce() {
        return new WebServce();
    }



}
