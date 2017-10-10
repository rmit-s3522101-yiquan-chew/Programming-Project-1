package model;

import java.math.BigDecimal;

public class Util {

	public BigDecimal calculateChange(BigDecimal baseNum, BigDecimal newNum)
	{
		BigDecimal increase = newNum.subtract(baseNum);
		BigDecimal result = increase.divide(baseNum, 4, BigDecimal.ROUND_HALF_UP).multiply(newNum);

		return result.setScale(4, BigDecimal.ROUND_HALF_UP);
	}
	
}
