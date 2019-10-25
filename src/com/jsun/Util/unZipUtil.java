package com.jsun.Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.jupiter.api.Test;

/**
 * 解压工具，支持把压缩文件（目前仅支持Zip格式）解压到当前文件夹和解压到单独文件夹
 * 
 * @author yssSWC
 *
 */
public class unZipUtil {

	public static void main(String[] args) {
		
	}

	/**
	 * 获取程序运行的地址
	 * @param clazz
	 */
	public void getClassPath(Class<Object> clazz) {

		String str = clazz.getClass().getResource("").toString();

		File checkFile = new File("E:\\jsun\\log.txt");
		FileWriter writer = null;
		try {
			// 二、检查目标文件是否存在，不存在则创建
			if (!checkFile.exists()) {
				checkFile.createNewFile();// 创建目标文件
			}
			// 三、向目标文件中写入内容
			// FileWriter(File file, boolean append)，append为true时为追加模式，false或缺省则为覆盖模式
			writer = new FileWriter(checkFile, true);
			writer.append(str);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	/**
	 * 遍历获取指定路径下及其所有子文件夹下的压缩文件路径
	 * 
	 * @param myPath
	 * @return
	 */
	public static void unZipAll(String myPath) {

		// 实例化File对象
		File file = new File(myPath);
		// 判断该File对象是否是文件夹
		if (file.isDirectory()) {
			// 获取该文件夹下所有的文件及文件夹
			File[] files = file.listFiles();
			// 遍历
			for (File f : files) {
				// 判断该File对象是否是文件夹
				if (f.isDirectory()) {
					unZipAll(f.getAbsolutePath());
				} else {
					// 若是文件，进行解压
					unZip(f);
				}
			}
		} else {
			// 若是文件，进行解压
			unZip(file);
		}

	}

	

	/**
	 * 解压中转，判断该文件是否是压缩文件，是的话解压 判断方式有三种： 1、文件后缀名。不安全，但是方便，暂时用这个
	 * 2、文件的Content-type。稍微复杂，但是也不安全。
	 * 3、读取文件流，根据文件流的字节标识来区分不同类型的文件。复杂，安全，但是性能低，暂时不考虑，以后可以封装一下。
	 * 
	 * @param file
	 */
	@Test
	public static void unZip(File file) {

		String filePath = file.getAbsolutePath();
		String fileSuffix;

		if (filePath.indexOf(".") > -1) {
			fileSuffix = filePath.substring(filePath.indexOf("."), filePath.length());
			if (".zip".equalsIgnoreCase(fileSuffix)) {

				// 获取解压到的路径
				String parent = getParentPath(filePath, null);
				// 解压
				try {
					doUnZip(parent, filePath);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	/**
	 * 获取解压到的路径，目前解压路径有三种，默认为解压到单个文件夹中 1、解压到当前文件夹中 2、解压到单个文件夹中（暂时默认） 3、解压到指定文件夹中
	 * 
	 * @param filePath 压缩文件路径
	 * @param path     指定文件夹路径
	 * @return
	 */
	@Test
	public static String getParentPath(String filePath, String path) {

		String dirPath = filePath.substring(0, filePath.indexOf("."));

		return dirPath;
	}

	/**
	 * 把压缩文件解压到指定路径下
	 * 
	 * @param Parent 解压到的路径
	 * @param zipUrl 压缩文件地址
	 */
	public static void doUnZip(String parent, String zipUrl) throws InterruptedException {

		try {
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipUrl));
			BufferedInputStream Bin = new BufferedInputStream(Zin);

			File file = null;
			ZipEntry entry;

			try {
				while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
					file = new File(parent, entry.getName());
					if (!file.exists()) {
						(new File(file.getParent())).mkdirs();
					}
					FileOutputStream out = new FileOutputStream(file);
					BufferedOutputStream Bout = new BufferedOutputStream(out);

					int b;
					while ((b = Bin.read()) != -1) {
						Bout.write(b);
					}
					Bout.close();
					out.close();

				}

				Bin.close();
				Zin.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
