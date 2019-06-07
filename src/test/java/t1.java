import java.util.ArrayList;
import java.util.List;

import com.kejing.shuyang.kjsip34j.bean.Fee;
import com.kejing.shuyang.kjsip34j.bean.Patron;
import com.kejing.shuyang.kjsip34j.messages.KJFeePaid;
import com.kejing.shuyang.kjsip34j.types.enumerations.CurrencyType;
import com.kejing.shuyang.kjsip34j.types.enumerations.FeeType;
import com.kejing.shuyang.kjsip34j.types.enumerations.PaymentType;

/**
 * 测试类:input String->Json
 */
public class t1 {
	public static void main(String[] args) {
		KJFeePaid kjFeePaid = new KJFeePaid();
		Fee fee = new Fee();
		fee.setCurrencyType(CurrencyType.CHINA_YUAN_RENMINBI);
		fee.setFeeAmount("0.01");
		fee.setFeeType(FeeType.ADMINISTRATIVE);
		fee.setPaymentType(PaymentType.VISA);
		List<Fee> list = new ArrayList<>();
		list.add(fee);
		kjFeePaid.setFees(list);
		kjFeePaid.setDevid("ABCDEF");
		kjFeePaid.setDevls("qwer");
		Patron patron = new Patron();
		patron.setPatronIdentifier("R0401612020002");
		patron.setPatronPassword("1111");
		kjFeePaid.setPatron(patron);
		System.out.println(kjFeePaid.toJasonString());
		// {"devid":"ABCDEF","devls":"f","fees":[{"currencyType":"CHINA_YUAN_RENMINBI","feeAmount":"0.01","feeType":"ADMINISTRATIVE","paymentType":"VISA"}],"patron":{"chargedItems":[],"fineItems":[],"holdItems":[],"overdueItems":[],"patronIdentifier":"R0401411630209","patronPassword":"1111","recallItems":[],"unavailableHoldItems":[]}}
	}

}
