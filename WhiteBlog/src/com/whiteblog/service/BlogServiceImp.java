package com.whiteblog.service;

import com.whiteblog.entity.Blog;
import com.whiteblog.dao.BlogDAO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

public class BlogServiceImp {
	private BlogDAO blogDAO;

	public String addBlog(Blog blog)throws IllegalAccessException, InvocationTargetException{
		Blog newBlog = new Blog();		
		BeanUtils.copyProperties(blog,newBlog);		
		blogDAO.save(newBlog);		
		return "SUCCESS";
	}
	public Blog getBlog(int blogID){
		return blogDAO.findById(blogID);
	}
	public List<Blog> getBlog(String title){
		return blogDAO.findByTitle(title);
	}
	public BlogDAO getBlogDAO() {
		return blogDAO;
	}

	public void setBlogDAO(BlogDAO blogDAO) {
		this.blogDAO = blogDAO;
	}	

}
