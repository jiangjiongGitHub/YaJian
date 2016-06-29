package love.image.dao;

import love.image.model.t_user;

public interface t_userMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(t_user record);

	int insertSelective(t_user record);

	int updateByPrimaryKey(t_user record);

	int updateByPrimaryKeySelective(t_user record);

	t_user selectByPrimaryKey(Integer id);

	int selectMaxUser();
}