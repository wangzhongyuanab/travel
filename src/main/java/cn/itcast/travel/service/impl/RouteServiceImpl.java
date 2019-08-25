package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgImpl;
import cn.itcast.travel.dao.impl.SellerImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {

    private RouteDao routeDao=new RouteDaoImpl();

    private RouteImgDao routeImgDao=new RouteImgImpl();

    private SellerDao sellerDao=new SellerImpl();

    private FavoriteDao favoriteDao=new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize,String rname) {
        //封装pageBean
        PageBean<Route> pb=new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页条数
        pb.setPageSize(pageSize);
        //设置总记录数
        int totalCount=routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start=(currentPage-1)*pageSize;//开始的记录数
        List<Route> list=routeDao.findByPage(cid,start,pageSize,rname);
        pb.setList(list);
        //设置总页数
        int totalPage=totalCount%pageSize==0?totalCount/pageSize:(totalCount/pageSize)+1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        Route route = routeDao.findOne(Integer.parseInt(rid));

        List<RouteImg> imgs = routeImgDao.findByRid(Integer.parseInt(rid));

        //将集合设置到route对象中
        route.setRouteImgList(imgs);

        //根据sid去查询卖家得信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //查询收藏次数
        int count=favoriteDao.findCountByRid(route.getRid());

        route.setCount(count);

        return route;
    }
}
