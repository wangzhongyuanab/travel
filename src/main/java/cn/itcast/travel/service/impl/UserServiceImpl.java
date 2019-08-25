package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao=new UserDaoImpl();
    /**
     *  注册用户
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //先查询用户是否存在
        User user1 = userDao.findByUserName(user.getUsername());
        if (user1!=null){
            //用户存在，注册失败
            return false;
        }
        //设置激活码，唯一字符串
        user.setCode(UuidUtil.getUuid());

        //设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //激活邮件发送，邮件正文
        String content="<a href='http://localhost:8023/travel/user/active?code="+user.getCode()+"'>点击激活[旅游网]</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 激活用户
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        //根据激活码查询激活码状态
        User user=userDao.findByUserCode(code);
        if (user!=null){
            userDao.updateStatus(user);
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        return userDao.findByUserNameAndPassword(user.getUsername(),user.getPassword());
    }
}
