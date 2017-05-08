package com.ody.library.util.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by meijunqiang on 2017/5/4.
 * 描述:根据适配类型 批量对dimen值进行转换
 */

public class DimenTool {
    public static void main(String[] args) throws FileNotFoundException {
        //TODO:meiyizhi 可以随时执行，或项目开发完成后执行转换操作
        genDimen();
//        gen320Dimen();
    }

    /**
     * 初始化1-100的dp值和12-18的偶数sp值（可以根据需要拓展）
     */
    private static void gen320Dimen() throws FileNotFoundException {
        String sw320File = "./library/src/main/res/values/dimens.xml";
        StringBuilder sw320 = new StringBuilder();
        sw320.append("<resources>\r\n");
        for (int i = 1; i <= 100; i++) {
            sw320.append("\t<dimen name='dp_" + i + "'>" + i + "dp</dimen>\r\n");
        }
        for (int i = 12; i <= 18; i = i + 2) {
            sw320.append("\t<dimen name='sp_" + i + "'>" + i + "sp</dimen>\r\n");
        }
        sw320.append("</resources>");
        writeFile(sw320File, sw320.toString());
    }

    /**
     * dimen转换
     */
    private static void genDimen() {
        //以此文件夹下的dimens.xml文件内容为初始值参照
        File file = new File("./library/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw240 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder w820 = new StringBuilder();
        StringBuilder w1080 = new StringBuilder();
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);

                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    //截取<dimen></dimen>标签内的内容，从>右括号开始，到左括号减2，取得配置的数字
                    Double num = Double.parseDouble
                            (tempString.substring(tempString.indexOf(">") + 1,
                                    tempString.indexOf("</dimen>") - 2));
                    //根据不同的尺寸，计算新的值，拼接新的字符串，并且结尾处换行。
                    sw240.append(start).append(String.format("%.2f",num * 0.75)).append(end).append("\r\n");
                    sw480.append(start).append(String.format("%.2f",num * 1.5)).append(end).append("\r\n");
                    sw600.append(start).append(String.format("%.2f",num * 1.87)).append(end).append("\r\n");
                    sw720.append(start).append(String.format("%.2f",num * 2.25)).append(end).append("\r\n");
                    sw800.append(start).append(String.format("%.2f",num * 2.5)).append(end).append("\r\n");
                    w820.append(start).append(String.format("%.2f",num * 2.56)).append(end).append("\r\n");
                    w1080.append(start).append(String.format("%.2f",num * 3.37)).append(end).append("\r\n");
                } else {
                    sw240.append(tempString).append("");
                    sw480.append(tempString).append("");
                    sw600.append(tempString).append("");
                    sw720.append(tempString).append("");
                    sw800.append(tempString).append("");
                    w820.append(tempString).append("");
                    w1080.append(tempString).append("");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  sw240 -->");
            System.out.println(sw240);
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);
            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);
            System.out.println("<!--  sw820 -->");
            System.out.println(w820);
            System.out.println("<!--  sw1080 -->");
            System.out.println(w1080);
            String sw240file = "./library/src/main/res/values-sw240dp/dimens.xml";
            String sw480file = "./library/src/main/res/values-sw480dp/dimens.xml";
            String sw600file = "./library/src/main/res/values-sw600dp/dimens.xml";
            String sw720file = "./library/src/main/res/values-sw720dp/dimens.xml";
            String sw800file = "./library/src/main/res/values-sw800dp/dimens.xml";
            String w820file = "./library/src/main/res/values-sw820dp/dimens.xml";
            String w1080file = "./library/src/main/res/values-sw1080dp/dimens.xml";
            //将新的内容，写入到指定的文件中去
            writeFile(sw240file, sw240.toString());
            writeFile(sw480file, sw480.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw800file, sw800.toString());
            writeFile(w820file, w820.toString());
            writeFile(w1080file, w1080.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入方法
     *
     * @param file
     * @param text
     */
    public static void writeFile(String file, String text) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.close();
    }
}
