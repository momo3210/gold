package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.momohelp.calculate.service.ICalculation;
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmService;
import com.momohelp.service.UserService;

//动态奖金计算
public class DynamicCalculation implements Serializable, ICalculation {

	private static final long serialVersionUID = -817967291317652692L;
	private static Logger log = Logger.getLogger(DynamicCalculation.class);
	@Resource
	private CfgService cfgService;
	@Resource
	private FarmService farmService;
	@Resource
	private UserService userService;

	// 用户等级计算
	@Override
	public boolean calculateLevel(List<Farm> farms) {
		boolean bool = false;
		for (Farm farm : farms) {
			User user = userService.selectByKey(farm.getUser_id());// 当前用户id
			// user.getPid() 获取当前用户的领导
			Map<String, Object> childs = userService.countMemberNOAndlevel(user
					.getPid());
			// 用户等级判断
			String lv = user.getLv();
			/**
			 * level 级别（05贫农、06中农、07富农、08农场主）
			 */
			
			switch (lv) {
			case Parameter.POOR_PEASANT:// 05贫农
				childs.get("lv");
				break;
			case Parameter.MIDDLE_PEASANT:// 06中农

				break;
			case Parameter.RICH_PEASANT:// 07富农

				break;
			case Parameter.FARMERS:// 08农场主

				break;
			default:
				break;
			}

		}
		return bool;
	}

	// 奖金基数计算
	/****
	 * 注意事项： 计算后的基数如果小于0 返回0 前置条件：买卖盘匹配结束，且没有遗留问题 当前单购买数量和成交数量相等
	 * 业务描述：判断是否出局条件【w_farm_chick表中是否出局标志】： 1.正常 0 出局 出局需要计算烧伤奖 正常排单基数不变 即购买金额
	 * 计算公式：小端金额- 推荐人最近获取被推荐人一单的推荐奖-被推荐人上一单所得利息 SQL： 被推荐人上一单推荐奖 SELECT w.* FROM
	 * w_farm_chick as w WHERE w.user_id=? ORDER BY w.create_time DESC LIMIT 0,2
	 * 中的第二条数据：来计算推荐奖 和等级相关 所得利息： 幼鸡利息=幼鸡数量* 喂养时长+成年鸡利息=成年鸡数量*喂养时长
	 * 
	 * 成年鸡数量： 可能在喂养过程中卖掉成年鸡 ，需要每日计算
	 */
	@Override
	public int base() {
		int base = 0;
		log.info("-------------------奖金基数计算------------------------");
		List<Farm> farms = farmService.getUntreatedFarm();
		// 等级计算
		if (calculateLevel(farms)) {
			// 计算奖金基数

		} else {
			// 计算等级失败
			return -1;
		}
        
		return base;
	}

	// 根据用户代数 计算代数字符串编号
	private String getCoefficientNO(int generation) {
		String generationNO = null;
		switch (generation) {
		case 1:
			generationNO = "01";
			break;
		case 2:
			generationNO = "02";
			break;
		case 3:
			generationNO = "03";
			break;
		case 4:
			generationNO = "04";
			break;
		case 5:
			generationNO = "05";
			break;
		case 6:
			generationNO = "06";
			break;

		}
		return generationNO;
	}

	// 根据用户等级和代数 计算提成系数编号
	private String getCoefficientNO(String lv, int generation) {

		StringBuffer base = new StringBuffer(8);
		base.append(lv);
		base.append(getCoefficientNO(generation));
		return base.toString();
	}

	// 计算提成
	/***
	 * 
	 * 
	 * @param base
	 *            提成基数
	 * @param uuid
	 *            当前等级lv编号
	 * @param generation
	 *            直系几代
	 * @return
	 */
	private double calculateRoyalty(int base, String lv, int generation) {
		Cfg cfg = cfgService.selectByKey(getCoefficientNO(lv, generation));
		return Double.parseDouble(cfg.getValue_()) * base;
	}

	// 买卖盘自动匹配
	/***
	 * 前置条件： 买卖盘中的日期都是冻结或者匹配周期后的日期 业务：获取当天 买卖盘中的数据 以及 交易记录中
	 * 卖盘id为空或者卖盘id为空且状态为0数据 SQL： 1.SELECT * FROM p_buy_sell as p WHERE
	 * (p.'p_buy_id' ='null' or p.'p_sell_id'='null') and p.`status`='0'
	 * 2.SELECT * FROM p_buy as p WHERE p.create_time=SYSDATE() 3.SELECT * FROM
	 * p_sell as p WHERE p.create_time=SYSDATE() 原则：优先匹配交易记录表中的数据 其次 卖家为主
	 *******************************/
	@Override
	public boolean automatch() {
		boolean bool = false;
		// 判断卖家数据大于买家数据 ：则 插入交易记录中一条数据【插入数据已买家为准】 并且买卖金额进行减法运算
		// 判断卖家数据等于买家数据 ：则 插入交易记录中一条数据【插入数据已买家为准】 并且买卖金额进行减法运算
		// 判断卖家数据小于买家数据 ：则 插入交易记录中一条数据【插入数据已卖家为准】 并且买卖金额进行减法运算
		return bool;
	}

}
