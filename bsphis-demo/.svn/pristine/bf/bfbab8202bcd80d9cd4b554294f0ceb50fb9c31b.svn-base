package phis.source.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

	/**
	 * 日志记录阶段
	 * 
	 * @param success
	 *            方法运行成功后记录日志
	 * @param always
	 *            不管是否成功都记录日志
	 * @return
	 */
	public String exec() default "success";

	/**
	 * 日志描述信息
	 * 
	 * @return
	 */
	public String desc() default "";

	/**
	 * 快照存储方式
	 * 
	 * @param auto
	 *            支持canvas的浏览器以图片形式存储,否则以html文本存储
	 * @param image
	 *            以图片形式存储,不支持canvas的浏览器则不记录快照
	 * @param html
	 *            以html方式存储快照
	 * @return
	 */
	public String snapshot() default "";
}
