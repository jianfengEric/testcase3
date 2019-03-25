package com.tng.portal.common.vo.wfl.request;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MAMRequest extends MerchantBase {
	private String mid;
	private String mamId;
	private Destination destination;
	
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	private MerchantType merchantTypeObj;

	public Long getNumericMid()
	{
		Long ret = null;
		if(mid != null)
		{
			String[] array = mid.split("-");
			if(array != null && array.length == 2)
			{
				ret = Long.valueOf(array[1]);
			}
		}
		return ret;
	}
	
	public enum MerchantType{
		MERCHANT("MERCHANT"), AGENT("AGENT"), VIP("VIP");
		
		private final String text;

	    private MerchantType(final String text) {
	        this.text = text;
	    }

	    @Override
	    @JsonValue
	    public String toString() {
	        return text;
	    }
	}
	
	public enum Destination{
		PROD("PROD"), PRE_PRO("PRE_PRO");
		
		private final String text;

	    private Destination(final String text) {
	        this.text = text;
	    }

	    @Override
	    @JsonValue
	    public String toString() {
	        return text;
	    }
	}
	
	public boolean validate() {
		// check mamid
		final String REGEX = "MAM[0-9]{4}-20[0-9]{6}";

		Pattern mamIdPattern = Pattern.compile(REGEX);
		Matcher m = mamIdPattern.matcher(mamId);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	public String getMamId() {
		return mamId;
	}

	public void setMamId(String mamId) {
		this.mamId = mamId;
	}

	public Destination getDestination() {
		return destination;
	}

	public void setDestination(Destination destination) {
		this.destination = destination;
	}

	public MerchantType getMerchantTypeObj() {
		return merchantTypeObj;
	}

	public void setMerchantTypeObj(MerchantType merchantType) {
		this.merchantTypeObj = merchantType;
	}

}




