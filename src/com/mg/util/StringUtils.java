package com.mg.util;

import com.jfinal.kit.PropKit;

public class StringUtils {
	public static final String DOT = ".";  
    public static final String SLASH_ONE = "/";  
    public static final String SLASH_TWO = "\\";
	/**
	 * 得到字符串首字母大写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstUpper(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String firstLower(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 全部是大写字母
	 * 
	 * @param word
	 * @return
	 */
	public static boolean isAllUpper(String word) {
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			if (c != '_') {
				if (!Character.isUpperCase(c)) {
					return false;
				}
			}
		}
		return true;
	}
	public static void main(String[] args) {
		String t = "XXX_JJa";
		System.out.println(isAllUpper(t));
	}
	/**
	 * 得到property正确的拼写
	 * @param string
	 * @param px
	 * @return
	 */
	public static String getProperty(String string, int px) {
		/**
		 * px:
		 * 1 - 全大写
		 * 2 - 全小写
		 * 3 - 首字母大写
		 * 4 - 首字母小写
		 */
		String res = string;
		if(px == 1){
			res = string.toUpperCase();
		}else if(px == 2){
			res = string.toLowerCase();
		}else if(px == 3){
			res = firstUpper(string);
		}else if(px == 4){
			res = firstLower(string);
		}
		return res;
	}
	/**
	 * 字符串反转
	 * @param str
	 * @return
	 */
	public static String reverse(String str){
		return new StringBuilder(str).reverse().toString();
	}
	/** 
     * 获取没有扩展名的文件名 
     *  
     * @param fileName 
     * @return 
     */  
    public static String getWithoutExtension(String fileName) {  
        String ext = org.apache.commons.lang.StringUtils.substring(fileName, 0,  
                org.apache.commons.lang.StringUtils.lastIndexOf(fileName, DOT));  
        return org.apache.commons.lang.StringUtils.trimToEmpty(ext);  
    }  
  
    /** 
     * 获取扩展名 
     *  
     * @param fileName 
     * @return 
     */  
    public static String getExtension(String fileName) {  
        if (org.apache.commons.lang.StringUtils.INDEX_NOT_FOUND == org.apache.commons.lang.StringUtils.indexOf(fileName, DOT))  
            return org.apache.commons.lang.StringUtils.EMPTY;  
        String ext = org.apache.commons.lang.StringUtils.substring(fileName,  
                org.apache.commons.lang.StringUtils.lastIndexOf(fileName, DOT));  
        return org.apache.commons.lang.StringUtils.trimToEmpty(ext);  
    }  
  
    /** 
     * 判断是否同为扩展名 
     *  
     * @param fileName 
     * @param ext 
     * @return 
     */  
    public static boolean isExtension(String fileName, String ext) {  
        return org.apache.commons.lang.StringUtils.equalsIgnoreCase(getExtension(fileName), ext);  
    }  
  
    /** 
     * 判断是否存在扩展名 
     *  
     * @param fileName 
     * @return 
     */  
    public static boolean hasExtension(String fileName) {  
        return !isExtension(fileName, org.apache.commons.lang.StringUtils.EMPTY);  
    }  
  
    /** 
     * 得到正确的扩展名 
     *  
     * @param ext 
     * @return 
     */  
    public static String trimExtension(String ext) {  
        return getExtension(DOT + ext);  
    }  
  
    /** 
     * 向path中填充扩展名(如果没有或不同的话) 
     *  
     * @param fileName 
     * @param ext 
     * @return 
     */  
    public static String fillExtension(String fileName, String ext) {  
        fileName = replacePath(fileName + DOT);  
        ext = trimExtension(ext);  
        if (!hasExtension(fileName)) {  
            return fileName + getExtension(ext);  
        }  
        if (!isExtension(fileName, ext)) {  
            return getWithoutExtension(fileName) + getExtension(ext);  
        }  
        return fileName;  
    }  
  
    /** 
     * 判断是否是文件PATH 
     *  
     * @param fileName 
     * @return 
     */  
    public static boolean isFile(String fileName) {  
        return hasExtension(fileName);  
    }  
  
    /** 
     * 判断是否是文件夹PATH 
     *  
     * @param fileName 
     * @return 
     */  
    public static boolean isFolder(String fileName) {  
        return !hasExtension(fileName);  
    }  
  
    public static String replacePath(String path) {  
        return org.apache.commons.lang.StringUtils.replace(org.apache.commons.lang.StringUtils.trimToEmpty(path), SLASH_ONE,  
                SLASH_TWO);  
    }  
  
    /** 
     * 链接PATH前处理 
     *  
     * @param path 
     * @return 
     */  
    public static String trimLeftPath(String path) {  
        if (isFile(path))  
            return path;  
        path = replacePath(path);  
        String top = org.apache.commons.lang.StringUtils.left(path, 1);  
        if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(SLASH_TWO, top))  
            return org.apache.commons.lang.StringUtils.substring(path, 1);  
        return path;  
    }  
  
    /** 
     * 链接PATH后处理 
     *  
     * @param path 
     * @return 
     */  
    public static String trimRightPath(String path) {  
        if (isFile(path))  
            return path;  
        path = replacePath(path);  
        String bottom = org.apache.commons.lang.StringUtils.right(path, 1);  
        if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(SLASH_TWO, bottom))  
            return org.apache.commons.lang.StringUtils.substring(path, 0, path.length() - 2);  
        return path + SLASH_TWO;  
    }  
  
    /** 
     * 链接PATH前后处理，得到准确的链接PATH 
     *  
     * @param path 
     * @return 
     */  
    public static String trimPath(String path) {  
        path = org.apache.commons.lang.StringUtils.replace(org.apache.commons.lang.StringUtils.trimToEmpty(path), SLASH_ONE,  
                SLASH_TWO);  
        path = trimLeftPath(path);  
        path = trimRightPath(path);  
        return path;  
    }  
  
    /** 
     * 通过数组完整链接PATH 
     *  
     * @param paths 
     * @return 
     */  
    public static String bulidFullPath(String... paths) {  
        StringBuffer sb = new StringBuffer();  
        for (String path : paths) {  
            sb.append(trimPath(path));  
        }  
        return sb.toString();  
    }  
}
