package cn.com.common.ssm.engine.bean;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Unique implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3744890693748892210L;

	@Id
	@GeneratedValue(generator = "JDBC")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Unique other = (Unique) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}