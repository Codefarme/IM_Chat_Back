package com.codefarme.imchat.service.impl;

import com.codefarme.imchat.config.Result;
import com.codefarme.imchat.config.ZxfConstans;
import com.codefarme.imchat.mapper.*;
import com.codefarme.imchat.oss.OSUtils;
import com.codefarme.imchat.pojo.*;
import com.codefarme.imchat.response.*;
import com.codefarme.imchat.service.DynamicService;
import com.codefarme.imchat.service.ServiceException;
import com.codefarme.imchat.service.UserVipService;
import com.codefarme.imchat.tencent.XGUtils;
import com.codefarme.imchat.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class DynamicServiceImpl implements DynamicService {

    @Resource
    private UserVipService vipUSerService;

    @Resource
    private DynamicMapper dynamicMapper;
    @Resource
    private UserInfoMapper userMapper;

    @Autowired
    private DynamicSoundMapper voiceMapper;

    @Autowired
    private DynamicCommentReplyMapper commentReplyMapper;
    @Autowired
    private DynamicCommentMapper dynamicCommentMapper;

    @Autowired
    private DynamicImgMapper dynamicImgMapper;

    @Autowired
    private DynamicVideoMapper dynamicVideoMapper;

    @Autowired
    private DynamicPraiseMapper dynamicPraiseMapper;


    @Autowired
    private DynamicViewMapper dynamicViewMapper;


    @Autowired
    private UserAttentionMapper userAttentionMapper;

    /**
     * 使用云存储的方式存储发布动态的图片和语音文件
     *
     * @param request
     */
    @Override
    @Transactional
    public void publish(HttpServletRequest request) {

        String account = request.getParameter("account");
        String written = request.getParameter("written");
        String time = request.getParameter("time");
        String type = request.getParameter("type");//动态类型

        Dynamic dynamic = new Dynamic();
        dynamic.setAccount(account);
        dynamic.setContent(written);
        dynamic.setAddTime(DateUtil.getCurrentTime());
        dynamic.setType(Integer.valueOf(type));
        dynamic.setStatus(0);

        if (account == null) {
            throw new ServiceException("账号不能为空");
        }
        Integer cid = publishdynamicofwritten(dynamic);

        //取文件
        if (ServletFileUpload.isMultipartContent(request)) {
            MultipartHttpServletRequest multipartRequest = null;
            try {
                multipartRequest = (MultipartHttpServletRequest) request;
            } catch (Exception e) {

            }
            //取图片文件
            List<MultipartFile> img = new LinkedList<MultipartFile>();
            img = multipartRequest.getFiles("picture");
            if (img.size() > 0) {
                for (MultipartFile pictureImg : img) {

                    String key = null;
                    try {
                        if (null != pictureImg && pictureImg.getBytes().length > 0) {

                            key = OSUtils.upload(pictureImg.getBytes(), true, account);//阿里云

                            DynamicImg dynamicimg = new DynamicImg();
                            dynamicimg.setCid(cid);
                            dynamicimg.setImgPath(key);
                            Integer integer = dynamicImgMapper.insert(dynamicimg);
                            if (integer != 1) {
                                throw new ServiceException("发布失败");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            //取音频文件
            List<MultipartFile> voice = new LinkedList<MultipartFile>();
            voice = multipartRequest.getFiles("voice");
            if (voice.size() > 0) {
                for (MultipartFile voiceFile : voice) {

                    String key = null;
                    try {
                        if (null != voiceFile && voiceFile.getBytes().length > 0) {
                            key = OSUtils.upload(voiceFile.getBytes(), false, account);

                            DynamicSound v = new DynamicSound();
                            v.setCid(cid);
                            v.setVoicepath(key);
                            v.setTime(Integer.valueOf(time));
                            Integer integer = voiceMapper.insert(v);
                            if (integer != 1) {
                                throw new ServiceException("发布失败");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * 发布动态返回主键
     */
    @Override
    public Integer publishdynamicofwritten(Dynamic dynamic) {
        Integer integer = dynamicMapper.insert(dynamic);
        if (integer != 1) {
            throw new ServiceException("发布失败");
        }

        return dynamic.getId();
    }

    /**
     * 删除动态
     */
    @Override
    public Result deleteDynamic(HttpServletRequest request) {
        String account = request.getParameter("account");
        String id = request.getParameter("id");//帖子的id
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(id)) {
            return new Result(1, "", null);
        }
        Integer integer = dynamicMapper.deleteDynamic(account, Integer.valueOf(id));
        if (integer != 1) {
            return new Result(1, "删除失败", null);
        }
        return new Result(0, "", null);
    }


    /**
     * 查看自己的动态
     */
    @Override
    public List<Map<String, Object>> lookoneSelfDynamic(HttpServletRequest request) {
        String myaccount = request.getParameter("myaccount");
        String account = request.getParameter("account");
        if (myaccount == null) {
            myaccount = account;
        }
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }

        System.out.println("page" + Integer.parseInt(page));
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<Dynamic> dynamics = dynamicMapper.lookoneSelfDynamic(account);
        List<Map<String, Object>> maps = new ArrayList<>();
        for (Dynamic dynamic : dynamics) {
            Map<String, Object> map = new HashMap<>();
            String cid = String.valueOf(dynamic.getId());
            UserInfo user = userMapper.getUserByAccount(account);
            //添加个人信息
            map.put("username", user.getUsername());//昵称
            map.put("headimg", OSUtils.getUrl(user.getAvatar()));//头像
            map.put("sex", user.getSex());//性别
            map.put("city", user.getCity());//城市
            String addtime = DateUtil.timeDifference(dynamic.getAddTime());
            if (addtime == null) {
                addtime = "刚刚";
            }
            map.put("addtime", addtime);//相隔时间
            //添加文字说明
            map.put("id", dynamic.getId());
            map.put("written", dynamic.getContent());//文字说明
            //图片一个list
            List<DynamicImg> dynamicimgs = dynamicImgMapper.getDynamicImgBycid(cid);
            List<String> imgs = new ArrayList<>();
            for (DynamicImg dynamicimg : dynamicimgs) {

                imgs.add(OSUtils.getUrl(dynamicimg.getImgPath()));
            }
            map.put("picture", imgs);//图片地址
            List<Map<String, String>> list = new ArrayList<>();

            //添加voice 声音只有一个
            DynamicSound voice = voiceMapper.getDynamicSoundBycid(cid);
            if (voice != null) {
                voice.setVoicepath(OSUtils.getUrl(voice.getVoicepath()));
                map.put("voice", voice);
            }

            //视频一个list 暂不提供视频功能
			/*List<Dynamicvideo> dynamicvideos = plazaMapper.getdynamicvideobycid(cid);
			for (Dynamicvideo dynamicvideo : dynamicvideos) {
				Map<String, String> maps1=new HashMap<>();
				maps1.put("imgpath", CommonUtils.addpath(request,dynamicvideo.getImgpath()));
				maps1.put("videopath", CommonUtils.addpath(request,dynamicvideo.getVideopath()));
				list.add(maps1);
			}
			map.put("video", list);//视频地址*/

            Integer countlike = dynamicPraiseMapper.getDynamicLikeCount(cid);
            map.put("countlike", countlike);//点赞的个数
            //自己是否点赞
            List<DynamicPraise> list5 = dynamicPraiseMapper.getDynamicLike(cid);
            List<String> listacc = new ArrayList<>();
            for (DynamicPraise dynamiclike : list5) {
                listacc.add(dynamiclike.getAccount());
            }
            if (listacc.contains(myaccount)) {
                map.put("mylike", "1");//自己是否点赞1点赞，2没有点赞
            } else {
                map.put("mylike", "2");//自己是否点赞1点赞，2没有点赞
            }
            //TODO 评论 只获取评论的数量
            Integer countComment = dynamicCommentMapper.getDynamicCommentCount(cid);
            map.put("comment", countComment);//评论数量
            maps.add(map);
            /*List<DynamicComment> dynamicComments = plazaMapper.getdynamicCommentBycid(cid);
            List<Map<String, String>> commentlist = new ArrayList<>();
            for (DynamicComment dynamicComment : dynamicComments) {
                String pinglunaccount = dynamicComment.getAccount();
                String commenttype = dynamicComment.getCommenttype();
                if ("1".equals(commenttype)) {
                    //是评论
                    Map<String, String> map2 = new HashMap<>();
                    map2.put("commentaccount", pinglunaccount);//评论人帐号
                    User user2 = userMapper.getuserbyaccount(pinglunaccount);
                    map2.put("commenttext", dynamicComment.getCommenttext());//评论人评论内容
                    map2.put("commentheadimg", CommonUtils.addpath(request, user2.getHeadimg()));//评论人头像
                    map2.put("commentusername", user2.getUsername());//评论人帐号
                    map2.put("commenttype", commenttype);//评论的内容（1.文字，2.礼物）
                    commentlist.add(map2);
                } else {
                    //是礼物
                }
            }*/
        }
        return maps;
    }


    /**
     * 查看所有动态
     */
    @Override
    public List<Map<String, Object>> lookplaza(HttpServletRequest request) {
        String account = request.getParameter("account");
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        String type = request.getParameter("type");//动态类型 不传值代表查看所有的


        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }


        List<Dynamic> dynamics = null;
        if (StringUtils.isEmpty(type) || type.equals("0")) {//查看所有的  0代表默认所有的
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
            dynamics = dynamicMapper.selectTotalCount();//获取进行查询
        } else {
            PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
            dynamics = dynamicMapper.selectDynamicByType(Integer.valueOf(type));//获取进行查询
        }


        List<Map<String, Object>> maps = new ArrayList<>();
        //List<Dynamic> dynamics = plazaMapper.lookplazadynamic(Integer.parseInt(page), Integer.parseInt(count));
        for (Dynamic dynamic : dynamics) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", dynamic.getType() + "");//动态类型100 代表是官方帖子 app端要给这个帖子置顶并添加角标


            String cid = String.valueOf(dynamic.getId());
            String acc = dynamic.getAccount();

            UserInfo user = userMapper.getUserByAccount(acc);
            //添加个人信息
            map.put("username", user.getUsername());//昵称
            map.put("account", user.getAccount());//昵称
            map.put("headimg", OSUtils.getUrl(user.getAvatar()));//头像
            map.put("sex", user.getSex());//性别
            map.put("city", user.getCity());//城市
            String addtime = DateUtil.timeDifference(dynamic.getAddTime());
            if (addtime == null) {
                addtime = "刚刚";
            }
            map.put("addtime", addtime);//相隔时间
            //添加文字说明
            map.put("id", dynamic.getId());
            map.put("written", dynamic.getContent());//文字说明

            //查询用户是不是VIP
            Integer isHavePer = checkMatchPer(user.getAccount());
            if(isHavePer== ZxfConstans.NOT_VIP){
                map.put("vip",ZxfConstans.NOT_VIP+"");
            }else {
                map.put("vip",ZxfConstans.IS_VIP+"");
            }

            //图片一个list
            List<DynamicImg> dynamicimgs = dynamicImgMapper.getDynamicImgBycid(cid);
            List<String> imgs = new ArrayList<>();
            for (DynamicImg dynamicimg : dynamicimgs) {

                //添加图片地址
                imgs.add(OSUtils.getUrl(dynamicimg.getImgPath()));
            }
            map.put("picture", imgs);//图片地址
            List<Map<String, String>> list = new ArrayList<>();

            //添加voice 声音只有一个
            DynamicSound voice = voiceMapper.getDynamicSoundBycid(cid);
            if (voice != null) {
                voice.setVoicepath(OSUtils.getUrl(voice.getVoicepath()));
                map.put("voice", voice);
            }


            //暂时不提供视频地址
			/*List<Dynamicvideo> dynamicvideos=plazaMapper.getdynamicvideobycid(cid);
			//视频一个list
			for (Dynamicvideo dynamicvideo : dynamicvideos) {
				Map<String, String> maps1=new HashMap<>();
				maps1.put("imgpath", CommonUtils.addpath(request,dynamicvideo.getImgpath()));
				maps1.put("videopath", CommonUtils.addpath(request,dynamicvideo.getVideopath()));
				list.add(maps1);
			}
			map.put("video", list);//视频地址*/
            Integer countlike = dynamicPraiseMapper.getDynamicLikeCount(cid);
            map.put("countlike", countlike);//点赞的个数
            //自己是否点赞
            List<DynamicPraise> list5 = dynamicPraiseMapper.getDynamicLike(cid);
            List<String> listacc = new ArrayList<>();
            for (DynamicPraise dynamiclike : list5) {
                listacc.add(dynamiclike.getAccount());
            }
            if (listacc.contains(account)) {
                map.put("mylike", "1");//自己是否点赞1点赞，2没有点赞
            } else {
                map.put("mylike", "2");//自己是否点赞1点赞，2没有点赞
            }


            Integer countComment = dynamicCommentMapper.getDynamicCommentCount(cid);
            map.put("comment", countComment);//评论内容

            Integer view_count = dynamicViewMapper.selectCountByCid(dynamic.getId());
            map.put("viewCount", view_count);

            maps.add(map);


            //插入数据
            DynamicView dynamicView = new DynamicView();
            dynamicView.setCid(dynamic.getId());
            dynamicView.setViewAccount(account);
            dynamicViewMapper.insert(dynamicView);
        }

        return maps;
    }

    //TODO 查看自己关注的暂时不做
    @Override
    public List<Map<String, Object>> look_attention_square(HttpServletRequest request) {
        return null;

    }

    /**
     * 查看某个帖子
     */

    @Override
    public Result<Map<String, Object>> lookOneDynamic(HttpServletRequest request) {
        String account = request.getParameter("account");
        String id = request.getParameter("id");//帖子的id
        if (StringUtils.isEmpty(account) || StringUtils.isEmpty(id)) {
            return new Result<>(1, "", null);
        }

        Dynamic dynamic = dynamicMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (null == dynamic) {
            return new Result<>(1, "", null);
        } else if (dynamic.getStatus() == 1) {//代表该帖子已经被删除了
            return new Result<>(0, "404", null);
        }


        Map<String, Object> map = new HashMap<>();
        String cid = String.valueOf(dynamic.getId());
        String acc = dynamic.getAccount();

        UserInfo user = userMapper.getUserByAccount(acc);
        //添加个人信息
        map.put("username", user.getUsername());//昵称
        map.put("headimg", OSUtils.getUrl(user.getAvatar()));//头像
        map.put("sex", user.getSex());//性别
        map.put("city", user.getCity());//城市
        String addtime = DateUtil.timeDifference(dynamic.getAddTime());
        if (addtime == null) {
            addtime = "刚刚";
        }
        map.put("addtime", addtime);//相隔时间


        //添加文字说明
        map.put("id", dynamic.getId());
        map.put("written", dynamic.getContent());//文字说明
        //图片一个list
        List<DynamicImg> dynamicimgs = dynamicImgMapper.getDynamicImgBycid(cid);
        List<String> imgs = new ArrayList<>();
        for (DynamicImg dynamicimg : dynamicimgs) {

            //添加图片地址
            imgs.add(OSUtils.getUrl(dynamicimg.getImgPath()));
        }
        map.put("picture", imgs);//图片地址
        List<Map<String, String>> list = new ArrayList<>();

        Integer countlike = dynamicPraiseMapper.getDynamicLikeCount(cid);
        map.put("countlike", countlike);//点赞的个数
        //自己是否点赞
        List<DynamicPraise> list5 = dynamicPraiseMapper.getDynamicLike(cid);
        List<String> listacc = new ArrayList<>();
        for (DynamicPraise dynamiclike : list5) {
            listacc.add(dynamiclike.getAccount());
        }
        if (listacc.contains(account)) {
            map.put("mylike", "1");//自己是否点赞1点赞，2没有点赞
        } else {
            map.put("mylike", "2");//自己是否点赞1点赞，2没有点赞
        }


        Integer countComment = dynamicCommentMapper.getDynamicCommentCount(cid);
        map.put("comment", countComment);//评论内容


        return new Result<>(0, "获取帖子详情成功", map);


    }


    /**
     * 查看某个帖子的评论和回复信息
     *
     * @param request
     * @return
     */
    @Override
    public Result<CommentReplyData> lookplazaDetail(HttpServletRequest request) {
        //评论
        String cid = request.getParameter("cid"); //帖子的id
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }

        //检查该帖子是否已经被删除
        Dynamic dynamic = dynamicMapper.selectByPrimaryKey(Integer.valueOf(cid));
        if (null == dynamic) {
            return new Result(1, "", null);
        } else if (dynamic.getStatus() == 1) {//代表该帖子已经被删除了
            return new Result(0, "404", null);
        }

        //获取评论
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<DynamicComment> dynamicComments = dynamicCommentMapper.getDynamicCommentByCid(cid);
        List<Map<String, Object>> commentlist = new ArrayList<>();

        //封装返回给客户端的数据
        CommentReplyData data = new CommentReplyData();
        data.setTotal(dynamicComments.size());
        List<CommentDetailBean> detailBeanList = new ArrayList<>();

        for (DynamicComment dynamicComment : dynamicComments) {
            String pinglunaccount = dynamicComment.getAccount();
            String commenttype = dynamicComment.getCommentType();

            String commentAddTime = DateUtil.timeDifference(dynamicComment.getAddTime());
            if (commentAddTime == null) {
                commentAddTime = "刚刚";
            }

            if ("1".equals(commenttype)) {
                //是评论
                Map<String, Object> map2 = new HashMap<>();


                map2.put("commentaccount", pinglunaccount);//评论人帐号

                UserInfo user2 = userMapper.getUserByAccount(pinglunaccount);

                CommentDetailBean detailBean = new CommentDetailBean();
                detailBean.setNickName(user2.getUsername());
                detailBean.setId(dynamicComment.getId());
                detailBean.setContent(dynamicComment.getCommentContent());
                detailBean.setUserLogo(OSUtils.getUrl(user2.getAvatar()));
                detailBean.setCreateDate(commentAddTime);


                //查询评论下对应的回复[限制为最多三条 查看更多时再用另外接口返回分页数据]
                List<DynamicCommentReply> replyList = commentReplyMapper.getReplyBycidAndLimit(dynamicComment.getId());//根据评论Id查回复

                List<ReplyDetailBean> replyDetailBeanList = new ArrayList<>();

                if (null == replyList || replyList.size() == 0) {
                    detailBean.setReplyTotal(0);
                } else {
                    for (DynamicCommentReply reply : replyList) {
                        String replyAddTime = DateUtil.timeDifference(reply.getAddTime());
                        if (replyAddTime == null) {
                            replyAddTime = "刚刚";
                        }

                        UserInfo replyUser = userMapper.getUserByAccount(reply.getAccount());
                        if (replyUser != null) {
                            ReplyDetailBean replyDetailBean = new ReplyDetailBean();
                            replyDetailBean.setCommentId(String.valueOf(dynamicComment.getId()));//回复对应的评论id
                            replyDetailBean.setCreateDate(replyAddTime);
                            replyDetailBean.setStatus(String.valueOf(reply.getStatus()));

                            replyDetailBean.setContent(reply.getReplyContent());
                            replyDetailBean.setNickName(replyUser.getUsername());
                            replyDetailBean.setUserLogo(OSUtils.getUrl(replyUser.getAvatar()));
                            replyDetailBeanList.add(replyDetailBean);
                        }
                    }

                    detailBean.setReplyList(replyDetailBeanList);
                }


                detailBeanList.add(detailBean);
            } else {
                //是礼物
            }
        }

        data.setList(detailBeanList);

        return new Result<CommentReplyData>(0, "获取成功", data);

    }


    @Override
    public Result<List<ReplyDetailBean>> commentDetail(HttpServletRequest request) {
        String account = request.getParameter("account");//点赞人的账号
        String cid = request.getParameter("cid");//评论的id
        if (StringUtils.isEmpty(cid) || StringUtils.isEmpty(account)) {
            return new Result(1, "缺少参数", null);
        }
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<DynamicCommentReply> replyList = commentReplyMapper.getReplyBycid(Integer.valueOf(cid));//根据评论Id查回复

        List<ReplyDetailBean> replyDetailBeanList = new ArrayList<>();
        if (null == replyList || replyList.size() == 0) {
            new Result<CommentReplyData>(0, "获取成功", null);
        } else {
            for (DynamicCommentReply reply : replyList) {
                String replyAddTime = DateUtil.timeDifference(reply.getAddTime());
                if (replyAddTime == null) {
                    replyAddTime = "刚刚";
                }

                UserInfo replyUser = userMapper.getUserByAccount(reply.getAccount());
                if (replyUser != null) {
                    ReplyDetailBean replyDetailBean = new ReplyDetailBean();
                    replyDetailBean.setCommentId(String.valueOf(cid));//回复对应的评论id
                    replyDetailBean.setCreateDate(replyAddTime);
                    replyDetailBean.setStatus(String.valueOf(reply.getStatus()));

                    replyDetailBean.setContent(reply.getReplyContent());
                    replyDetailBean.setNickName(replyUser.getUsername());
                    replyDetailBean.setUserLogo(OSUtils.getUrl(replyUser.getAvatar()));
                    replyDetailBeanList.add(replyDetailBean);
                }
            }
        }

        return new Result(0, "获取成功", replyDetailBeanList);
    }


    /**
     * 进行点赞
     */
    @Transactional
    @Override
    public void dynamiclike(HttpServletRequest request) {
        String account = request.getParameter("account");//点赞人的账号
        String id = request.getParameter("id");//动态的id
        String addtime = DateUtil.getCurrentTime();//点赞时间
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(account)) {
            throw new ServiceException("缺少参数");
        }
        List<DynamicPraise> list = dynamicPraiseMapper.getDynamicLike(id);
        List<String> listacc = new ArrayList<>();
        for (DynamicPraise dynamiclike : list) {
            listacc.add(dynamiclike.getAccount());
        }
        if (listacc.contains(account)) {//判断该条帖子点赞的人有没有点赞用户
            throw new ServiceException("请不要重复点赞");
        } else {//自己没有点赞过
            DynamicPraise dynamicLike = new DynamicPraise();
            dynamicLike.setCid(Integer.valueOf(id));
            dynamicLike.setAccount(account);
            dynamicLike.setAddTime(addtime);

            //获取该条动态所属的账号
            Dynamic dynamic = dynamicMapper.selectByPrimaryKey(Integer.valueOf(id));
            dynamicLike.setDynamicAccount(dynamic.getAccount());

            Integer integer = dynamicPraiseMapper.insertSelective(dynamicLike);
            if (integer != 1) {
                throw new ServiceException("点赞失败");
            } else {
                //点赞消息传递给被点赞者
                //1. 校验这个动态帖子是谁发布的，判断是不是自己的点赞

                if (!dynamic.getAccount().equals(account)) {//不是点赞的自己
                    //2.给被点赞者发透传消息告诉它被点赞了

                    UserInfo user = userMapper.getUserByAccount(account);

                    //构建点赞消息的Json字符串
                    CommonMessage message = new CommonMessage();
                    message.setLikeAccount(account);
                    message.setLikeHeadImg(OSUtils.getUrl(user.getAvatar()));
                    message.setLikeNickName(user.getUsername());
                    message.setLikeCid(id);

                    String jsonContent = new Gson().toJson(message);
                    //传递给发布这条动态的人
                    XGUtils.pushSingleAccount("1", jsonContent, 86400, dynamic.getAccount());//点赞透传消息.24小时候过期
                }

            }
        }

    }

    //获取当前用户点赞别人的记录
    @Override
    public Result<List<DynamicPraise>> getSelfPraise(HttpServletRequest request) {
        String account = request.getParameter("account");//点赞人的账号
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "缺少参数", null);
        }

        List<DynamicPraise> praises = dynamicPraiseMapper.selectByAccount(account);
        for (DynamicPraise praise : praises) {
            String addtime = DateUtil.timeDifference(praise.getAddTime());
            if (addtime == null) {
                addtime = "刚刚";
            }
            praise.setAddTime(addtime);
        }

        return new Result(0, "", praises);
    }

    //获取其他用户点赞当前用户的记录
    @Override
    public Result<List<DynamicPraise>> getPraise(HttpServletRequest request) {
        String account = request.getParameter("account");//点赞人的账号
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "缺少参数", null);
        }

        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<DynamicPraise> praises = dynamicPraiseMapper.selectByDynamicAccount(account);

        List<DynamicPraiseResponse> responses = new ArrayList<>();
        for (DynamicPraise praise : praises) {
            Dynamic dynamic = dynamicMapper.selectByPrimaryKey(praise.getCid());
            if (dynamic.getAccount().equals(praise.getAccount())) {//自己点赞自己就不显示
                continue;
            }
            UserInfo user = userMapper.getUserByAccount(praise.getAccount());
            user.setToken(null);
            user.setWechat(null);
            user.setQqAccount(null);
            user.setAvatar(OSUtils.getUrl(user.getAvatar()));
            //封装成带其他用户个人信息的点赞对象
            DynamicPraiseResponse response = new DynamicPraiseResponse();
            String addtime = DateUtil.timeDifference(praise.getAddTime());
            if (addtime == null) {
                addtime = "刚刚";
            }
            response.praise = praise;
            response.praise.setAddTime(addtime);
            response.userInfo = user;
            response.dynamicContent = dynamic.getContent();
            responses.add(response);
        }

        return new Result(0, "", responses);
    }


    /**
     * 发起评论
     */
    @Override
    public void dynamiccomment(HttpServletRequest request) {
        String account = request.getParameter("account");//评论人的账号
        String id = request.getParameter("id");//动态的id
        String commenttext = request.getParameter("commenttext");
        if (StringUtils.isEmpty(id)) {
            throw new ServiceException("动态id不能为空");
        }
        DynamicComment dynamicComment = new DynamicComment();
        dynamicComment.setAccount(account);
        dynamicComment.setCid(Integer.valueOf(id));
        dynamicComment.setCommentType("1");//1.文字评论，2礼物
        dynamicComment.setCommentContent(commenttext);
        String addtime = DateUtil.getCurrentTime();//评论时间

        dynamicComment.setAddTime(addtime);


        Dynamic dynamic = dynamicMapper.selectByPrimaryKey(Integer.valueOf(id));
        dynamicComment.setDynamicAccount(dynamic.getAccount());//设置该条评论所属动态的账号

        Integer integer = dynamicCommentMapper.insert(dynamicComment);
        if (integer != 1) {
            throw new ServiceException("评论失败");
        } else {
            //评论消息传递给被评论者 参考点赞消息注释(点赞和评论消息可以通用)
            //1. 校验这个动态帖子是谁发布的，判断是不是自己评论的

            if (!dynamic.getAccount().equals(account)) {//不是自己评论自己

                UserInfo user = userMapper.getUserByAccount(account);


                CommonMessage message = new CommonMessage();
                message.setLikeAccount(account);
                message.setLikeHeadImg(OSUtils.getUrl(user.getAvatar()));
                message.setLikeNickName(user.getUsername());
                message.setLikeCid(id);

                String jsonContent = new Gson().toJson(message);
                XGUtils.pushSingleAccount("2", jsonContent, 86400, dynamic.getAccount());//点赞透传消息.24小时候过期
            }
        }

    }

    /**
     * 发起回复
     *
     * @param request
     * @return
     */
    @Override
    public Result dynamicReply(HttpServletRequest request) {

        String account = request.getParameter("account");//评论人的账号
        String id = request.getParameter("id");//被回复的评论id
        String replyContent = request.getParameter("replyContent");
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(account)) {

            return new Result(1, "", null);
        }


        DynamicCommentReply reply = new DynamicCommentReply();
        reply.setAccount(account);//谁回复的
        reply.setCid(Integer.valueOf(id));//被回复的评论id
        String addtime = DateUtil.getCurrentTime();//回复时间
        reply.setAddTime(addtime);
        reply.setReplyType("1");
        reply.setReplyContent(replyContent);

        DynamicComment dynamicComment = dynamicCommentMapper.selectByPrimaryKey(Integer.valueOf(id));
        reply.setCommentAccount(dynamicComment.getAccount());//设置回复的评论人的所属账号
        reply.setDynaId(dynamicComment.getCid());//设置该条评论所属动态的ID


        Integer integer = commentReplyMapper.insert(reply);

        if (integer != 1) {

            return new Result(1, "", null);
        } else {
            //回复消息传递给被回复者 参考点赞消息注释(点赞和评论消息可以通用)
            //Todo 要不要传递给该条动态的拥有者
            if (!dynamicComment.getAccount().equals(account)) {//判断是不是自己回复自己

                UserInfo user = userMapper.getUserByAccount(account);

                CommonMessage message = new CommonMessage();
                message.setLikeAccount(account);
                message.setLikeHeadImg(OSUtils.getUrl(user.getAvatar()));
                message.setLikeNickName(user.getUsername());
                message.setLikeCid(id);

                String jsonContent = new Gson().toJson(message);
                XGUtils.pushSingleAccount("2", jsonContent, 86400, dynamicComment.getAccount());//点赞透传消息.24小时候过期
            }
        }


        return new Result(0, "", null);
    }


    //获取其他用户评论当前用户的记录
    @Override
    public Result<List<DynamicCommentResponse>> getComment(HttpServletRequest request) {
        String account = request.getParameter("account");//评论人的账号
        if (StringUtils.isEmpty(account)) {
            return new Result(1, "缺少参数", null);
        }
        String page = request.getParameter("page");//第几页开始查
        String count = request.getParameter("count");//每页数量
        //查看自己所有的动态（分页显示）
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(count)) {
            count = "10";
        }

        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(count));
        List<DynamicComment> comments = dynamicCommentMapper.selectByDynaicAccount(account);
        List<DynamicCommentResponse> responses = new ArrayList<>();
        for (DynamicComment comment : comments) {
            Dynamic dynamic = dynamicMapper.selectByPrimaryKey(comment.getCid());
            if (dynamic.getAccount().equals(comment.getAccount())) {//自己评论自己就不显示
                continue;
            }
            UserInfo user = userMapper.getUserByAccount(comment.getAccount());
            user.setToken(null);
            user.setWechat(null);
            user.setQqAccount(null);
            user.setAvatar(OSUtils.getUrl(user.getAvatar()));
            //封装成带其他用户个人信息的点赞对象
            DynamicCommentResponse response = new DynamicCommentResponse();
            String addtime = DateUtil.timeDifference(comment.getAddTime());
            if (addtime == null) {
                addtime = "刚刚";
            }
            comment.setAddTime(addtime);
            response.comment = comment;
            response.userInfo = user;
            response.dynamicContent = dynamic.getContent();
            responses.add(response);
        }


        return new Result(0, "", responses);
    }


    //检查用户是否是VIP
    private Integer checkMatchPer(String account) {
        //检查是不是VIP
        UserVip userVip = vipUSerService.getVipByAccount(account);
        if (userVip != null) {
            String fishtime = userVip.getFishtime();//VIP结束时间
            String end = DateUtil.getCurrentDate();//当前时间
            try {
                if (!DateUtil.compare(fishtime, end)) {
                    //会员没有过期
                    return ZxfConstans.IS_VIP;
                }else {
                    return ZxfConstans.NOT_VIP;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return ZxfConstans.NOT_VIP;
        } else {
            return ZxfConstans.NOT_VIP;
        }
    }
}
