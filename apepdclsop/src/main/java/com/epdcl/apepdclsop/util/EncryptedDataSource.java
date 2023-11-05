package com.epdcl.apepdclsop.util;

import org.apache.commons.codec.binary.Base64;

public class EncryptedDataSource  {
	
	public static String decode(String decode) {
		byte[] actualByte = Base64.decodeBase64(decode);

		String actualString = new String(actualByte); 

		return actualString;
	}
}
