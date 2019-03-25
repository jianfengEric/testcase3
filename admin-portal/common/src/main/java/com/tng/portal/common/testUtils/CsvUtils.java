package com.tng.portal.common.testUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

/**
 * Created by Eric on 2019/3/8.
 */
public class CsvUtils implements Iterator<Object[]> {
    BufferedReader in;
    ArrayList<String> csvList=new ArrayList<String>();
    int rowNum=0;     //行数
    int columnNum=0;  //列数
    int curRowNo=0;   //当前行数
    String columnName[];  //列名
    /**
     * 在TestNG中由@DataProvider(dataProvider = "name")修饰的方法取csv数据时，
     * 调用此类构造方法（此方法会得到列名），
     * 返回给由@Test(dataProvider = "name")修饰的方法，如此
     * 反复到数据读完为止
     * @param fileName 文件名
     * @throws IOException
     */
    public CsvUtils(String fileName, String path) throws IOException {
        File directory=new File(".");
        /*String path;//默认文件路径
        if("dev".equals(PropertiesUtil.getPropertyValueByKey("current.environment"))){
            path=".src.main.java.com.gea.portal.ewp.testData.devTestData.";
        }else{
            path=".src.main.java.com.gea.portal.ewp.testData.sitTestData.";
        }*/
        String absolutePath=directory.getCanonicalPath()+path.replaceAll("\\.", Matcher.quoteReplacement("\\"))+fileName;
        System.out.println(absolutePath);   //打印路径
        File csv=new File(absolutePath);
        in=new BufferedReader(new FileReader(csv)); //读取csv数据
        while (in.ready()) {
            csvList.add(in.readLine());
            this.rowNum++;
        }
        String[] str=csvList.get(0).split(",");
        this.columnNum=str.length;
        columnName=new String[columnNum];
        //获取列名
        for (int i = 0; i < columnNum; i++) {
            columnName[i]=str[i];
        }
        this.curRowNo++;
    }
    @Override
    public boolean hasNext() {
        // TODO Auto-generated method stub
        if(rowNum==0||curRowNo>=rowNum){
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }else{
            return true;
        }
    }
    /**
     * 获取一组参数，即一行数据
     */
    @Override
    public Object[] next() {
        // TODO Auto-generated method stub
        Map<String,String> s=new TreeMap<String,String>();
        String csvCell[]=csvList.get(curRowNo).split(",");
        for(int i=0;i<this.columnNum;i++){
            s.put(columnName[i], csvCell[i]);
        }
        Object[] d=new Object[1];
        d[0]=s;
        this.curRowNo++;
        return d;
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("remove unsupported");
    }

}
