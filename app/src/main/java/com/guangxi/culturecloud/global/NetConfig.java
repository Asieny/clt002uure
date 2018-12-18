package com.guangxi.culturecloud.global;

/**
 * 网络相关配置
 *
 * @author zww
 */
public class NetConfig {
    //游客ID
    public static       String UserID            = "28";
    //第一个链接，定位后，用来修改链接头，获取不同内容
    public static final String FIRSTURL          = "http://222.216.241.136:8081/whyapi/serApiAddressByArea";
    //登陆明细
    public static final String INSERT_LOGINCOUNT = "http://222.216.241.136:8081/whyapi/insertLoginCount";

//    public static String ROOT                     = "http://220.248.107.62:8084/whyapi/";
    public static String ROOT                     = "http://222.216.241.136:8081/whyapi/";
    //链接请求头
    public static String URL_HEAD_ADDRESS         = "http://222.216.241.136:8081/whyapi/";
    //提交图片接口（可根据地区改变）
    public static String URL_CHANGE_UPLOAD_BASE64 = ROOT + "uploadBase64";

    public static String IMG      = "http://222.216.241.136:8081/upFiles/";
    //用户提交的图片前面加地址
    public static String HEAD_IMG = "http://222.216.241.136:8081/upFiles/photo/";

    //提交图片接口
    public static String UPLOAD_BASE64 = ROOT + "uploadBase64";

    //提交头像
    public static String INSERTPIC     = ROOT + "insertPic";
    //首页轮播图地址
    public static String BANNERLIST    = ROOT + "bannerList";
    //首页活动推荐
    //用户注册
    public static String REGISTER      = ROOT + "register";
    //用户发送短信验证码
    public static String CHECK_MESSAGE = ROOT + "checkMessage";

    //用户登录
    public static String LOGIN           = ROOT + "login?";
    //修改密码
    public static String MODIFYPSW       = ROOT + "modifyPsw?";
    //图书馆分类
    public static String LIBRARYTYPELIST = ROOT + "libraryTypeList";
    //美术馆分类
    public static String ARTSTYPELIST    = ROOT + "artsTypeList";

    //图书馆详情
    public static String LIBRARY_Detail          = ROOT + "searchLibraryInfo";
    //美术馆详情
    public static String ARTS_Detail             = ROOT + "searchArtsInfo";
    //社区活动列表
    public static String COMMUNITY_ACTIVITY_LIST = ROOT + "communityActivityList";
    //社区活动详情
    public static String COMMUNITY_DETAIL        = ROOT + "searchCommunityInfo";

    //民族文化分类
    public static String NATION_TYPE_LIST = ROOT + "nationTypeList";
    //体育场馆分类
    public static String SPORT_TYPE_LIST  = ROOT + "sportsTypeList";

    //民族文化详情
    public static String NATION_Detail = ROOT + "nationInfo";
    //体育馆详情
    public static String SPORT_Detail  = ROOT + "searchSportsInfo";


    //民族文化GET /nationList
    public static String NATIONLIST      = ROOT + "nationList?";
    //体育场馆GET /sportsActivityList
    public static String SPORTLIST       = ROOT + "sportsActivityList?";
    //文化艺术分类
    public static String CULTURETYPELIST = ROOT + "cultureTypeList";

    //文化艺术详情
    public static String CULTURE_Detail = ROOT + "searchCultureInfo";


    //博物馆分类
    public static String MUSEUMTYPELIST = ROOT + "museumTypeList";
    //博物馆详情
    public static String MUSEUM_Detail  = ROOT + "searchMuseumInfo";
    //影剧院详情
    public static String FILM_Detail    = ROOT + "searchTheaterInfo";

    //图书馆列表
    public static String LIBRARYACTIVITYLIST = ROOT + "libraryActivityList?";
    //美术馆
    public static String ARTSACTIVITYLIST    = ROOT + "artsActivityList?";
    //文化艺术
    public static String CULTUREACTIVITYLIST = ROOT + "cultureActivityList?";
    //博物馆列表
    public static String MUSEUMACTIVITYLIST  = ROOT + "museumActivityList?";
    //活动众筹
    public static String CROWDFUNDINGLIST    = ROOT + "crowdfundingList?";

    //活动众筹详情
    public static String SEARCHCROWDFUNDINGINFO = ROOT + "searchCrowdfundingInfo?";
    //订单列表
    public static String SEARCHORDERLIST        = ROOT + "searchOrderList?";
    //订单列表里的活动详情
    public static String SERACTIVITYINFO        = ROOT + "serActivityInfo";

    //推荐列表GET /recommendList
    public static String RECOMMND_LIST = ROOT + "recommendList";
    public static String RECOMMND_INFO = ROOT + "recommendInfo";

    //大活动GET /bigEventList
    public static String BIGEVENT_LIST = ROOT + "bigEventList";
    //大活动详情 GET /bigEventInfo
    public static String BIGEVENT_INFO = ROOT + "bigEventInfo";

    //影剧院
    public static String THEATER_LIST = ROOT + "theaterActivityList";


    //活动详情
    public static String SEARCH_THEATER_INFO = ROOT + "searchTheaterInfo";
    //点赞列表接口
    public static String ZAN_LIST            = ROOT + "zanList";
    //评论列表
    public static String COMMENT_LIST        = ROOT + "commentList";

    //提交订单
    public static String INSERT_WHYORDER = ROOT + "insertWhyOrder.do";

    //成功提交订单POST /whyOrderNotify.do
    public static String INSERT_WHYORDER_OK = ROOT + "whyOrderNotify.do";

    //取消提交订单POST GET /cancleWhyOrder
    public static String CANCEL_WHYORDER = ROOT + "cancleWhyOrder.do";


    //点赞接口
    public static String INSERT_ZAN = ROOT + "insertZan";
    //取消点赞
    public static String CANCEL_ZAN = ROOT + "cancelZan";

    //关注
    public static String INSERT_FAVOURITE     = ROOT + "insertFavourite";
    //取消关注
    public static String CANCEL_FAVOURITE     = ROOT + "cancelFavourite";
    //客户收藏(除活动和馆类之外调用)
    public static String INSERTOTHERFAVOURITE = ROOT + "insertOtherFavourite";

    //爱社团
    public static String LOVE_SOCIETY       = ROOT + "loveSocietyList";
    //爱社团社团详情
    public static String LOVE_SOCIETY_INFO  = ROOT + "loveSocietyInfo";
    //浇花
    public static String INSERT_JIAOHUA     = ROOT + "insertJiaohua";
    //入驻文化云
    public static String INSERT_LOVESOCIETY = ROOT + "insertLoveSociety";

    //志愿者
    public static String VOLUNTEER_ACTIVITYLIST = ROOT + "volunteerActivityList";
    //志愿者招聘详情
    public static String SEARCH_VOLUNTEERINFO   = ROOT + "searchVolunteerInfo";
    //注册志愿者
    public static String REGISTER_VOLUNTEER     = ROOT + "registerVolunteer";

    //文化空间，商家列表
    public static String BUSINESS_LIST      = ROOT + "businessList";
    //商家详情
    public static String BUSINESS_INFO      = ROOT + "businessInfo";
    public static String BUSINESS_GOODSLIST = ROOT + "businessGoodsList";
    //商家活动室详情
    public static String BUSINESS_PLAYROOM  = ROOT + "businessplayroom";

    //广场列表
    public static String GCZAN_LIST                 = ROOT + "gcZanList";
    //点赞讨厌投票
    public static String INSERT_ZANCOL              = ROOT + "insertZanCol";
    //广场活动排行榜
    public static String SERRANKING_GCACTIVITY_LIST = ROOT + "serRankingGcActivityList";
    //广场发布
    public static String INSERT_GC                  = ROOT + "insertGc";

    //实名认证
    public static String INSERT_SMRZ          = ROOT + "insertSmrz";
    //个人资质认证
    public static String INSERT_ZZRZPERSONNEL = ROOT + "insertZzrzPersonnel";
    //社团资质认证
    public static String INSERT_ZZRZST        = ROOT + "insertZzrzSt";
    //公司资质认证
    public static String INSERT_ZZRZCOMPANY   = ROOT + "insertZzrzCompany";

    //找回密码
    public static String BACK_PSW          = ROOT + "backPsw";
    //帮助和反馈
    public static String INSERT_HELP       = ROOT + "insertHelp";
    //申请入驻
    public static String INSERT_JOIN       = ROOT + "insertJoin";
    //提交评论
    public static String INSERT_COMMENT    = ROOT + "insertComment";
    //个人收藏列表
    public static String MAINFAVOURITELIST = ROOT + "mainfavouriteList";
    //个人评论列表
    public static String MAINCOMMENTLIST   = ROOT + "maincommentList";
    //帮助规则
    public static String SERHELP_RULE      = ROOT + "serHelpRule";
    //积分规则
    public static String POINTS_RULE      = ROOT + "serPointsRule";
    //分享录入积分
    public static String INSERT_SHARE      = ROOT + "insertshare";
    //个人积分列表
    public static String SERUSER_POINTS      = ROOT + "seruserpoints";
}