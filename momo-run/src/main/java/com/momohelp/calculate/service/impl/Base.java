package com.momohelp.calculate.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.momohelp.calculate.service.Ibase;
import com.momohelp.model.Cfg;
import com.momohelp.model.Farm;
import com.momohelp.model.FarmFeed;
import com.momohelp.model.ModelLV;
import com.momohelp.model.Prize;
import com.momohelp.model.User;
import com.momohelp.service.CfgService;
import com.momohelp.service.FarmFeedService;
import com.momohelp.service.FarmService;
import com.momohelp.service.PrizeService;
import com.momohelp.service.UserService;
@Service
public class Base implements Ibase, Serializable {

	private static final long serialVersionUID = -7329182768601602219L;

	private static Logger log = Logger.getLogger(Base.class);

	@Resource
	private CfgService cfgService;
	@Resource
	private FarmService farmService;
	@Resource
	private UserService userService;

	@Resource
	private PrizeService prizeService;

	@Resource
	private FarmFeedService farmFeedService;

	// 奖金基数计算
	/****
	 * 注意事项： 计算后的基数如果小于0 返回0
	 * 
	 * 前置条件：买卖盘匹配结束，且没有遗留问题 当前单购买数量和成交数量相等
	 * 
	 * 业务描述：判断是否出局条件【w_farm_chick表中是否出局标志】： 1.正常 0 出局
	 * 
	 * 出局需要计算烧伤奖 正常排单基数不变 即购买金额
	 * 
	 * 计算公式：小端金额- 推荐人最近获取被推荐人一单的推荐奖-被推荐人上一单所得利息
	 * 
	 * SQL： 被推荐人上一单推荐奖 SELECT w.* FROM w_farm_chick as w WHERE w.user_id=? ORDER
	 * BY w.create_time DESC LIMIT 0,2 中的第二条数据：来计算推荐奖 和等级相关
	 * 
	 * 所得利息： 幼鸡利息=幼鸡数量* 喂养时长+成年鸡利息=成年鸡数量*喂养时长
	 * 
	 * 成年鸡数量： 可能在喂养过程中卖掉成年鸡 ，需要每日计算
	 */
	@Override
	public double base() {
		double base = 0.00;
		log.info("-------------------奖金基数计算------------------------");
		List<Farm> farms = farmService.getUntreatedFarm();
		// 用户等级计算
		if (calculateLevel(farms)) {  
			// 计算奖金基数
			for (Farm farm : farms) {
				User user = userService.selectByKey(farm.getUser_id());// 获取当前排单的用户
				if (farm.getPid_higher_ups() == null
						|| farm.getPid_higher_ups().trim().length() == 0) {
					continue;
				}
				if ("0".equals(farm.getPid_higher_ups())) {
					continue;
				}
				User leader = userService.selectByKey(user.getPid());// 获取当前排单的用户的领导
				if (leader==null) {
					continue;
				}
				Farm f = farmService.selectByKey(farm.getPid_higher_ups());// 领导最近一单
				double tempBase = 0.00;
				// 小端金额
				if (farm.getNum_buy() >= f.getNum_buy()) {
					tempBase = f.getNum_buy();
				} else {
					tempBase = farm.getNum_buy();
				}
				if (f.getFlag_out_p() == 2) {// 领导排单已经出局 f.getFlag_out()==3时
												// 计算基数为0 也就不用计算了
					/***
					 * 提成基数计算
					 * 
					 * 计算公式：小端金额- 推荐人获取被推荐人最近一单的推荐奖-被推荐人当前单所得利息
					 * 
					 * 利息包含：
					 * 
					 * 1.幼鸡饲养利息：获取配置表中幼鸡饲养利率 * 幼鸡饲养周期
					 * 
					 * 2.成年下蛋利息：获取这一批次 * 时间
					 * 
					 */
					// 获得当前用户的上一单
					Farm beforeFarm = farmService.selectByKey(farm.getPid());
					Example example = new Example(Prize.class);
					example.createCriteria()
							.andEqualTo("relation_id", beforeFarm.getId())
							.andEqualTo("user_id", leader.getId());
					// 获取当前用户排单上一单 领导拿到的提成--推荐奖
					double money = prizeService.selectByExample(example).get(0)
							.getMoney();//
					// 获得当前用户的上一单饲养成年鸡利息
					Example example2 = new Example(FarmFeed.class);
					example2.createCriteria().andEqualTo("w_farm_chick_id",
							beforeFarm.getId());
					List<FarmFeed> farmFeeds = farmFeedService
							.selectByExample(example2);
					double chicken = 0.00;
					// 成鸡饲养利息计算
					for (FarmFeed farmFeed : farmFeeds) {
						chicken = farmFeed.getPrice() + chicken;
					}
					// 获得当前单饲养利息总和
					double deposit = beforeFarm.getNum_buy()
							* Integer.parseInt(cfgService.selectByKey("0205")
									.getValue_()) + chicken;
					tempBase = tempBase - money - deposit;
				}
				if (tempBase <= 0.0) {
					break;
				}
				int temp = user.getDepth();
				User userTemp = user;
				Prize entity = null;
				while (true) {// 这样可能数据不统一：
					userTemp = userService.selectByKey(userTemp.getPid());// 2
					if (null == userTemp) {// 死循环终止条件
						break;
					}
					if (userTemp.getId().trim().length() == 0) {
						continue;
					}
					int depth = temp - userTemp.getDepth();
					double number = calculateRoyalty(tempBase,
							userTemp.getLv(), depth);
					entity = new Prize();
					entity.setId(UUID.randomUUID().toString().replace("-", ""));
					entity.setCreate_time(new Date());
					entity.setDepth(depth);
					entity.setMoney(number);
					entity.setFlag(0);
					entity.setUser_id(userTemp.getId());
					Calendar cr = Calendar.getInstance();
					if (depth == 1) {
						cr.add(Calendar.DAY_OF_MONTH, 7);
					} else {
						cr.add(Calendar.DAY_OF_MONTH, 15);
					}
					entity.setTrigger_time(cr.getTime());
					entity.setRelation_id(farm.getId());
					prizeService.save(entity);
				}
				farm.setFlag_calc_bonus(1);
				farmService.updateNotNull(farm);
			}
		} else {
			// 计算用户等级失败
			return -1;
		}
		return base;
	}

	// 用户等级计算
	@Override
	public boolean calculateLevel(List<Farm> farms) {
		boolean bool = false;
		for (Farm farm : farms) {
			User user = userService.selectByKey(farm.getUser_id());// 当前用户id
			if (user == null) {
				break;
			}
			String pid = user.getPid();// 获取当前用户的领导
			if (pid.equals("0")) {
				continue;
			}
			List<ModelLV> childsList = (List<ModelLV>) userService
					.countMemberNOAndlevel(pid);
			/**
			 * level 级别（05贫农、06中农、07富农、08农场主）
			 */
			List<String> list = new ArrayList<String>(8);
			list.add(Parameter.POOR_PEASANT);
			list.add(user.getLv());
			for (ModelLV modelLV : childsList) {
				// System.err.println(modelLV.getLv() + "---" +
				// modelLV.getNo());
				switch (modelLV.getLv()) {
				case Parameter.POOR_PEASANT:
					if (modelLV.getNo() >= 4
							&& userService.countLvNO(pid,
									Parameter.POOR_PEASANT) >= 4) {
						list.add(Parameter.MIDDLE_PEASANT);
					}
					break;
				case Parameter.MIDDLE_PEASANT:
					if (modelLV.getNo() >= 8) {
						list.add(Parameter.RICH_PEASANT);
					}
					break;
				case Parameter.RICH_PEASANT:
					if (modelLV.getNo() >= 12) {
						list.add(Parameter.FARMERS);
					}
					break;
				case Parameter.FARMERS:
					if (modelLV.getNo() >= 1) {
						list.add(Parameter.FARMERS);
					}
					break;
				default:
					break;
				}
			}
			user = new User();
			user.setId(pid);
			user.setLv(Collections.max(list));
			userService.updateNotNull(user);
		}
		bool = true;
		return bool;
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
	private double calculateRoyalty(double base, String lv, int generation) {
		Cfg cfg = cfgService.selectByKey(getCoefficientNO(lv, generation));
		return Double.parseDouble(cfg.getValue_()) * base;
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
		default:
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

}
