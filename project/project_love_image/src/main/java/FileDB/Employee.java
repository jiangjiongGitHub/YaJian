/**
 * 员工实体类
 */
package FileDB;

public class Employee {
	private String employeeId;			//员工编号

	private String employeeName;		//员工姓名

	private String sex;					//员工性别
	
	private int age;					//员工年龄

	private String company;				//公司名称
	
	private String time;				//受雇佣的时间

	public Employee(String employeeId, String employeeName, String sex,
			int age, String company, String time) {
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.sex = sex;
		this.age = age;
		this.company = company;
		this.time = time;
	}

	public Employee() {
	}

	//getter 和 setter
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
