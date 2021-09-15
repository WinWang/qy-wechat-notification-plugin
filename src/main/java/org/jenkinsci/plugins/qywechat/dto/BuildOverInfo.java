package org.jenkinsci.plugins.qywechat.dto;

import hudson.model.Result;
import hudson.model.Run;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.jenkinsci.plugins.qywechat.NotificationUtil;
import org.jenkinsci.plugins.qywechat.model.NotificationConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 结束构建的通知信息
 *
 * @author jiaju
 */
public class BuildOverInfo {

    private static final String DEBUG = "Debug";
    private static final String Preview = "Preview";
    private static final String Release = "Release";

    private static final String TZYK_DEBUG = "https://hd.get88.cn/test/tzykapk/img/tzyk_debug.png";
    private static final String TZYK_PREVIEW = "https://hd.get88.cn/test/tzykapk/img/tzyk_preview.png";
    private static final String TZYK_RELEASE = "https://hd.get88.cn/test/tzykapk/img/tzyk_release.png";
    private static final String XG_DEBUG = "https://hd.get88.cn/test/tzykapk/img/xg_debug.png";
    private static final String XG_PREVIEW = "https://hd.get88.cn/test/tzykapk/img/xg_preview.png";
    private static final String XG_RELEASE = "https://hd.get88.cn/test/tzykapk/img/xg_release.png";


    /**
     * 使用时间，毫秒
     */
    private String useTimeString = "";

    /**
     * 本次构建控制台地址
     */
    private String consoleUrl;

    /**
     * 工程名称
     */
    private String projectName;

    /**
     * 环境名称
     */
    private String topicName = "";

    /**
     * 构建分支名称
     */
    private String buildParam = "";

    /**
     * 提交构建信息
     */
    private String commitMessage = "";

    /**
     * 编译信息
     */
    private String buildMsg = "暂无用户编辑信息";

    /**
     * 编译参数化信息
     */
    private HashMap<String, Object> paramMap = null;


    /**
     * 执行结果
     */
    private Result result;

    public BuildOverInfo(String projectName, Run<?, ?> run, NotificationConfig config) {
        //使用时间
        this.useTimeString = run.getTimestampString();
//        run.
        //控制台地址
        StringBuilder urlBuilder = new StringBuilder();
        String jenkinsUrl = NotificationUtil.getJenkinsUrl();
        if (StringUtils.isNotEmpty(jenkinsUrl)) {
            String buildUrl = run.getUrl();
            urlBuilder.append(jenkinsUrl);
            if (!jenkinsUrl.endsWith("/")) {
                urlBuilder.append("/");
            }
            urlBuilder.append(buildUrl);
            if (!buildUrl.endsWith("/")) {
                urlBuilder.append("/");
            }
            urlBuilder.append("console");
        }
        this.consoleUrl = urlBuilder.toString();
        //工程名称
        this.projectName = projectName;
        //环境名称
        if (config.topicName != null) {
            topicName = config.topicName;
        }
        if (!TextUtils.isEmpty(config.buildParam)) {
            buildParam = config.buildParam;
        }
        if (!TextUtils.isEmpty(config.commitMessage)) {
            this.commitMessage = config.commitMessage;
        }

        if (!TextUtils.isEmpty(config.buildMsg)) {
            this.buildMsg = config.buildMsg;
        }

        if (config.buildHashMap != null) {
            this.paramMap = config.buildHashMap;
        }

        //结果
        result = run.getResult();
    }

    public String toJSONString() {
        boolean isTzyk = this.projectName.contains("TZYK");
        String buildType = "Debug";

        //组装内容
        StringBuilder content = new StringBuilder();
        if (StringUtils.isNotEmpty(topicName)) {
            content.append(this.topicName);
        }
        content.append("<font color=\"info\">【" + (isTzyk ? "大阳智投Android" : "西瓜智选股Android") + "】</font>" + "\n");
        content.append("<font color=\"info\">【" + this.projectName + "】</font>构建" + getStatus() + "\n");
        content.append("><font color=#E66B1A>编译参数：</font>\n");
        content.append(">" + buildParam + "\n");
        if (paramMap != null) {
            content.append("><font color=#E66B1A>编译版本：</font>");
            buildType = (String) paramMap.get("BUILD_TYPE");
            if (buildType.equals(DEBUG)) {
                content.append("测试包");
            } else if (buildType.equals(Preview)) {
                content.append("预生产包");
            } else if (buildType.equals(Release)) {
                content.append("生产包");
            }
            content.append("\n");
            content.append("><font color=#E66B1A>构建分支：</font>");
            String build_branch = (String) paramMap.get("Branch");
            content.append(build_branch);
            content.append("\n");
        }
        content.append("><font color=#E66B1A>Git提交：</font>" + (TextUtils.isEmpty(commitMessage) ? "暂无Git更新日志" : commitMessage) + "\n");
        content.append("><font color=#E66B1A>编译信息：</font>" + buildMsg + "\n");
        content.append("><font color=#E66B1A>构建用时：</font><font color=\"comment\">" + this.useTimeString + "</font>\n");
        if (this.projectName.contains("TZYK")) { //大阳智投
            content.append(">[下载地址：http://hd.get88.cn/test/tzykapk/web/index.html#/](http://hd.get88.cn/test/tzykapk/web/index.html#/)\n");
        } else if (this.projectName.contains("XGZXG")) { //西瓜智选股
            content.append(">[下载地址：https://hd.get88.cn/test/tzykapk/web/index.html#/XGZXG](http://hd.get88.cn/test/tzykapk/web/index.html#/XGZXG)\n");
        }
        if (StringUtils.isNotEmpty(this.consoleUrl)) {
            content.append(" >[查看控制台](" + this.consoleUrl);
        }
//        if (isTzyk) {
//            switch (buildType) {
//                case DEBUG:
//                    content.append("![大阳智投测试](" + TZYK_DEBUG + ")");
//                    break;
//                case Preview:
//                    content.append("![大阳智投预生产](" + TZYK_PREVIEW + ")");
//                    break;
//                case Release:
//                    content.append("![大阳智投生产](" + TZYK_RELEASE + ")");
//                    break;
//            }
//        } else {
//            switch (buildType) {
//                case DEBUG:
//                    content.append("![西瓜测试](" + XG_DEBUG + ")");
//                    break;
//                case Preview:
//                    content.append("![西瓜预生产](" + XG_PREVIEW + ")");
//                    break;
//                case Release:
//                    content.append("![西瓜生产](" + XG_RELEASE + ")");
//                    break;
//            }
//        }
        Map markdown = new HashMap<String, Object>();
        markdown.put("content", content.toString());

        Map data = new HashMap<String, Object>();
        data.put("msgtype", "markdown");
        data.put("markdown", markdown);

        String req = JSONObject.fromObject(data).toString();
        return req;
    }

    private String getStatus() {
        if (null != result && result.equals(Result.FAILURE)) {
            return "<font color=\"warning\">失败!!!</font>\uD83D\uDE2D";
        } else if (null != result && result.equals(Result.ABORTED)) {
            return "<font color=\"warning\">中断!!</font>\uD83D\uDE28";
        } else if (null != result && result.equals(Result.UNSTABLE)) {
            return "<font color=\"warning\">异常!!</font>\uD83D\uDE41";
        } else if (null != result && result.equals(Result.SUCCESS)) {
            int max = successFaces.length - 1, min = 0;
            int ran = (int) (Math.random() * (max - min) + min);
            return "<font color=\"info\">成功~</font>" + successFaces[ran];
        }
        return "<font color=\"warning\">情况未知</font>";
    }

    String[] successFaces = {
            "\uD83D\uDE0A", "\uD83D\uDE04", "\uD83D\uDE0E", "\uD83D\uDC4C", "\uD83D\uDC4D", "(o´ω`o)و", "(๑•̀ㅂ•́)و✧"
    };


}
