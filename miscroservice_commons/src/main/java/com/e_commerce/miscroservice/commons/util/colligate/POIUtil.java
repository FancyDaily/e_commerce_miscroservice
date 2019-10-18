package com.e_commerce.miscroservice.commons.util.colligate;

import java.io.*;
import java.util.*;

import com.beust.jcommander.Strings;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 功能描述:excel读写工具类
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年1月23日 下午5:53:21
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class POIUtil {
	private static Logger logger = Logger.getLogger(POIUtil.class);
	private final static String xls = "xls";
	private final static String xlsx = "xlsx";

	/**
	 * 根据路径读取excel文件
	 */
	public static List<String[]> readFromPath(String path) throws IOException, InvalidFormatException {
		InputStream inputStream = new FileInputStream(new File(path));
    	return readExcel(inputStream);
	}

	public static List<String[]> readExcel(InputStream inputStream) throws IOException, InvalidFormatException {
    	/*//检查文件
        checkFile(file);  */
		//获得Workbook工作薄对象
		Workbook workbook = WorkbookFactory.create(inputStream);
//		Workbook workbook = new XSSFWorkbook(inputStream);
		//创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
		List<String[]> list = new ArrayList<String[]>();
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum = sheet.getFirstRowNum();
				//获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				//循环除了第一行的所有行
//				for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
				for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
					//获得当前行
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					//获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					//获得符合标题行的列数
					int lastCellNum = sheet.getRow(firstRowNum).getPhysicalNumberOfCells();
					String[] cells = new String[lastCellNum];
					//循环当前行
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
						Cell cell = row.getCell(cellNum);
						cells[cellNum] = getCellValue(cell);
					}
					list.add(cells);
				}
			}
			workbook.close();
		}
		return list;
	}


	/**
	 * 读入excel文件，解析后返回
	 *
	 * @param file
	 * @throws IOException
	 */
	public static List<String[]> readExcel(MultipartFile file) throws IOException {
		//检查文件
		checkFile(file);
		//获得Workbook工作薄对象
		Workbook workbook = getWorkBook(file);
		//创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
		List<String[]> list = new ArrayList<String[]>();
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				//获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}
				//获得当前sheet的开始行
				int firstRowNum = sheet.getFirstRowNum();
				//获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				//循环除了第一行的所有行
				for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
					//获得当前行
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					//获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					//获得符合标题行的列数
					int lastCellNum = sheet.getRow(firstRowNum).getPhysicalNumberOfCells();
					String[] cells = new String[lastCellNum];
					//循环当前行
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
						Cell cell = row.getCell(cellNum);
						cells[cellNum] = getCellValue(cell);
					}
					list.add(cells);
				}
			}
			workbook.close();
		}
		return list;
	}

	public static void checkFile(MultipartFile file) throws IOException {
		//判断文件是否存在
		if (null == file) {
			logger.error("文件不存在！");
			throw new FileNotFoundException("文件不存在！");
		}
		//获得文件名
		String fileName = file.getOriginalFilename();
		//判断文件是否是excel文件
		if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
			logger.error(fileName + "不是excel文件");
			throw new IOException("只能提交excel文件！");
			//throw new IOException(fileName + "不是excel文件");
		}
	}

	public static Workbook getWorkBook(MultipartFile file) {
		//获得文件名
		String fileName = file.getOriginalFilename();
		//创建Workbook工作薄对象，表示整个excel
		Workbook workbook = null;
		try {
			//获取excel文件的io流
			InputStream is = file.getInputStream();
			//根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
			if (fileName.endsWith(xls)) {
				//2003
				workbook = new HSSFWorkbook(is);
			} else if (fileName.endsWith(xlsx)) {
				//2007
				workbook = new XSSFWorkbook(is);
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
		return workbook;
	}

	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		//把数字当成String来读，避免出现1读成1.0的情况
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		//判断数据的类型
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: //数字
				cellValue = String.valueOf(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING: //字符串
				cellValue = String.valueOf(cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN: //Boolean
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA: //公式
				cellValue = String.valueOf(cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK: //空值
				cellValue = "";
				break;
			case Cell.CELL_TYPE_ERROR: //故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
		}
		return cellValue;
	}

	/*public static void demo() {
		try {
			String pathname = "/Users/xufangyi/Downloads/title_demo.xlsx";
			File file = new File(pathname);
			InputStream in = new FileInputStream(file);
			//得到整个excel对象
			XSSFWorkbook excel = new XSSFWorkbook(in);
			//获取整个excel有多少个sheet
			int sheets = excel.getNumberOfSheets();
			//便利第一个sheet
			Map<String, String> colMap = new HashMap<String, String>();
			for (int i = 0; i < sheets; i++) {
				XSSFSheet sheet = excel.getSheetAt(i);
				if (sheet == null) {
					continue;
				}
				int mergedRegions = sheet.getNumMergedRegions();
				XSSFRow row2 = sheet.getRow(0);
				Map<Integer, String> category = new HashMap<Integer, String>();
				for (int j = 0; j < mergedRegions; j++) {
					CellRangeAddress rangeAddress = sheet.getMergedRegion(j);
					int firstColumnIndex = rangeAddress.getFirstColumn();
					int lastColumnIndex = rangeAddress.getLastColumn();
					category.put(firstColumnIndex, lastColumnIndex + "-" + row2.getCell(firstColumnIndex).toString());
				}
				//便利每一行
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					System.out.println();
					XSSFRow row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					short lastCellNum = row.getLastCellNum();
					String cate = "";
					Integer maxIndex = 0;
					for (int col = row.getFirstCellNum(); col < lastCellNum; col++) {
						XSSFCell cell = row.getCell(col);
						if (skipEmptyCell(cell)) continue;
						int columnIndex = cell.getColumnIndex();
						String string = category.get(columnIndex);
						if (string != null && !string.equals("")) {
							String[] split = string.split("-");
							cate = split[1];
							maxIndex = Integer.parseInt(split[0]);
							System.out.println(cate + "<-->" + cell.toString());
						} else {
							//如果当前便利的列编号小于等于合并单元格的结束,说明分类还是上面的分类名称
							if (columnIndex <= maxIndex) {
								System.out.println(cate + "<-->" + cell.toString());
							} else {
								System.out.println("分类未知" + "<-->" + cell.toString());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public static List<Map<String, Object>> estateDemo(InputStream in) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
//			checkFile(multipartFile);
			/*String pathname = "/Users/xufangyi/Downloads/楼盘管理导入demo.xlsx";
			File file = new File(pathname);*/
//			InputStream in = new FileInputStream(file);
			//得到整个excel对象
			XSSFWorkbook excel = new XSSFWorkbook(in);
			//获取整个excel有多少个sheet
			int sheets = excel.getNumberOfSheets();
			//遍历每一个sheet
			for (int i = 0; i < sheets; i++) {
				XSSFSheet sheet = excel.getSheetAt(i);
				if (sheet == null) {
					continue;
				}
				int mergedRegions = sheet.getNumMergedRegions();
				XSSFRow rowFirst = sheet.getRow(0);
				//处理合并单元格(获取首行构筑标题cate(楼号)、获取非首行构建单元cate)
				Map<Integer, String> titleCategory = new HashMap<>();
				Map<Integer, String> groupCategory = new HashMap<>();
				for (int j = 0; j < mergedRegions; j++) {        //遍历所有合并单元格
					CellRangeAddress rangeAddress = sheet.getMergedRegion(j);
					int firstColumn = rangeAddress.getFirstColumn();
					int lastColumn = rangeAddress.getLastColumn();
					int firstRow = rangeAddress.getFirstRow();    //起始行索引
					rowFirst = sheet.getRow(firstRow);
					if (firstRow == 0) {    //标题行
						titleCategory.put(firstColumn, lastColumn + "-" + rowFirst.getCell(firstColumn).toString());
					} else {    //"单元"行
						groupCategory.put(firstColumn, lastColumn + "-" + rowFirst.getCell(firstColumn).toString());
					}
				}
				//处理面积信息
				XSSFRow rowSecond = sheet.getRow(1);
				HashMap<Integer, String> areaCategory = new HashMap<>();
				for (int col = 1; col < rowSecond.getLastCellNum(); col++) {
					XSSFCell cell = rowSecond.getCell(col);
					if (skipEmptyCell(cell)) continue;
					int columnIndex = cell.getColumnIndex();
					areaCategory.put(columnIndex, cell.toString());    //装载列索引，面积
				}
				//遍历每一行
				for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
					XSSFRow row = sheet.getRow(rowNum);
					//如果最后一行以空行开头，则忽略
					int firstCellNum = row.getFirstCellNum();
					XSSFCell firstCell = row.getCell(firstCellNum);
					if (skipEmptyCell(firstCell)) continue;
					String floorNumString = firstCell.toString();    //当前行需要的楼层数

					//处理常规
					String s = titleCategory.get(firstCellNum);
					String[] ss = s.split("-");
					String buidingName = ss[1];
					Integer maxIndex = Integer.parseInt(ss[0]);

					String groupName = "";
					Integer maxIndex2 = 0;
					for (int col = firstCellNum + 1; col < row.getLastCellNum(); col++) {
						XSSFCell cell = row.getCell(col);
						if (skipEmptyCell(cell)) continue;
						int columnIndex = cell.getColumnIndex();
						//获取楼号，获取单元号，获取面积信息
						String string = titleCategory.get(columnIndex);
						Map<String, Object> res0 = getCate(col, string, buidingName, maxIndex);
						buidingName = (String) res0.get("cate");
						maxIndex = (Integer) res0.get("maxIndex");

						String groupString = groupCategory.get(columnIndex);
						Map<String, Object> res = getCate(columnIndex, groupString, groupName, maxIndex2);
						groupName = (String) res.get("cate");
						maxIndex2 = (Integer) res.get("maxIndex");

						String areString = areaCategory.get(columnIndex);

						System.out.println("楼号 = " + buidingName);
						System.out.println("单元 = " + groupName);

						System.out.println("area = " + areString);

						System.out.println("楼层号 = " + floorNumString);

						String houseNum = cell.toString();
						if(houseNum.contains(".")) {
							houseNum = houseNum.substring(0, houseNum.indexOf("."));
						}
						System.out.println("以上是当前信息。房号 = " + houseNum);

						System.out.println("<==================================>");

						Integer buildingNum = Integer.valueOf(buidingName.substring(0, buidingName.length() - 2));
						Integer floorNum = Integer.valueOf(floorNumString.substring(0, floorNumString.length() - 1));
						Double area = Double.valueOf(areString.substring(0, areString.length() - 1));
						HashMap<String, Object> resultMap = new HashMap<>();

						XSSFCellStyle cellStyle = cell.getCellStyle();
						XSSFColor fillBackgroundColorColor = cellStyle.getFillBackgroundColorColor();
						resultMap.put("haveColor", fillBackgroundColorColor != null);

						resultMap.put("buildingNum", buildingNum);
						resultMap.put("groupName", groupName);
						resultMap.put("floorNum", floorNum);
						resultMap.put("area", area);
						resultMap.put("houseNum", Integer.valueOf(houseNum));
						list.add(resultMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/*public static List<Map<String, Object>> housesDemo(InputStream in) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			//得到整个excel对象
			XSSFWorkbook excel = new XSSFWorkbook(in);
			//获取整个excel有多少个sheet
			int sheets = excel.getNumberOfSheets();
			//遍历每一个sheet
			for (int i = 0; i < sheets; i++) {
				XSSFSheet sheet = excel.getSheetAt(i);
				if (sheet == null) {
					continue;
				}
				int mergedRegions = sheet.getNumMergedRegions();
				XSSFRow rowFirst = sheet.getRow(0);
				//处理合并单元格(获取首行构筑标题cate(楼号)、获取非首行构建单元cate)
				Map<Integer, String> titleCategory = new HashMap<>();
				for (int j = 0; j < mergedRegions; j++) {        //遍历所有合并单元格
					CellRangeAddress rangeAddress = sheet.getMergedRegion(j);
					int firstColumn = rangeAddress.getFirstColumn();
					int lastColumn = rangeAddress.getLastColumn();
					int firstRow = rangeAddress.getFirstRow();    //起始行索引
					rowFirst = sheet.getRow(firstRow);
					if (firstRow == 0) {    //标题行
						titleCategory.put(firstColumn, lastColumn + "-" + rowFirst.getCell(firstColumn).toString());
					}
				}
				//处理表头信息
				XSSFRow rowSecond = sheet.getRow(1);
				HashMap<Integer, String> tableHeadCategory = new HashMap<>();
				for (int col = 1; col < rowSecond.getLastCellNum(); col++) {
					XSSFCell cell = rowSecond.getCell(col);
					if (skipEmptyCell(cell)) continue;
					int columnIndex = cell.getColumnIndex();
					tableHeadCategory.put(columnIndex, cell.toString());    //装载列索引，面积
				}
				//遍历每一行
				for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
					XSSFRow row = sheet.getRow(rowNum);
					//如果最后一行以空行开头，则忽略
					int firstCellNum = row.getFirstCellNum();
					XSSFCell firstCell = row.getCell(firstCellNum);
					if (skipEmptyCell(firstCell)) continue;
					String floorNumString = firstCell.toString();    //当前行需要的楼层数

					//处理常规
					String s = titleCategory.get(firstCellNum);
					String[] ss = s.split("-");
					String buidingName = ss[1];
					Integer maxIndex = Integer.parseInt(ss[0]);

					String groupName = "";
					Integer maxIndex2 = 0;
					for (int col = firstCellNum + 1; col < row.getLastCellNum(); col++) {
						XSSFCell cell = row.getCell(col);
						if (skipEmptyCell(cell)) continue;
						int columnIndex = cell.getColumnIndex();
						//获取楼号，获取表头
						String string = titleCategory.get(columnIndex);
						Map<String, Object> res0 = getCate(col, string, buidingName, maxIndex);
						buidingName = (String) res0.get("cate");
						maxIndex = (Integer) res0.get("maxIndex");

						String headString = tableHeadCategory.get(columnIndex);

						System.out.println("楼号 = " + buidingName);
						System.out.println("单元 = " + groupName);

						System.out.println("area = " + headString);

						System.out.println("楼层号 = " + floorNumString);

						String houseNum = cell.toString();
						if(houseNum.contains(".")) {
							houseNum = houseNum.substring(0, houseNum.indexOf("."));
						}
						System.out.println("以上是当前信息。房号 = " + houseNum);

						System.out.println("<==================================>");

						Integer buildingNum = Integer.valueOf(buidingName.substring(0, buidingName.length() - 2));
						Integer floorNum = Integer.valueOf(floorNumString.substring(0, floorNumString.length() - 1));
						Double area = Double.valueOf(headString.substring(0, headString.length() - 1));
						HashMap<String, Object> resultMap = new HashMap<>();

						XSSFCellStyle cellStyle = cell.getCellStyle();
						XSSFColor fillBackgroundColorColor = cellStyle.getFillBackgroundColorColor();
						resultMap.put("haveColor", fillBackgroundColorColor != null);

						resultMap.put("buildingNum", buildingNum);
						resultMap.put("groupName", groupName);
						resultMap.put("floorNum", floorNum);
						resultMap.put("area", area);
						resultMap.put("houseNum", Integer.valueOf(houseNum));
						list.add(resultMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}*/

	/*
	 * 导出数据
	 */
	public static void export(String title, List<String> rowName, List<Object[]> dataList, OutputStream out) throws Exception {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
			HSSFSheet sheet = !StringUtil.isEmpty(title)? workbook.createSheet(title) : workbook.createSheet(); // 创建工作表
			// sheet样式定义;    getColumnTopStyle();    getStyle()均为自定义方法 --在下面,可扩展
//			HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
//			HSSFCellStyle style = this.getStyle(workbook); // 获取单元格样式对象
			int startRow = 0;
			if(!StringUtil.isEmpty(title)) {
	//			cellTiltle.setCellStyle(columnTopStyle);    //设置标题行样式
				HSSFRow rowm = sheet.createRow(0);  // 产生表格标题行
				HSSFCell cellTiltle = rowm.createCell(0);   //创建表格标题列
				//合并表格标题行，合并列数为列名的长度,第一个0为起始行号，第二个1为终止行号，第三个0为起始列好，第四个参数为终止列号
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.size() - 1)));
				cellTiltle.setCellValue(title);     //设置标题行值
				startRow = 2;
			}
			int columnNum = rowName.size();     // 定义所需列数
			HSSFRow rowRowName = sheet.createRow(startRow); // 在索引2的位置创建行(最顶端的行开始的第二行)
			// 将列头设置到sheet的单元格中
			for (int n = 0; n < columnNum; n++) {
				HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
				cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
				HSSFRichTextString text = new HSSFRichTextString(rowName.get(n));
				cellRowName.setCellValue(text); // 设置列头单元格的值
//				cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
			}
			// 将查询出的数据设置到sheet对应的单元格中
			for (int i = 0; i < dataList.size(); i++) {
				Object[] obj = dataList.get(i);   // 遍历每个对象
				HSSFRow row = sheet.createRow(i + 1);   // 创建所需的行数
				for (int j = 0; j < obj.length; j++) {
					HSSFCell cell = null;   // 设置单元格的数据类型
					/*if (j == 0) {
						cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(i + 1);
					} else {*/
						cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
						if (!"".equals(obj[j]) && obj[j] != null) {
							cell.setCellValue(obj[j].toString()); // 设置单元格的值
						}
//					}
//					cell.setCellStyle(style); // 设置单元格样式
				}
			}

			/*// 让列宽随着导出的列长自动适应
			for (int colNum = 0; colNum < columnNum; colNum++) {
				int columnWidth = sheet.getColumnWidth(colNum) / 256;
				for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
					HSSFRow currentRow;
					// 当前行未被使用过
					if (sheet.getRow(rowNum) == null) {
						currentRow = sheet.createRow(rowNum);
					} else {
						currentRow = sheet.getRow(rowNum);
					}
					if (currentRow.getCell(colNum) != null) {
						HSSFCell currentCell = currentRow.getCell(colNum);
						if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							int length = currentCell.getStringCellValue()
								.getBytes().length;
							if (columnWidth < length) {
								columnWidth = length;
							}
						}
					}
				}
				if (colNum == 0) {
					sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
				} else {
					sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
				}
			}*/
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
		try {
			OutputStream outputStream = new FileOutputStream(new File("/Users/xufangyi/Downloads/newFile"));
			List<String> strings = Arrays.asList("年龄", "性别", "课程");
			List<Object[]> objects = Arrays.asList(new Object[]{"里斯", "王五", "赵六"}, new Object[]{"张三年", "大虫", "AA"});
			export(null, strings, objects, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	private static Map<String, Object> getCate(int columnIndex, String groupString, String cate, Integer maxIndex) {
		HashMap<String, Object> res = new HashMap<>();
		if (groupString != null && !groupString.equals("")) {
			String[] split = groupString.split("-");
			cate = split[1];
			maxIndex = Integer.parseInt(split[0]);
			res.put("cate", cate);
		} else {
			//如果当前便利的列编号小于等于合并单元格的结束,说明分类还是上面的分类名称
			if (columnIndex <= maxIndex) {
				res.put("cate", cate);
			} else {
				res.put("cate", "");
			}
		}
		res.put("maxIndex", maxIndex);
		return res;
	}

	public static void main(String[] args) {
		String path = "/Users/xufangyi/Downloads/导入数据10-18.17.56.xls";
		try {
			List<String[]> strings = readFromPath(path);
			strings.stream()
				.map(Arrays::asList).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	private static boolean skipEmptyCell(XSSFCell cell) {
		return cell == null || "".equals(cell.toString());
	}
}
