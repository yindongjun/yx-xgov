package a.test;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

public class BloomFilterTest {
	
	public static void main(String[] args) {
		BloomFilter<String> bloomFilter = BloomFilter.create(new Funnel<String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void funnel(String from, PrimitiveSink into) {
				into.putString(from, Charsets.UTF_8);
			}
		}, 10000000, 0.0001);
		
		boolean isContain = bloomFilter.mightContain("1");
		System.out.println(isContain);
		bloomFilter.put("1");
		isContain = bloomFilter.mightContain("1");
		System.out.println(isContain);
	}

}
