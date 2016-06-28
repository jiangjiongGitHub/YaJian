/**
 * 系统测试
 */
package FileDB;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统说明 : 1.首先系统加载 2.然后执行增删改查(员工编号唯一) 3.系统退出
 * 
 * @author student
 * 
 */
public class Test {
	static Scanner read = new Scanner(System.in);
	static FileOp fileOp = FileOp.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public static void main(String[] args) {
		// 设置文件
		fileOp.setFileName("employeeDB");
		// 初始化
		fileOp.initial();
		System.out.println("系统正在初始化....");
		while (true) {
			opration();
		}
	}

	private static void addInfo() {
		int flag = 0;
		do {
			flag = 0;
			System.out.print("\n请输入员工编号：");
			String employeeId = read.next();
			System.out.print("\n请输入员工姓名：");
			String employeeName = read.next();
			System.out.print("\n请输入员工性别：");
			String sex = read.next();
			System.out.print("\n请输入员工年龄：");
			int age = Integer.parseInt(read.next());
			System.out.print("\n请输入公司名称：");
			String company = read.next();
			System.out.print("\n请输入受雇佣的时间：");
			String time = read.next();
			Employee emp = new Employee(employeeId, employeeName, sex, age,
					company, time);
			if (fileOp.addInfo(emp))
				System.err.println("员工" + employeeName + "添加成功！");
			else {
				flag = 1;
			}
		} while (flag == 1);
	}

	private static void deleleInfo() {
		System.out.println("请输入您要删除的员工的编号：");
		String id = read.next();
		if (fileOp.deleteInfo(id))
			System.err.println("编号为" + id + "的员工删除成功！");
	}

	private static void updateInfo() {
		System.out.println("请输入需要修改信息的员工的编号:");
		String employeeId = read.next();
		System.out.print("\n请输入员工姓名：");
		String employeeName = read.next();
		System.out.print("\n请输入员工性别：");
		String sex = read.next();
		System.out.print("\n请输入员工年龄：");
		int age = Integer.parseInt(read.next());
		System.out.print("\n请输入公司名称：");
		String company = read.next();
		System.out.print("\n请输入受雇佣的时间：");
		String time = read.next();
		Employee emp = new Employee(employeeId, employeeName, sex, age,
				company, time);
		if (fileOp.updateInfo(emp))
			System.err.println("编号为" + employeeId + "的员工的信息修改成功！");
	}

	private static void queryInfo() {
		System.out.println("请输入您要查找的员工的编号：");
		String id = read.next();
		if (fileOp.queryInfo(id))
			System.err.println("查询成功！");
	}

	public static void opration() {
		int choice = 0;
		String errors = null;
		System.out.println("*   1.查看员工信息                    *");
		System.out.println("*   2.添加员工信息                    *");
		System.out.println("*   3.删除员工信息                    *");
		System.out.println("*   4.修改员工信息                    *");
		System.out.println("*   5.查看所有信息                    *");
		System.out.println("*   6.退出员工系统                    *");

		do {
			try {
				choice = Integer.parseInt(read.next());
				errors = null;
			} catch (NumberFormatException e) {
				System.out.println("输入有误！！");
				errors = "error";
				System.out.println("请重新输入:");
			}
		} while (errors != null);
		switch (choice) {
		case 1:
			queryInfo();
			break;
		case 2:
			addInfo();
			break;
		case 3:
			deleleInfo();
			break;
		case 4:
			updateInfo();
			break;
		case 5:
			fileOp.queryAll();
			break;
		case 6:
			System.out.println("欢迎再次使用...");
			System.exit(0); // 正常退出
			break;
		default:
			System.out.println("输入无效!\n系统自动退出...");
			break;
		}
	}
}
