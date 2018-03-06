package com.semioe.dubbo.service;

import com.semioe.api.vo.UserShareServiceVO;
import com.semioe.common.result.Result;

public interface ShareServiceService {

	Result addShareServices(UserShareServiceVO service);

	Result removeShareServices(UserShareServiceVO service);

	Result getShareServices(UserShareServiceVO shareService);
}