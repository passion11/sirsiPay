package com.kejing.feepaid.sirsi.service;

import com.kejing.feepaid.sirsi.exception.AuditException;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaid;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaidResponse;

public interface IFeePaidManager {
	public KJFeePaidResponse doFeePaid(KJFeePaid request) throws AuditException;

}
