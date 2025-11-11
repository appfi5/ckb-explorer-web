package com.ckb.explorer.facade;

import com.ckb.explorer.domain.resp.NetInfoResponse;

/**
 * 网络信息服务接口
 */
public interface INetInfoCacheFacade {

    /**
     * 获取本地节点信息
     * @return 本地节点信息响应
     */
    NetInfoResponse getLocalNodeInfoVersion();
}