/**
 * 员工文件的操作
 */
package FileDB;

import java.util.*;
import java.io.*;

public class FileOp {
	private String fileName;

	private FileWriter fw = null;

	private BufferedWriter bw = null;

	private FileReader fr = null;

	private BufferedReader br = null;

	private ArrayList<Employee> listInfo = new ArrayList<Employee>();

	String[] header = { "员工编号", "员工姓名", "员工性别", "员工年龄", "公司名称", "雇佣时间" };

	public static FileOp fileOp = new FileOp();

	// 返回文件操作对象
	public static FileOp getInstance() {
		return fileOp;
	}

	private FileOp() {
	}

	// 文件名的设置
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// **************************文件的具体操作****************************
	// 获得输出流
	public BufferedWriter getOutPutStream() {
		if (checkFile()) {
			try {
				fw = new FileWriter(fileName, true);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bw;
	}

	// 关闭输出流
	public void closeOutPutStream() {
		try {
			fw.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获得输入流
	public BufferedReader getInPutStream() {
		if (checkFile()) {
			try {
				fr = new FileReader(fileName);
				br = new BufferedReader(fr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return br;
		// BufferedReader br = new BufferedReader();
	}

	// 关闭输出流
	public void closeInPutStream() {
		try {
			fr.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 加载所有信息
	public boolean initial() {
		boolean flag = true;
		// 首先清空集合
		if (listInfo.size() != 0) {
			listInfo.clear();
		}
		BufferedReader br = getInPutStream();
		try {
			br.readLine();
			String strInfo = br.readLine();
			while (strInfo != null && !"".equals(strInfo)) {
				String[] infos = strInfo.split("------");
				Employee emp = new Employee();
				emp.setEmployeeId(infos[0]);
				emp.setEmployeeName(infos[1]);
				emp.setSex(infos[2]);
				emp.setAge(Integer.parseInt(infos[3]));
				emp.setCompany(infos[4]);
				emp.setTime(infos[5]);
				listInfo.add(emp);
				strInfo = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		} finally {
			closeInPutStream();
		}
		// while
		return flag;
	}

	/**
	 * 添加一条员工信息
	 * 
	 * @param employee
	 * @return
	 */
	public boolean addInfo(Employee employee) {
		boolean flag = false; // 添加失败标志变量
		Employee emp = employee;
		for (int i = 0; i < listInfo.size(); i++) {
			if (emp.getEmployeeId().equals(listInfo.get(i).getEmployeeId())) {
				System.out.println("对不起!该编号的员工已经存在，请重新输入:");
				return flag;
			}
		}
		System.out.println("存在！");
		BufferedWriter bw = getOutPutStream(); // 获得输出流
		try {
			bw.newLine();
			bw.write(emp.getEmployeeId() + "------" + emp.getEmployeeName() + "------"
					+ emp.getSex() + "------" + emp.getAge() + "------"
					+ emp.getCompany() + "------" + emp.getTime());
			bw.flush(); // 清空缓冲
			flag = true; // 添加记录成功
			// 加入集合类中
			listInfo.add(employee);
			if (flag) {
				System.out.println("添加员工成功！");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeOutPutStream(); // 关闭流
		}
		
		return flag;
	}

	public boolean queryAll() {
		boolean flag = false; // 无指定员工信息标志变量
		for (int i = 0; i < listInfo.size(); i++) {
			// 在控制台显示
			System.out.println("员工详细信息如下:");
			System.out.println(header[0] + ":  " + listInfo.get(i).getEmployeeId() + "------" 
					+ header[1] + ":  " + listInfo.get(i).getEmployeeName() + "------"
					+ header[2] + ":  " + listInfo.get(i).getSex() + "------"
					+ header[3] + ":  " + listInfo.get(i).getAge() + "------"
					+ header[4] + ":  " + listInfo.get(i).getCompany() + "------"
					+ header[5] + ":  " + listInfo.get(i).getTime());
			System.out.println("\n*****************");
		}
		flag = true;
		System.out.println("查询成功！");
		return flag;
	}

	/**
	 * 根据员工编号查询指定员工信息
	 * 
	 * @param employeeId
	 * @return
	 */
	public boolean queryInfo(String employeeId) {
		boolean flag = false; // 无指定员工信息标志变量
		for (int i = 0; i < listInfo.size(); i++) {
			if (employeeId.equals(listInfo.get(i).getEmployeeId())) {
				System.out.println(listInfo.get(i).getEmployeeId() + "------"
						+ listInfo.get(i).getEmployeeName() + "------"
						+ listInfo.get(i).getSex() + "------"
						+ listInfo.get(i).getAge() + "------"
						+ listInfo.get(i).getCompany() + "------"
						+ listInfo.get(i).getTime());
				flag = true; // 查询成功
				break;
			}
		}
		System.out.println("查询成功！");
		return flag;

	}

	/**
	 * 根据员工编号执行删除操作
	 * 
	 * @param employeeId
	 * @return
	 */
	public boolean deleteInfo(String employeeId) {
		boolean flag = false;
		for (int i = 0; i < listInfo.size(); i++) {
			if (employeeId.equals(listInfo.get(i).getEmployeeId())) {
				listInfo.remove(i); // 从集合中移出
				System.out.println(" 删除员工");
				// 更新文件
				if (updateFile()) {
					flag = true; // 删除成功
				}
				break;
			}
		}
		return flag;
	}

	/**
	 * 更新指定的员工
	 * 
	 * @param emp
	 * @return
	 */
	public boolean updateInfo(Employee emp) {
		boolean flag = false;
		for (int i = 0; i < listInfo.size(); i++) {
			if (emp.getEmployeeId().equals(listInfo.get(i).getEmployeeId())) {
				listInfo.set(i, emp); // 更新集合
				// 更新文件
				if (updateFile()) {
					flag = true; // 更新成功
				}
				break;
			}
		}
		System.out.println("更新员工！");
		return flag;
	}

	/**
	 * 更新文件
	 * 
	 * @return
	 */
	private boolean updateFile() {
		boolean flag = false;
		// 重写文件-->覆盖文件
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);
			// **********************************************************************************
			bufferedWriter.write(header[0] + "------" + header[1] + "------"
					+ header[2] + "------" + header[3] + "------" + header[4] + "------"
					+ header[5]);
			bufferedWriter.newLine();
			for (int j = 0; j < listInfo.size(); j++) {

				bufferedWriter.write(listInfo.get(j).getEmployeeId() + "------"
						+ listInfo.get(j).getEmployeeName() + "------"
						+ listInfo.get(j).getSex() + "------"
						+ listInfo.get(j).getAge() + "------"
						+ listInfo.get(j).getCompany() + "------"
						+ listInfo.get(j).getTime());
				bufferedWriter.newLine();
			}
			bufferedWriter.flush(); // 清空缓冲
			flag = true; // 删除成功
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				bufferedWriter.close();
				fileWriter.close(); // 关闭流操作
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("更新文件");
		return flag;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @return
	 */
	public boolean checkFile() {
		File file = new File(fileName);
		boolean flag = false;
		// 如果指定的文件不存在,则创建该文件
		if (!file.exists()) {
			try {
				flag = file.createNewFile();
				if (flag) {
					System.out.println(fileName + "文件创建成功!");
				}
				fw = new FileWriter(fileName);
				bw = new BufferedWriter(fw);
				bw.write(
						header[0] + "------" + header[1] + "------" + header[2] + "------"
								+ header[3] + "------" + header[4] + "------"
								+ header[5]);
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				closeOutPutStream();
			}
		} else {
			flag = true;
		}
		
		return flag;
	}
}
