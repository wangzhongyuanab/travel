package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User findByUserName(String username);

    /**
     * 用户保存
     * @param user
     */
    public void save(User user);

    /**
     *  根据激活码
     * @param Code
     * @return
     */
    public User findByUserCode(String Code);

    void updateStatus(User user);

    User findByUserNameAndPassword(String username,String password);
}
