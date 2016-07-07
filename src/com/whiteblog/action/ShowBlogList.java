package com.whiteblog.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.whiteblog.entity.Blog;
import com.whiteblog.entity.User;
import com.whiteblog.service.ShowBlogListService;

public class ShowBlogList extends ActionSupport{
	private List<Blog> blogList;
	private List<Blog> unCheckBlog;
	private ShowBlogListService showBlogListService;

	public String execute() throws ParseException{
		System.out.println("[At ShowBlogList]");
		Map<String,Object> session = ActionContext.getContext().getSession();		
		if(!session.containsKey("loginUser")){
			blogList=showBlogListService.getAllBlog();
			System.out.println("blogList size:"+blogList.size());
			ActionContext.getContext().getSession().put("blogList", blogList);
		}else{
			User user = (User) session.get("loginUser");	
			blogList=showBlogListService.findByUserId(user.getUserId());
			for(int i=0;i<blogList.size();i++){
				if(blogList.get(i).getFilterwords()==0){
					blogList.remove(i);
					i--;
				}
			}
			ActionContext.getContext().getSession().put("blogList", blogList);
		}
		
		Map<Object,Double> blogrank = new HashMap<Object,Double>();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		long nowDate = calendar.getTime().getTime();
		
		for(int i=0;i<blogList.size();i++){			
			int likenumber = blogList.get(i).getLikenumber(); 
			int viewnumber = blogList.get(i).getViewnumber();			
			long publishDate = sdf.parse(blogList.get(i).getTime()).getTime();
			long betweenHour = (nowDate - publishDate)/(1000 * 60 * 60);
			System.out.println("[betweenHour]:"+betweenHour);				
			double rankvalue = (Math.log10(viewnumber)*4 + likenumber)/Math.pow((betweenHour + 2), 1.8);
			System.out.println("[rankvalue]"+ rankvalue);
			blogrank.put(blogList.get(i), rankvalue); 	
		}
		
		List<Map.Entry<Object,Double>> mappingList = new ArrayList<Map.Entry<Object,Double>>(blogrank.entrySet());
		
		Collections.sort(mappingList, new Comparator<Map.Entry<Object,Double>>(){ 
		public int compare(Map.Entry<Object,Double> mapping1,Map.Entry<Object,Double> mapping2){ 
			
			if((mapping2.getValue() - mapping1.getValue())>0){
				return 1;
			}else if((mapping2.getValue() - mapping1.getValue())==0){
				return 0;
			}else{
				return -1;
			}
		} 
		}); 
		
		for(Map.Entry<Object,Double> mapping:mappingList){ 
			   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
		} 
		blogrank.clear();
		
		List<Blog> topblog = new ArrayList<Blog>();	
		for(int i=0;i<6;i++){
			Blog blog = (Blog) mappingList.get(i).getKey();
			topblog.add(blog);
		}	
		session.put("topblog", topblog);
		
		
		
		
		
		
		
		
		
		return SUCCESS;
	}

	
	
	
	
	
	public String changeBlogList(){
//		Map<String,Object> session = ActionContext.getContext().getSession();
//		if(!session.containsKey("loginUser")){
//			blogList=showBlogListService.getAllBlog();
//		}else{
//			int userID = (Integer) session.get("loginUser");	
//			blogList=showBlogListService.findByUserId(userID);
//		}
		return SUCCESS;
	}
	
	
	
	public List<Blog> getBlogList() {
		return blogList;
	}

	public void setBlogList(List<Blog> blogList) {
		this.blogList = blogList;
	}

	public ShowBlogListService getShowBlogListService() {
		return showBlogListService;
	}

	public void setShowBlogListService(ShowBlogListService showBlogListService) {
		this.showBlogListService = showBlogListService;
	}
	
	public String showUNCheckBlog(){
		
		Map<String,Object> session = ActionContext.getContext().getSession();	
		unCheckBlog = showBlogListService.getuncheckBlog();
		System.out.println("[uncheckblog size]:"+unCheckBlog.size());
		session.put("uncheckblog",unCheckBlog);
		
		return SUCCESS;
	}
}
