package cn.com.common.ssm.engine.interceptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

/**
 * 
 *
 * @author yong.zhou
 * @version 3.4, 2016年9月22日 下午2:14:27
 * @since Yeexun
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = IOUtils.toString(request.getInputStream(), "UTF-8").getBytes();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		final ByteArrayInputStream bais = new ByteArrayInputStream(body);

		return new ServletInputStream() {

			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}
}