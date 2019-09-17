package cn.com.yeexun.utils;

/**
 * 使用feign调取其他模块接口时出错的异常类
 */
public class FeignException extends CommonException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String clientName;

    public FeignException(String clientName, String message, Throwable cause){
        super(message, cause);
        this.clientName = clientName;
    }

    public FeignException(String clientName, String message ){
        super(message);
        this.clientName = clientName;
    }

    public FeignException(String message ){
        super(message);
    }

    public String getClientName(){
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
