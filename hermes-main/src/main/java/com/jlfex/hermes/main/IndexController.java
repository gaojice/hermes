package com.jlfex.hermes.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jlfex.hermes.common.Logger;
import com.jlfex.hermes.common.cache.Caches;
import com.jlfex.hermes.common.dict.Element;
import com.jlfex.hermes.service.ArticleService;
import com.jlfex.hermes.service.LoanService;
import com.jlfex.hermes.service.web.PropertiesFilter;

/**
 * 索引控制器
 * 
 * @author ultrafrog
 * @version 1.0, 2013-12-18
 * @since 1.0
 */
@Controller
public class IndexController {

	/** 文章业务接口 */
	@Autowired
	private ArticleService articleService;
	
	/** 借款业务接口 */
	@Autowired
	private LoanService loanService;
	
	/**
	 * 索引
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		// 设置数据
		model.addAttribute("nav", HomeNav.HOME);
		model.addAttribute("notices", articleService.findHomeNotices());
		model.addAttribute("loans", loanService.findForIndex());
		
		// 渲染视图
		return "index";
	}
	
	/**
	 * 公告列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/notices")
	public String notices(Integer page, Integer size, Model model) {
		model.addAttribute("notices", articleService.findNotices(page, size));
		return "/system/notices";
	}
	
	/**
	 * 公告内容
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/notice/{id}")
	public String notice(@PathVariable("id") String id, Model model) {
		model.addAttribute("article", articleService.loadByIdWithText(id));
		return "/system/notice";
	}
	
	/**
	 * 清空缓存
	 * 
	 * @return
	 */
	@RequestMapping("/clear")
	public void clear() {
		PropertiesFilter.clear();
		Caches.clear();
		Logger.info("clear all cache.");
	}
	
	/**
	 * 首页导航
	 * 
	 * @author ultrafrog
	 * @version 1.0, 2013-12-31
	 * @since 1.0
	 */
	public static final class HomeNav {
		
		@Element("首页")
		public static final String HOME		= "home";
		
		@Element("我要理财")
		public static final String INVEST	= "invest";
		
		@Element("我要借款")
		public static final String LOAN		= "loan";
		
		@Element("账户中心")
		public static final String ACCOUNT	= "account";
		
		@Element("使用帮助")
		public static final String HELP		= "help";
	}
}
