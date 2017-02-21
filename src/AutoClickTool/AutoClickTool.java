package AutoClickTool;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by JiaXin on 2017/2/6.
 */
public class AutoClickTool implements Runnable{

    private String mobileXMLPath = "/sdcard/ui.xml" ;
    private String mobileXMLLocalPath = System.getProperty("user.dir");
    private String password = "cfca1234";
    private String clickname ;
    private String windowname ;
    private int keyboard ;
    private String devicename;
    private String threadname;
    public static void main(String[] args) {

        String [] a =new AutoClickTool().getItemPosition("btn_install","E:\\AndroidSDK\\platform-tools\\u.xml");
        for(int i=0 ; i<a.length;i++){
            System.out.print(a[i]+"\n");
        }
    }

    public void setClickname(String str){
        clickname = str;
    }
    public void setWindowname(String str){
        windowname = str;
    }
    public void setKeyboard(int str){
        keyboard = str;
    }
    public void setDevicename(String str){
        devicename = str;
    }
    public void setThreadname(String str){
        threadname = str;
    }
    public void setPassword(String str){
        password = str;
    }
    @Override
    public void run() {
        System.out.print(threadname+":设备"+devicename+"针对"+windowname+"页面点击"+clickname+"的线程启动\n");
        autoClick(clickname,windowname,keyboard,devicename);
    }


    public void autoClick(String clickkeywords,String windowsKeyWords,int keyboard,String devicename){

        while (1==1){
            CommonTool ct  = new CommonTool();
            String nowWin = ct.execCommand("adb -s "+devicename+" shell dumpsys window | grep mCurrentFocus");
            if(nowWin.contains(windowsKeyWords)){
                System.out.print(threadname+":"+devicename+" is found "+windowsKeyWords+" windows\n");

                if (keyboard==1){
                    //有软键盘弹出，输入内容，点击后退取消键盘,并且等待3秒屏幕正常后继续
                    ct.execCommand("adb -s "+devicename+" shell input text '"+password+"'");
                    System.out.print(threadname+":"+"input password:"+password+"\n");
                    ct.execCommand("adb -s "+devicename+" shell input keyevent 4");
                    try {
                        Thread.sleep(3000);//停顿3秒钟等待页面响应后再点击
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ct.execCommand("adb -s "+devicename+" shell uiautomator dump "+mobileXMLPath);

                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");//可以方便地修改日期格式
                String nowtime = dateFormat.format( now );
                String filename = mobileXMLLocalPath+"/"+threadname+nowtime+".xml";
                ct.execCommand("adb -s "+devicename+" pull "+mobileXMLPath+" "+filename);
                ct.execCommand("adb -s "+devicename+" shell rm -f -r "+mobileXMLPath);
                String[] s = getItemPosition("text=\""+clickkeywords+"\"",filename);
                if(s[0].equals("0") && s[1].equals("0")) {
                    //如果根据text没有找到按钮，在根据ID找一次
                    s = getItemPosition(clickkeywords,filename);
                }
                if(!s[0].equals("0") && !s[1].equals("0")){
                    System.out.print(threadname+":"+"click button :"+s[0]+","+s[1]);
                    ct.execCommand("adb -s "+devicename+" shell input tap "+s[0]+" "+s[1]);
                }
                try {
                    //线程结束前删除该线程的XML文件
                    File file = new File(filename);
                    file.delete();
                    Thread.sleep(5000);//停顿5秒钟等待页面响应后再结束线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String[] getItemPosition(String ItemText,String xmlfilepath) {

        String [] pos ={"0","0"};
        File file = new File(xmlfilepath);
        BufferedReader reader = null;
        String xmlstr = "";
        try {
            if(file.exists()){
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    xmlstr += tempString;
                    line++;
                }
                reader.close();
                int index1 = xmlstr.indexOf(ItemText);
                if (index1!=-1){
                    String substr1 = xmlstr.substring(index1);
                    int index2 = substr1.indexOf("bounds");
                    String substr2 = substr1.substring(index2);
                    int index3 = substr2.indexOf("[");
                    int index4 = substr2.indexOf("]");
                    String substr3 = substr2.substring(index3+1,index4);
                    pos = substr3.split(",");
                    return pos;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return pos;
        }
        return pos;
    }
}