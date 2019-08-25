package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService=new RouteServiceImpl();

    private FavoriteService favoriteService=new FavoriteServiceImpl();

    /**
     *  分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");


        //接受rname线路名称
        String rname =request.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"),"utf-8");


        int cid=0;
        //处理参数
        if (cidStr!=null&&cidStr.length()>0&& "null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }

        int currentPage=0; //如果不传，则默认为第一页
        if (currentPageStr!=null&&currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }else{
            currentPage=1;
        }

        int pageSize=0;//如果不传，则默认为5条
        if (pageSizeStr!=null&&pageSizeStr.length()>0){
            pageSize = Integer.parseInt(pageSizeStr);
        }else{
            pageSize=5;
        }


        //调用service查询PageBean对象
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize,rname);

        //将PageBean对象序列化为json，返回给前端
        writeValue(pageBean,response);
    }


    /**
     *  根据id查询一个旅游线路得详细信息
     * @param request
     * @param response
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收rid
        String rid = request.getParameter("rid");
        //调用service
        Route route = routeService.findOne(rid);

        writeValue(route,response);
    }

    /**
     * 判断当前登陆得用户是否收藏过该线路
     * @param request
     * @param response
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //获取rid
        String rid = request.getParameter("rid");
        //获取当前登录得用户得session
        User user = (User) request.getSession().getAttribute("user");
        int uid; //用户id
        if (user==null){
            //用户尚未登录
            return;
        }else{
            //用户已经登录
            uid=user.getUid();
        }
        //调用service
        boolean flag = favoriteService.isFavorite(rid, uid);

        //将flag写回客户端
        writeValue(flag,response);

    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取rid
        String rid = request.getParameter("rid");
        //获取当前登录得用户
        User user = (User) request.getSession().getAttribute("user");
        int uid; //用户id
        if (user==null){
            //用户尚未登录
            return;
        }else{
            //用户已经登录
            uid=user.getUid();
        }
        //调用service添加
        favoriteService.add(rid,uid);
    }

}
