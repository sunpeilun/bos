package com.czxy.bos.util;

import org.apache.shiro.crypto.hash.*;

/**
 * @Description:
 * @Author:			传智袁新奇
 * @Company:		http://www.czxy.com
 *
 * MD5
 *
 */
public class Encrypt {
	/*
	 * 散列算法一般用于生成数据的摘要信息，是一种不可逆的算法，一般适合存储密码之类的数据，
	 * 常见的散列算法如MD5、SHA等。一般进行散列时最好提供一个salt（盐），比如加密密码“admin”，
	 * 产生的散列值是“21232f297a57a5a743894a0e4a801fc3”，
	 * 可以到一些md5解密网站很容易的通过散列值得到密码“admin”，
	 * 即如果直接对密码进行散列相对来说破解更容易，此时我们可以加一些只有系统知道的干扰数据，
	 * 如用户名和ID（即盐）；这样散列的对象是“密码+用户名+ID”，这样生成的散列值相对来说更难破解。
	 */
	
	//高强度加密算法,不可逆
	public static String md5(String password, String salt){
		return new Md5Hash(password,salt,2).toString();
	}


	public static void main(String[] args) {
		/**
		 * 第一个参数：密码
		 * 第二个参数：盐
		 * 第二个参数：几把
		 *
		 * new Md5Hash("123456","lisi",1):1b539b60601b934441308049a9526e7d
		 * new Md5Hash("123456","lisi",2):42bd4e7685cb11d3ba02716c313cb04b
		 * new Md5Hash("123456","lisi",3):16f807d62105b4896034552ee5caeb8a
		 * new Md5Hash("123456","KMNO4",3):8bd35dc14dc07f756478bb44513694f6
		 */
		//System.out.println(new Md5Hash("123456","KMNO4",3));

		/**
		 *  sha家族加密算法
		 *  sha1:b00ac584c10a5b1c79d4b1371cae06c84bd0984f
		 *  sha256:05daa621cb6824ba46f23e7dcaad4ba9825563b5e446c6c642bfb604ec1e977c
		 *sha384:ab16ae2250a6151ea0e64692775799d6983c1bed4f1b8c4342b5916035b427ee50ca66caad798db113029976b055ded9
		 * sha512:44dc09d07e5cf244d0a9b5ae3f550b62e034a7005fc45b467fd7e2a352985c24cf6526c97220fec13d7ace2cf9eb3d876272d58b4696542bd25dc133e153524c
		 */
		System.out.println("sha1:"+new Sha1Hash("123456","lisi",3));
		System.out.println("sha256:"+new Sha256Hash("123456","lisi",3));
		System.out.println("sha384:"+new Sha384Hash("123456","lisi",3));
		System.out.println("sha512:"+new Sha512Hash("123456","lisi",3));


	}








}