package com.codefarme.imchat.utils;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ImageUtil {
	/**
	 * 保存一张图片返回地址
	 * @param name
	 * @param image
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String upload(
			String name,
			MultipartFile image,
			HttpServletRequest request)
			throws IOException{
		
		//文件的名称
		System.out.println(
			image.getOriginalFilename());
		String filename=image.getOriginalFilename();
		int lastIndexDoc = filename.lastIndexOf(".");
		String suffix = ".jpg";
		if (lastIndexDoc > -1) {
			suffix = filename.substring(lastIndexDoc);
		}
		String newname=System.currentTimeMillis()+ CommonUtils.getRandomString(6)+suffix;
		//String path = "D:/images"+timeFolder+"/";
		String timeFolder = DateUtil.getCurrentTime("yyyyMMdd");
		String oldpath="/upload/image/" +name+"/"+ timeFolder;//WEB路径
			
		//将WEB路径转换为当前操作系统的实际路径
		String path = request.getSession().getServletContext()
				.getRealPath(oldpath);
			
		System.out.println("新的path地址"+path);
		//输出实际路径:
		System.out.println("path:"+path); 
		String savepath=path;
		File dir=new File(savepath);
		dir.mkdirs();
		File file = new File(dir, newname);
		image.transferTo(file);
		//返回结果
		return oldpath+"/"+newname;	
	}
	/**
	 * 保存视频的返回地址和封面的返回地址
	 * @param name
	 * @param image
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> uploadvideo(
			String name,
			MultipartFile video,
			HttpServletRequest request)
			throws IOException{
		//MultipartFile 封装了全部的上载文件信息
		//利用其方法可以获取全部的上载信息
		//显示上载文件的文件名OriginalFilename
		System.out.println(
			video.getOriginalFilename());
		//文件大小(字节数)image.getSize()
		//返回input name属性的值 image.getName()
		//返回文件的全部字节 image.getBytes()
		//返回流,其中包含文件的全部字节
		//      image.getInputStream();
		//返回文件的类型: image.getContentType()
		//将上载的文件直接保存到一个目标文件中
		//      image.transferTo(file);
		String filename=video.getOriginalFilename();
		int lastIndexDoc = filename.lastIndexOf(".");
		String suffix = ".jpg";
		if (lastIndexDoc > -1) {
			suffix = filename.substring(lastIndexDoc);
		}
		String newname=System.currentTimeMillis()+ CommonUtils.getRandomString(6)+suffix;
		//String path = "D:/images"+timeFolder+"/";
		String timeFolder = DateUtil.getCurrentTime("yyyyMMdd");
		String oldpath="/upload/video/" +name+"/"+ timeFolder;//WEB路径
			
		//将WEB路径转换为当前操作系统的实际路径
		String path = request.getSession().getServletContext()
				.getRealPath(oldpath);
			
		System.out.println("新的path地址"+path);
		//输出实际路径:
		System.out.println("path:"+path); 
		String savepath=path;
		File dir=new File(savepath);
		dir.mkdirs();
		File file = new File(dir, newname);
		video.transferTo(file);
		//返回结果
		String videopath=oldpath+"/"+newname;
		String newvideopath= CommonUtils.addpath(request, videopath);
		String videoimgname=System.currentTimeMillis()+ CommonUtils.getRandomString(6)+".jpg";
		String imgpath=oldpath+"/"+videoimgname;
		String newimgpath=path+"/"+videoimgname;
		try {
			fetchFrame(newvideopath,newimgpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> map=new HashMap<>();
		map.put(videopath, imgpath);
		return map;
	}
	/**
	 * 获取指定视频的帧并保存为图片至指定目录
	 * @param videofile  源视频文件路径
	 * @param framefile  截取帧的图片存放路径
	 * @throws Exception
	 */
	public static Map<String, String> fetchFrame(String videofile, String framefile)
	        throws Exception {
	    long start = System.currentTimeMillis();
	    File targetFile = new File(framefile);
	    FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile); 
	    ff.start();
	    int lenght = ff.getLengthInFrames();
	    int i = 0;
	    Frame f = null;
	    while (i < lenght) {
	        // 过滤前5帧，避免出现全黑的图片，依自己情况而定
	        f = ff.grabFrame();
	        if ((i > 5) && (f.image != null)) {
	            break;
	        }
	        i++;
	    }


	    IplImage img = f.image;
	    int owidth = img.width();
	    int oheight = img.height();
	    // 对截取的帧进行等比例缩放
	    int width = 800;
	    int height = (int) (((double) width / owidth) * oheight);
	    //int height =600;
	    //System.out.println("输出的长宽是多少："+width+height);
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width, height, Image.SCALE_SMOOTH),
	            0, 0, null);
	    ImageIO.write(bi, "jpg", targetFile);
	    Map<String, String> map=new HashMap<String ,String>();
	    map.put("width", width+"");
	    map.put("height", height+"");
	    //ff.flush();
	    ff.stop();
	    System.out.println(System.currentTimeMillis() - start);
	    return map;
	}
	
	/**
	 * 返回文件集合
	 * @param request 包含文件的request
	 * @param filename 文件参数名
	 * @return
	 */
	public static List<MultipartFile> getImgFile(
			HttpServletRequest request,
			String filename) {
		List<MultipartFile> img = new LinkedList<MultipartFile>();
		if (ServletFileUpload.isMultipartContent(request)){ 
			MultipartHttpServletRequest multipartRequest = null;
	        try {  
	            multipartRequest = (MultipartHttpServletRequest) request;
	        } catch (Exception e) {  
	            
	        }  
	        img = multipartRequest.getFiles(filename);
	        System.out.println(img);
		}
		return img;
	}
	
	

	
	
}
