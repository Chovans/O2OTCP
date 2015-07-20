package com.dotnar.controller;

import com.dotnar.util.CheckCodeUtil;

public class CheckCodeController {
	private CheckCodeUtil checkCodeUtil = new CheckCodeUtil();

	public String getCheckCode(String code) {
		return checkCodeUtil.crimg(code);
	}

}
