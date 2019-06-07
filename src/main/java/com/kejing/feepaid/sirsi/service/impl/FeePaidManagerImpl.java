package com.kejing.feepaid.sirsi.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.kejing.feepaid.sirsi.dao.module.Tfeepaid;
import com.kejing.feepaid.sirsi.exception.AuditException;
import com.kejing.feepaid.sirsi.service.IFeePaidManager;
import com.kejing.shuyang.kjsip34j.bean.Fee;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaid;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaidResponse;
import com.kejing.shuyang.kjsip34j.types.enumerations.OperResult;

import feign.Response;

@Service
public class FeePaidManagerImpl extends BaseManagerImpl implements IFeePaidManager {

	@Override
	public KJFeePaidResponse doFeePaid(KJFeePaid request) throws AuditException {
		// 查找配置对象

		KJFeePaidResponse kjfeepaidresp = new KJFeePaidResponse();
		kjfeepaidresp.setDevid(request.getDevid());
		kjfeepaidresp.setDevls(request.getDevls());
		kjfeepaidresp.setFees(request.getFees());
		// TODO Auto-generated method stub
		/***
		 * 1.查找是否重复流水 1.new 新平账新记录 2.向sirsi 请求授权 3.向sirsi 平账
		 * 
		 * 4.回写平账结果状态
		 */
		Tfeepaid feepaid = null;
		feepaid = tfeepaidDao.findByForderid(request.getDevls());
		if (feepaid != null) {
			kjfeepaidresp.setOperResult(OperResult.SUCCESS);
			kjfeepaidresp.setScreenMessage("重复流水!");
			kjfeepaidresp.getFees().get(0).setOperResult(OperResult.FAIL);
			kjfeepaidresp.getFees().get(0).setScreenMessage("重复流水!");
			return kjfeepaidresp;
		}
		feepaid = new Tfeepaid();
		Fee respFee = request.getFees().get(0);
		feepaid.setFcardno(request.getFees().get(0).getCardhwno());// 缴费的物理卡号
		feepaid.setFcashier(conf.getCasherid());// 收银台代码
		feepaid.setFcertid(request.getPatron().getPatronIdentifier());// 读者号
		feepaid.setFcommittimes(0);// 初始递交次数为0
		feepaid.setFcurrencytype(request.getFees().get(0).getCurrencyType().getCode());
		feepaid.setFfeeamount(Double.valueOf(request.getFees().get(0).getFeeAmount()));
		feepaid.setFfeeidentifier(request.getFees().get(0).getFeeIdentifier());
		feepaid.setFfeetype(request.getFees().get(0).getFeeType().getCode());
		// feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
		feepaid.setFresv1(request.getPatron().getPatronPassword());
		feepaid.setFopercode((long) 137);
		feepaid.setForderid(request.getDevls());
		feepaid.setFpaymenttype(request.getFees().get(0).getPaymentType().getCode());
		feepaid.setFstatus((long) 0);
		feepaid.setFtimestamp(new Timestamp(new Date().getTime()));
		feepaid.setFyktls(request.getFees().get(0).getYktls());
		tfeepaidDao.save(feepaid);
		/***
		 * 2.向sirsi 请求授权
		 */
		HashMap<String, String> reqParams = new HashMap<>();
		reqParams.put("login", conf.getUsername());
		reqParams.put("password", conf.getPassword());
		Response resp = null;
		try {
			resp = sirsiFeepaidClient.getSession(reqParams);
		} catch (Exception e) {
			kjfeepaidresp.getFees().get(0).setOperResult(OperResult.FAIL);
			kjfeepaidresp.getFees().get(0).setScreenMessage("平账接口通讯错误");
			kjfeepaidresp.setScreenMessage("平账接口通讯错误");
			kjfeepaidresp.setOperResult(OperResult.FAIL);
			return kjfeepaidresp;
		}
		String token = null;
		if (resp.status() == 200) {
			token = resp.body().toString();
		}
		/***
		 * 3.向sirsi 平账
		 */
		if (token != null) {
			reqParams.clear();
			reqParams.put("userID", request.getPatron().getPatronIdentifier());
			reqParams.put("userPin", request.getPatron().getPatronPassword());
			reqParams.put("billAmount", request.getFees().get(0).getFeeAmount());
			reqParams.put("sessionToken", token);
			try {
				resp = sirsiFeepaidClient.doFeepaid(reqParams);
			} catch (Exception e) {
				kjfeepaidresp.getFees().get(0).setOperResult(OperResult.FAIL);
				kjfeepaidresp.getFees().get(0).setScreenMessage("平账接口通讯错误");
				kjfeepaidresp.setScreenMessage("平账接口通讯错误");
				kjfeepaidresp.setOperResult(OperResult.FAIL);
				return kjfeepaidresp;
			}
			String respBody = resp.body().toString();
			if (resp.status() == 200)// 平账中支付请求成功
			{
				long payStatus = 3;
				if ("NO:用户没有需要支付的账单".equals(respBody)) {
					respFee.setOperResult(OperResult.SUCCESS);
					respFee.setScreenMessage(respBody.substring(3));
					payStatus = 2;
				}
				if ("OK".equals(respBody.toUpperCase())) {
					respFee.setOperResult(OperResult.SUCCESS);
					respFee.setScreenMessage("支付成功");
					payStatus = 1;
				}
				feepaid.setFremark(respBody);
				feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
				feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
				feepaid.setFstatus(payStatus);
				tfeepaidDao.save(feepaid);
				if (payStatus == 3) {
					String errorMsg1 = "NO:付款超过所欠金额";
					String errorMsg2 = "NO:There is a problem with this fee transaction. See Librarian.";
					respFee.setOperResult(respBody.equals(errorMsg1) || respBody.equals(errorMsg2) ? OperResult.SUCCESS
							: OperResult.FAIL);
					respFee.setScreenMessage("支付成功,请勿重复提交");
					kjfeepaidresp.setOperResult(OperResult.SUCCESS);
					kjfeepaidresp.setScreenMessage(respBody.substring(3));
				} else {
					kjfeepaidresp
							.setScreenMessage("OK".equals(respBody.toUpperCase()) ? "支付成功" : respBody.substring(3));
					kjfeepaidresp.setOperResult(OperResult.SUCCESS);
				}
			} else// 平账中支付请求失败
			{
				feepaid.setFremark("平账请求失败!");
				feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
				feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
				feepaid.setFstatus((long) 93);
				tfeepaidDao.save(feepaid);
				respFee.setOperResult(OperResult.SUCCESS);
				respFee.setScreenMessage(respBody.substring(3));
				kjfeepaidresp.setOperResult(OperResult.SUCCESS);
				kjfeepaidresp.setScreenMessage("平账失败，系统将稍后为您处理，请勿重复操作!");
			}

		}
		// token失效或者登录不正确
		else {
			feepaid.setFremark("token失效或登录不正确");
			feepaid.setFlastcommit(new Timestamp(new Date().getTime()));
			feepaid.setFcommittimes(feepaid.getFcommittimes() + 1);
			feepaid.setFstatus((long) 92);
			tfeepaidDao.save(feepaid);
			respFee.setOperResult(OperResult.SUCCESS);
			kjfeepaidresp.setOperResult(OperResult.SUCCESS);
			kjfeepaidresp.setScreenMessage("平账失败，系统将稍后为您处理，请勿重复操作!");
		}
		kjfeepaidresp.getFees().add(respFee);
		return kjfeepaidresp;
	}

}
