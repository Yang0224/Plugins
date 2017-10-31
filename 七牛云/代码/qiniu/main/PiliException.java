package com.utils.qiniu.main;

import okhttp3.Response;

/**
 * 错误类型
 * <p></p>
 * @author YangShaoPing 2017年5月10日 上午9:32:38
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2017年5月10日
 * @modify by reason:{方法名}:{原因}
 */
public class PiliException extends Exception {
    /**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1L;
	public final Response response;
    private final String ErrNotFound = "stream not found";
    private final String ErrDuplicate = "stream already exists";
    private final String ErrNotLive = "no data";


    public PiliException(Response response) {
        this.response = response;
    }

    public PiliException(String msg) {
        super(msg);
        response = null;
    }

    public PiliException(Exception e) {
        super(e);
        this.response = null;
    }

    public int code() {
        return response == null ? -1 : response.code();
    }

    public boolean isDuplicate() {
        return code() == 614;
    }

    public boolean isNotFound() {
        return code() == 612;
    }

    public boolean isNotInLive() {
        return code() == 619;
    }

    public String getMessage() {
        if (response == null) {
            return super.getMessage();
        } else {
            switch (code()) {
                case 614:
                    return ErrDuplicate;
                case 612:
                    return ErrNotFound;
                case 619:
                    return ErrNotLive;
                default:
                    return response.message();
            }
        }
    }

}
