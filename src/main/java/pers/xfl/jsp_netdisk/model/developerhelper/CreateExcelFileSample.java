package pers.xfl.jsp_netdisk.model.developerhelper;

/**
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;




/**
 * @author bpan
 * source code URL: https://www.cnblogs.com/revel171226/p/8488816.html
 * created 2018年2月27日
 */
public class CreateExcelFileSample {

    private static HSSFWorkbook hWorkbook = null;
    private static final XSSFWorkbook xWorkbook = null;

    /**
     * 判断文件是否存在.
     * @param fileDir  文件路径
     * @return boolean
     */
    public static boolean fileExist(String fileDir){
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }

    /**
     * 判断文件的sheet是否存在.
     * @param fileDir   文件路径
     * @param sheetName  表格索引名
     * @return boolean
     */
    public static boolean XlsSheetExist(String fileDir, String sheetName){

        boolean flag = false;
        File file = new File(fileDir);

        if (file.exists()) {
            //文件存在，创建workbook
            try {
                hWorkbook = new HSSFWorkbook(new FileInputStream(file));

                HSSFSheet sheet = hWorkbook.getSheet(sheetName);
                if (sheet!=null) {
                    //文件存在，sheet存在

                    flag = true;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            //文件不存在
            flag = false;
        }
        return flag;
    }

    /**
     * 创建新excel(xls).
     * @param fileDir excel的路径
     * @param sheetNames 要创建的表格索引列表
     * @param titleRow  excel的第一行即表格头
     */
    public static void createExcelXls(String fileDir, List<String> sheetNames, String[] titleRow){

        //创建workbook
        hWorkbook = new HSSFWorkbook();
        //新建文件
        FileOutputStream fileOutputStream = null;
        HSSFRow row = null;
        try {

            CellStyle cellStyle = hWorkbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);

            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
            for(int i = 0; i<sheetNames.size(); i++){
                hWorkbook.createSheet(sheetNames.get(i));
                hWorkbook.getSheet(sheetNames.get(i)).createRow(0);
                //添加表头, 创建第一行
                row = hWorkbook.getSheet(sheetNames.get(i)).createRow(0);
//                row.setHeight((short)(20*20));
                for (short j = 0; j < titleRow.length; j++) {

                    HSSFCell cell = row.createCell(j, CellType.BLANK);
                    cell.setCellValue(titleRow[j]);
                    cell.setCellStyle(cellStyle);
                }
                fileOutputStream = new FileOutputStream(fileDir);
                hWorkbook.write(fileOutputStream);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {

            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件.
     * @param fileDir  文件路径
     * @return 如果文件不存在返回false, 如果文件存在删除成功之后返回true
     */
    public static boolean deleteExcel(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 往excel(xls)中写入(已存在的数据无法写入).
     * @param fileDir    文件路径
     * @param sheetName  表格索引
     * @param mapList
     * @throws Exception
     */

    public static void writeToExcelXls(String fileDir, String sheetName, List<Map<String,String>> mapList) throws Exception{

        //创建workbook
        File file = new File(fileDir);

        try {
            hWorkbook = new HSSFWorkbook(new FileInputStream(file));

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //文件流
        FileOutputStream fileOutputStream = null;
        HSSFSheet sheet = hWorkbook.getSheet(sheetName);
        // 获取表格的总行数
        // int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        //获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();

        try {
            // 获得表头行对象
            HSSFRow titleRow = sheet.getRow(0);
            //创建单元格显示样式
            CellStyle cellStyle = hWorkbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);


            if(titleRow!=null){
                for(int rowId = 0; rowId < mapList.size(); rowId++){
                    Map<String,String> map = mapList.get(rowId);
                    HSSFRow newRow=sheet.createRow(rowId+1);
//                    newRow.setHeight((short)(20*20));//设置行高  基数为20

                    for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {  //遍历表头
                        //trim()的方法是删除字符串中首尾的空格
                        String mapKey = titleRow.getCell(columnIndex).toString().trim();
                        HSSFCell cell = newRow.createCell(columnIndex);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(map.get(mapKey)==null ? null : map.get(mapKey));
                    }
                }
            }

            fileOutputStream = new FileOutputStream(fileDir);
            hWorkbook.write(fileOutputStream);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建Excel(xlsx)
     * @param fileDir  文件名称及地址
     * @param sheetName sheet的名称
     * @param titleRow  表头
     */
    public static void createExcelXlsx(String fileDir, String sheetName, String[] titleRow){



    }



    public static void main(String[] args) {

        String fileDir = "R:\\workbook.xls";

        List<String> sheetName = new ArrayList<>();

        sheetName.add("A");
        sheetName.add("B");
        sheetName.add("C");

        System.out.println(sheetName);

        String[] title = {"id","name","password"};
        CreateExcelFileSample.createExcelXls(fileDir, sheetName, title);

        List<Map<String,String>> userList1 = new ArrayList<Map<String,String>>();
        Map<String,String> map=new HashMap<String,String>();
        map.put("id", "111");
        map.put("name", "张三");
        map.put("password", "111！@#");

        Map<String,String> map2=new HashMap<String,String>();
        map2.put("id", "222");
        map2.put("name", "李四");
        map2.put("password", "222！@#");

        Map<String,String> map3=new HashMap<String,String>();
        map3.put("id", "33");
        map3.put("name", "王五");
        map3.put("password", "333！@#");
        userList1.add(map);
        userList1.add(map2);
        userList1.add(map3);

        Map<String, List<Map<String, String>>> users = new HashMap<>();

        users.put("A", userList1);

        List<Map<String,String>> userList2 = new ArrayList<Map<String,String>>();
        Map<String,String> map4=new HashMap<String,String>();
        map4.put("id", "111");
        map4.put("name", "张三");
        map4.put("password", "111！@#");

        Map<String,String> map5=new HashMap<String,String>();
        map5.put("id", "222");
        map5.put("name", "李四");
        map5.put("password", "222！@#");

        Map<String,String> map6=new HashMap<String,String>();
        map6.put("id", "33");
        map6.put("name", "王五");
        map6.put("password", "333！@#");
        userList2.add(map4);
        userList2.add(map5);
        userList2.add(map6);

        users.put("B", userList2);

        List<Map<String,String>> userList3 = new ArrayList<Map<String,String>>();


        users.put("C", userList3);

        System.out.println(sheetName.size());

        //删除List 集合中特定的元素
        for(Iterator<String> sheetNameIterator = sheetName.iterator(); sheetNameIterator.hasNext(); ){

            String sheet = sheetNameIterator.next();

            if ( users.get(sheet).size() == 0) {

                sheetNameIterator.remove();

            }
        }

        System.out.println(sheetName.size());

        createExcelXls(fileDir, sheetName, title);
        for (int j = 0; j < sheetName.size(); j++) {

            try {
                writeToExcelXls(fileDir, sheetName.get(j), users.get(sheetName.get(j)));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }
}
