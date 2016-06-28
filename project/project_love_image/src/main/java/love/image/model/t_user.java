package love.image.model;

import java.io.Serializable;
import java.util.Date;

public class t_user implements Serializable {
	private Integer id;

	private Date add_time;

	private Integer add_status;

	private String user_name;

	private String user_name_nick;

	private String user_image;

	private String user_telephone;

	private String user_password;

	private Integer user_level;

	private Double user_money;

	private Integer login_count;

	private String login_ip;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Date add_time) {
		this.add_time = add_time;
	}

	public Integer getAdd_status() {
		return add_status;
	}

	public void setAdd_status(Integer add_status) {
		this.add_status = add_status;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_name_nick() {
		return user_name_nick;
	}

	public void setUser_name_nick(String user_name_nick) {
		this.user_name_nick = user_name_nick;
	}

	public String getUser_image() {
		return user_image;
	}

	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}

	public String getUser_telephone() {
		return user_telephone;
	}

	public void setUser_telephone(String user_telephone) {
		this.user_telephone = user_telephone;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}

	public Integer getUser_level() {
		return user_level;
	}

	public void setUser_level(Integer user_level) {
		this.user_level = user_level;
	}

	public Double getUser_money() {
		return user_money;
	}

	public void setUser_money(Double user_money) {
		this.user_money = user_money;
	}

	public Integer getLogin_count() {
		return login_count;
	}

	public void setLogin_count(Integer login_count) {
		this.login_count = login_count;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
}