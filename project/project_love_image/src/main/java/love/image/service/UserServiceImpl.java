package love.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import love.image.dao.t_userMapper;
import love.image.model.t_user;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private t_userMapper userMapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(t_user record) {
		return userMapper.insert(record);
	}

	@Override
	public int insertSelective(t_user record) {
		return userMapper.insertSelective(record);
	}

	@Override
	public int updateByPrimaryKey(t_user record) {
		return userMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateByPrimaryKeySelective(t_user record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public t_user selectByPrimaryKey(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int selectMaxUser() {
		return userMapper.selectMaxUser();
	}

}
