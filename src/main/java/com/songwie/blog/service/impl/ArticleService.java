package com.songwie.blog.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.songwie.blog.base.util.PageUtil;
import com.songwie.blog.model.ArticleDao;


@Service("articleService")
@Scope("prototype")
@Transactional
public class ArticleService{

	@Autowired
	ArticleDao dao;

	//首页
	public List<Map<String, Object>> getArticles(String start,String limit,StringHolder total) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if(start==null||start.equals("")||start.equals("null")){
			start = "0";
		}
		if(limit==null||limit.equals("")||limit.equals("null")){
			limit = "2";
		}

		List data = dao.getArticleList(Integer.valueOf(start),Integer.valueOf(limit));
		List<Object> totalList = dao.getArticleTotal();

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);
			map.put("content", objects[5]==null?"":objects[5]);
			map.put("articleTypeName", objects[6]==null?"":objects[6]);
			map.put("articleDate", objects[3]==null?"":objects[3]);
			map.put("createUser", objects[4]==null?"":objects[4]);
			map.put("ncount", objects[7]==null?"":objects[7]);

			list.add(map);
		}

		total.value = totalList.get(0).toString();

		return list;
	}
	//最新文章列表
	public List<Map<String, Object>> getNewArticles() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List data = dao.getNewArticles(Integer.valueOf(10));

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);

			list.add(map);
		}

		return list;
	}

	//最新文章列表
	public List<Map<String, Object>> getNewArticleList(String start,String limit) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List data = dao.getNewArticles(Integer.valueOf(limit));

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);

			list.add(map);
		}

		return list;
	}

	public Map<String, Object> getArticle(String id) {
		List data = dao.getArticleById(id);
		dao.freshCount(id);

		Map<String,Object> map = new HashMap<String, Object>();
		Object[] objects = (Object[]) data.get(0);
		map.put("id", objects[0]==null?"":objects[0]);
		map.put("title", objects[1]==null?"":objects[1]);
		map.put("content", objects[5]==null?"":objects[5]);
		map.put("articleTypeName", objects[6]==null?"":objects[6]);
		map.put("articleDate", objects[3]==null?"":objects[3]);
		map.put("createUser", objects[4]==null?"":objects[4]);


		//上一篇下一篇
        //Integer page = Integer.valueOf(objects[0]==null?"1":objects[0].toString());
		//map.put("nextArticle", page+1);
		//map.put("nextArticle", page-1);


		return map;
	}

	public void saveRepy(String articleid,String replyid, String comment, String author, String contact) {
		String CODE = "1001";
		String code = CODE;
		String fullCode = CODE;
		Object maxCode = dao.getMaxCodeByArticleId(articleid);
		if(maxCode!=null){
			code = String.valueOf(Integer.valueOf(CODE) + Integer.valueOf(maxCode.toString()));
			fullCode = code;
		}
		String level = "1";
		if(replyid!=null&&!replyid.equals("0")){
           Object[] farentObj = dao.getReplyById(replyid);

           String farentFullCode = farentObj[1].toString();
           code = String.valueOf(Integer.valueOf(CODE) + Integer.valueOf(replyid));
           fullCode = farentFullCode + "-" + code;
           level = "2";
		}
		dao.saveRepy(articleid,replyid,code,fullCode,comment,author,contact ,level);

	}

	public List<Map<String, Object>> getReplys(String id) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List  data = dao.getReplys(id);

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("level", objects[2]==null?"":objects[2]);
			map.put("reply_author", objects[5]==null?"":objects[5]);
			map.put("replydate", objects[4]==null?"":objects[4]);
			map.put("articleid", objects[3]==null?"":objects[3]);
			map.put("replyMsg", objects[7]==null?"":objects[7]);
			map.put("responAuthor", objects[1]==null?"":objects[1]);

			list.add(map);
		}

		return list;
	}
	public List<Map<String, Object>> getNewReplyList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List  data = dao.getNewReplyList(Integer.valueOf(10));

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("reply_author", objects[1]==null?"":objects[1]);
			map.put("articleid", objects[2]==null?"":objects[2]);
			map.put("article", objects[3]==null?"":objects[3]);

			list.add(map);
		}

		return list;
	}
	public List<Map<String, Object>> getNewTimeList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List  data = dao.getNewTimeList(Integer.valueOf(20));

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("time", objects[0]==null?"":objects[0]);
			map.put("count", objects[1]==null?"":objects[1]);

			list.add(map);
		}

		return list;
	}
	public List<Map<String, Object>> getArticleTypeList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List  data = dao.getArticleTypeList();

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("type", objects[1]==null?"":objects[1]);

			list.add(map);
		}

		return list;
	}
	public List<Map<String, Object>> getFriendLinks() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List  data = dao.getFriendLinks();

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("name", objects[1]==null?"":objects[1]);
			map.put("link", objects[2]==null?"":objects[2]);

			list.add(map);
		}

		return list;
	}
	public List<Map<String, Object>> getAllArticleList(String start,String limit, String bymonth, String type,String search, PageUtil pageUtil) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(start==null||start.equals("")||start.equals("null")){
			start = "0";
		}
		if(limit==null||limit.equals("")||limit.equals("null")){
			limit = "10";
		}
		List  data = dao.getAllArticleList(Integer.valueOf(start),Integer.valueOf(limit),bymonth,type,search);
		BigInteger  total = dao.getAllArticleTotal(bymonth,type,search);

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);
			map.put("articleTypeName", objects[5]==null?"":objects[5]);
			map.put("articleDate", objects[3]==null?"":objects[3]);
			map.put("createUser", objects[4]==null?"":objects[4]);
			map.put("ncount", objects[6]==null?"":objects[6]);

			list.add(map);
		}

		pageUtil.parsePage(start, limit, total.intValue());

		return list;

	}
	public List<Map<String, Object>> getShuoList(String start,String limit,PageUtil pageUtil ) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(start==null||start.equals("")||start.equals("null")){
			start = "0";
		}
		if(limit==null||limit.equals("")||limit.equals("null")){
			limit = "5";
		}
		List  data = dao.getShuoList(Integer.valueOf(start),Integer.valueOf(limit) );
        BigInteger total = dao.getShuoTotal();

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);
			map.put("articleTypeName", objects[5]==null?"":objects[5]);
			map.put("articleDate", objects[3]==null?"":objects[3]);
			map.put("createUser", objects[4]==null?"":objects[4]);

			list.add(map);
		}
		pageUtil.parsePage(start, limit, total.intValue());

		return list;

	}
	public List<Map<String, Object>> getAboutme(  ) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List  data = dao.getAboutme( );

		for(int i=0;i<data.size();i++){
			Map<String,Object> map = new HashMap<String, Object>();
			Object[] objects = (Object[]) data.get(i);
			map.put("id", objects[0]==null?"":objects[0]);
			map.put("title", objects[1]==null?"":objects[1]);
			map.put("articleTypeName", objects[5]==null?"":objects[5]);
			map.put("articleDate", objects[3]==null?"":objects[3]);
			map.put("createUser", objects[4]==null?"":objects[4]);
			map.put("content", objects[6]==null?"":objects[6]);

			list.add(map);
		}

		return list;

	}


}
