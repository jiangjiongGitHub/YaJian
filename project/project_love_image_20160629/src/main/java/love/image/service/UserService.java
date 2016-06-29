package love.image.service;

import love.image.model.t_user;

public interface UserService {
	int deleteByPrimaryKey(Integer id);

	int insert(t_user record);

	int insertSelective(t_user record);

	int updateByPrimaryKey(t_user record);

	int updateByPrimaryKeySelective(t_user record);

	t_user selectByPrimaryKey(Integer id);

	int selectMaxUser();
}
