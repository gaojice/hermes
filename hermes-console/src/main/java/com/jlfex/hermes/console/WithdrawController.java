package com.jlfex.hermes.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jlfex.hermes.common.dict.Dicts;
import com.jlfex.hermes.common.utils.Calendars;
import com.jlfex.hermes.model.UserProperties;
import com.jlfex.hermes.model.Withdraw;
import com.jlfex.hermes.service.UserInfoService;
import com.jlfex.hermes.service.WithdrawService;

/**
 * 提现控制器
 * 
 * @author ultrafrog
 * @version 1.0, 2014-01-20
 * @since 1.0
 */
@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

	/** 提现业务接口 */
	@Autowired
	private WithdrawService withdrawService;
	
	/** 用户个人信息接口 */
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 索引
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("status", Dicts.elements(Withdraw.Status.class).entrySet());
		model.addAttribute("today", Calendars.date());
		return "withdraw/index";
	}
	
	/**
	 * 数据表格
	 * 
	 * @param name
	 * @param beginDate
	 * @param endDate
	 * @param status
	 * @param page
	 * @param size
	 * @param model
	 * @return
	 */
	@RequestMapping("/table")
	public String table(String name, String beginDate, String endDate, String status, Integer page, Integer size, Model model) {
		model.addAttribute("withdraw", withdrawService.findByNameAndDateBetweenAndStatus(name, beginDate, endDate, status, page, size));
		return "withdraw/table";
	}
	
	/**
	 * 明细
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable("id") String id, Model model) {
		// 查询数据
		Withdraw withdraw = withdrawService.loadById(id);
		UserProperties prop = userInfoService.getProByUser(withdraw.getUser());
		
		// 渲染视图
		model.addAttribute("withdraw", withdraw);
		model.addAttribute("prop", prop);
		model.addAttribute("wait", Withdraw.Status.WAIT);
		model.addAttribute("success", Withdraw.Status.SUCCESS);
		model.addAttribute("failure", Withdraw.Status.FAILURE);
		return "withdraw/detail";
	}
	
	/**
	 * 处理
	 * 
	 * @param id
	 * @param status
	 * @param remark
	 * @param model
	 * @return
	 */
	@RequestMapping("/deal")
	public String deal(String id, String status, String remark, Model model) {
		withdrawService.deal(id, status, remark);
		return detail(id, model);
	}
}
